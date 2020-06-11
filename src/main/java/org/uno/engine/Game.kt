/*
 * Copyright (C) 2020  Fábio Furtado
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.uno.engine

import org.uno.engine.engineExceptions.CardIndexOutOfHandBoundsException
import org.uno.engine.engineExceptions.GameRulesException
import org.uno.engine.engineExceptions.MissingColourForWildCardException
import org.uno.engine.objects.*
import org.uno.util.Stack
import kotlin.random.Random

/**
 * @see UnoGame
 *
 * @author Fábio Furtado
 */
class Game private constructor(_players: Array<Player>, _deck: Stack<Card>,
                               _table: Stack<Card>,_turn: Int, _previous: Int,
                               _direction: Int, _winner: Player?) : UnoGame {

    /**
     * All the cards on the deck. The deck is where the players draw new cards.
     * It will get refilled with the cards from the table if it's close to get
     * empty.
     */
    private val deck = _deck

    /**
     * Cards which have been played
     */
    private val table = _table

    /**
     * The index of the player in turn
     */
    private var turn = _turn

    /**
     * The index of the player who played previously
     */
    private var previous = _previous

    /**
     * The direction to which the turn to play moves. 1 means it's moving towards
     * the player with higher index, -1 means it's moving towards the one with
     * lower index.
     */
    private var direction = _direction

    /**
     * The number of cards each player starts with
     */
    private val startingCards = 7

    /**
     * The players of this game
     */
    private val players = _players

    override var winner: Player? = _winner
        private set
        get() = field?.clone()

    /**
     * @see UnoGame.playerInTurn
     */
    override val playerInTurn
        get() = players[turn].clone()

    /**
     * @see UnoGame.previousPlayer
     */
    override val previousPlayer
        get() = players[previous].clone()

    override val deckTop: Card
        get() = deck.peek().clone()

    override val tableTop: Card
        get() = table.peek().clone()

    override val numberOfPlayers: Int
        get() = players.size

    override val isOver: Boolean
        get() = winner != null
    /**
     * Distributes the cards to the players and puts the first
     * card on the table.
     */
    private fun distributeAndFlip() {
        for (player in players) {
            var i = 0
            while (i < startingCards) {
                player.addToHand(deck.pop())
                i++
            }
        }

        // Making sure the first card to be flipped is a numeric one
        val aux = Stack<Card>()
        while (deck.peek().type != CardType.NUMERIC)
            aux.push(deck.pop())
        table.push(deck.pop())
        for (card in aux)
            deck.push(card)
    }

    /**
     * @see UnoGame#getPlayer(int)
     */
    override fun getPlayer(index: Int): Player = players[index].clone()

    /**
     * @see UnoGame.getPlayer
     */
    override fun getPlayer(id: String?): Player? {
        for (player in players) {
            if (id.equals(player.id))
                return player.clone()
        }
        return null
    }

    /**
     * @see UnoGame.getIndex
     */
    override fun getIndex(id: String): Int {
        for (index in players.indices) {
            if (players[index].id == id)
                return index
        }
        return -1
    }

    /**
     * @see UnoGame#isCardValid(Card)
     */
    override fun isCardValid(card: Card): Boolean {
        return when (card.type) {
            CardType.NUMERIC -> checkValidityForNumeric(card as NumericCard)
            CardType.SPECIAL -> checkValidityForSpecial(card as SpecialCard)

            // Wild cards are always valid
            else -> true
        }
    }

    private fun checkValidityForNumeric(card: NumericCard): Boolean {
        val top = table.peek()
        var isValid = false
        if (top is Numeric && card.number == (top as Numeric).number)
            isValid = true
        if (top is Colourful && card.colour === (top as Colourful).colour)
            isValid = true
        if (top is Wild && card.colour === (top as Wild).pickedColour)
            isValid = true
        return isValid
    }

    private fun checkValidityForSpecial(card: SpecialCard): Boolean {
        val top = table.peek()
        var isValid = false
        if (top is Colourful && card.colour === (top as Colourful).colour)
            isValid = true
        if (top is Symbolic && card.symbol === (top as Symbolic).symbol)
            isValid = true
        if (top is Wild && card.colour === (top as Wild).pickedColour)
            isValid = true
        return isValid
    }

    /**
     * @see UnoGame.executeMove
     */
    @Throws(CardIndexOutOfHandBoundsException::class,
            MissingColourForWildCardException::class)
    override fun executeMove(command: GameCommand): Int {
        return if (!isOver) {
            checkMoveValidity(command)
            makeSureDeckDoesNotGetEmpty()
            var returnValue = 1
            if (command.option == 0) {
                draw()
                returnValue = 0
            } else if (command.option == 1) {
                returnValue = play(command)
                if (previousPlayer.hand.isEmpty())
                    winner = players[turn]
            }
            returnValue
        } else throw IllegalStateException("This game is already over!")
    }

    /**
     * Checks if the given {@link GameCommand} is valid. Throws exceptions
     * if problems are found.
     *
     * @throws MissingColourForWildCardException if the command plays a wild card
     *                                           but does not have a chosen colour
     * @throws CardIndexOutOfHandBoundsException if the command plays a card out
     *                                           of the player in turn hand's
     *                                           index bounds
     */
    @Throws(MissingColourForWildCardException::class,
            CardIndexOutOfHandBoundsException::class)
    private fun checkMoveValidity(command: GameCommand) {
        if (command.option == 1) {
            if (command.index > players[turn].hand.size - 1)
                throw CardIndexOutOfHandBoundsException(
                        "${command.index} is out of hand range of ${players[turn].hand.size}"
                )
            else if (players[turn].hand[command.index] is Wild && command.colour == null)
                throw MissingColourForWildCardException()
        }
    }

    /**
     * If the deck size is lesser than 5, the cards from the table will be
     * reshuffled and passed the the deck so it doesn't get empty.
     */
    private fun makeSureDeckDoesNotGetEmpty() {
        if (deck.size() < 5) {
            val lastInDeck = Stack<Card>()
            val tableList = ArrayList<Card>()
            val tableTop = table.pop()
            while (!deck.isEmpty)
                lastInDeck.push(deck.pop())
            while (!table.isEmpty)
                tableList.add(table.pop())
            tableList.shuffle()
            for (card in tableList)
                deck.push(card)
            for (card in lastInDeck)
                deck.push(card)
            table.push(tableTop)
        }
    }

    private fun draw() {
        players[turn].addToHand(deck.pop())
        updatePrevious()
        move()
    }

    private fun play(command: GameCommand): Int {
        val card = players[turn].hand[command.index]
        return if (isCardValid(card)) {
            updatePrevious()
            when (card.type) {
                CardType.SPECIAL -> playSpecial(command)
                CardType.WILD -> playWild(command)

                // Numeric card
                else -> playNumeric(command)
            }
            0
        }
        else 1
    }

    private fun playSpecial(command: GameCommand) {
        table.push(players[turn].takeFromHand(command.index))
        when ((table.peek() as Symbolic).symbol) {
            SpecialCardSymbol.DRAW_2 -> {
                move()
                for (i in 0..1) players[turn].addToHand(deck.pop())
                move()
            }
            SpecialCardSymbol.REVERSE -> {
                revert()
                if (players.size > 2) move()
            }

            // Skip card
            else -> move(2)
        }
    }

    /**
     * Sets the chosen colour for the wild card and adds 4 cards to the next
     * player's hand in case the card is a wild 4.
     *
     * @param command command of the move
     */
    private fun playWild(command: GameCommand) {
        (players[turn].hand[command.index] as Wild).pickedColour = command.colour
        table.push(players[turn].takeFromHand(command.index))

        when ((table.peek() as Symbolic).symbol) {
            WildCardSymbol.DRAW_4 -> {
                move()
                for (i in 0 until 4)
                    players[turn].addToHand(deck.pop())
                move()
            }
            else -> move()
        }
    }

    private fun playNumeric(command: GameCommand) {
        table.push(players[turn].takeFromHand(command.index))
        move()
    }

    /**
     * @see UnoGame#goBot()
     */
    override fun goBot(): GameCommand {
        if (players[turn] is Bot) {
            val move = (players[turn] as Bot).makeMove(this)
            executeMove(move)
            return move
        }
        throw IllegalStateException()
    }

    /**
     * Moves the turn as many times as requested.
     *
     * @param repeat number of times to repeat this operation
     */
    private fun move(repeat: Int) {
        for (i in 0 until repeat) {
            if (direction == 1 && turn == players.size - 1)
                turn = 0
            else if (direction == -1 && turn == 0)
                turn = players.size - 1
            else turn += direction
        }
    }

    /**
     * Moves the turn to the next player.
     */
    private fun move() = move(1)

    /**
     * Updates the previous and previousPlayer properties. Should be called
     * whenever a move is about to be executed so it updates those values based
     * on the current turn and player in turn.
     */
    private fun updatePrevious() {
        previous = turn
    }

    /**
     * Reverts the turn direction. Usually done when a revert card is played.
     */
    private fun revert() {
        direction *= -1
    }

    /**
     * @see UnoGame#clone()
     */
    override fun clone() = instance(
            players.copyOf(),
            deck.clone(),
            table.clone(),
            turn, previous, direction )

    /**
     * Creates instances of [Game] class.
     */
    companion object Factory {

        /**
         * All bots name's will start with this prefix.
         */
        private const val BOT_PLAYER_NAME_PREFIX = "Bot"

        /**
         * Minimal number of players for each game.
         */
        const val MIN_NUMBER_OF_PLAYERS = 2

        /**
        * Maximal number of players for each game.
        */
        const val MAX_NUMBER_OF_PLAYERS = 8

        /**
         * This is to be used only and in no other situation other than:
         * to create a game with a previous state or to create a clone.
         * Appliance in other situations may produce unexpected behaviour. This
         * shouldn't be used especially to create a Game in a initial state.
         *
         * Make sure the following is true:
         *```
         * code players != null && players.length > 0 && deck != null &&
         * table != null && deck.size() + table.size() == 108 && turn > 0
         * && turn < players.length && (direction == 1 || direction == -1)}
         * ```
         * @param players   players for this instance
         * @param deck      deck for this instance
         * @param table     table for this instance
         * @param turn      turn for this instance
         * @param previous  previous for this instance
         * @param direction direction for this instance (1 or -1)
         */
        fun instance(players: Array<Player>,
                     deck: Stack<Card>,
                     table: Stack<Card>,
                     turn: Int,
                     previous: Int,
                     direction: Int) : Game {

            var winner:Player? = null
            for (player in players) {
                if (player.hand.isEmpty()) winner = player
            }
            return Game(players, deck, table, turn, previous, direction, winner)
        }

        /**
         * Creates a new Game with [numberOfBots] defining how many bot player's
         * it'll have and [humanPlayersNames] defining the number of human players.
         *
         * The sum of those parameters can't be lesser than [MIN_NUMBER_OF_PLAYERS]
         * or greater than [MAX_NUMBER_OF_PLAYERS].
         *
         * @param numberOfBots      number of bot's to add
         * @param humanPlayersNames array with the name of each human player
         * @return a new `Game` instance
         */
        fun instance(numberOfBots: Int, vararg humanPlayersNames: String) : Game {
            val players = arrayOfNulls<Player>(numberOfBots + humanPlayersNames.size)
            checkIfNumberOfPlayersIsLegal(players.size)
            for (i in 0 until numberOfBots)
                players[i] = BotPlayer(BOT_PLAYER_NAME_PREFIX.plus(i + 1))
            var j = 0
            for (i in numberOfBots until players.size)
                players[i] = HumanPlayer(humanPlayersNames[j++])

            val game = Game(players as Array<Player>, DeckGenerator().next(),
                    Stack(), Random.nextInt(0, players.size), 0, 1, null)
            game.distributeAndFlip()
            return game
        }

        /**
         * Checks if the given number of players is legal, throwing an Exception case
         * not.
         *
         * @param numberOfPlayers number of players to test
         * @throws GameRulesException if the given number of players is illegal
         */
        @Throws(GameRulesException::class)
        private fun checkIfNumberOfPlayersIsLegal(numberOfPlayers: Int) {
            var exceptionMessage: String? = null
            if (numberOfPlayers > MAX_NUMBER_OF_PLAYERS)
                exceptionMessage = "$numberOfPlayers players exceed the " +
                        "players limit of $MAX_NUMBER_OF_PLAYERS"
            else if (numberOfPlayers < MIN_NUMBER_OF_PLAYERS)
                exceptionMessage = "$numberOfPlayers players is not enough to " +
                        "start a game, minimum of $MIN_NUMBER_OF_PLAYERS" +
                        " is needed"
            if (exceptionMessage != null)
                throw GameRulesException(exceptionMessage)
        }
    }
}