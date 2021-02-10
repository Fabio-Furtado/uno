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
package uno.engine

import uno.engine.engineExceptions.CardIndexOutOfHandBoundsException
import uno.engine.engineExceptions.MissingColourForWildCardException
import uno.engine.objects.Card
import uno.engine.objects.Player
import java.util.*

/**
 * A uno game abstraction. Use [Game.createGame]  the get instances.
 *
 * @author Fábio Furtado
 */
interface UnoGame {

    /**
     * The player who's in turn.
     *
     * The getter will return a copy
     */
    val playerInTurn: Player

    /**
     * The player which played previously.
     *
     * This is not the player at index `turn - 1`, but the player which
     * indeed made the last move.
     * if called when the game just started and no moves have been made, the
     * return value is meaningless.
     */
    val previousPlayer: Player

    /**
     * This game's winner.
     *
     * The getter will return a copy of the player or null if the game is
     * still not finished
     */
    val winner: Player?

    /**
     * The card on the top of the deck.
     */
    val deckTop: Card

    /**
     * The card at the top of the table.
     */
    val tableTop: Card

    /**
     * Number of players in this game
     */
    val numberOfPlayers: Int

    /**
     * True if this game is over, false if not
     */
    val isOver: Boolean

    /**
     * This is the colour chosen when the last wild card was played
     *
     * It's value means nothing if no wild card has been played yet in this game
     */
    @Deprecated(message = "reference table top colour ",
        level = DeprecationLevel.WARNING)
    val lastPickedColour: CardColour

    /**
     * Returns an `Optional` with a **copy** of the player by index if it exists
     *
     * The `Optional` will have no value if the `index` is invalid
     */
    fun getPlayer(index: Int): Optional<Player>

    /**
     * Returns an Optional with **copy** of the player by id if it exists
     *
     * @param id id of the player
     */
    fun getPlayer(id: String?): Optional<Player>

    /**
     * Returns a List with **copies** of all the players in this game
     *
     * The list will be empty if the game has no players
     */
    fun getPlayers(): List<Player>

    /**
     * Returns the index of the player
     *
     * @param id player identification
     * @return index of the player, -1 if there's no player with the given id
     */
    fun getIndex(id: String): Int


    /**
     * Checks if it's valid to play the given `card`
     *
     * @return true if the card is valid, false if not
     */
    fun isCardValid(card: Card): Boolean

    /**
     * Executes the given command for the player currently in turn.
     *
     * @param command command of the move
     * @return 0 if the play was successfully executed, 1 if the move is invalid
     * @throws CardIndexOutOfHandBoundsException if the index of the card chosen
     * in the command is not valid
     * @throws MissingColourForWildCardException if no colour was specified when
     * trying to play a wild card
     * @throws IllegalStateException             if the game is already over
     */
    @Throws(CardIndexOutOfHandBoundsException::class,
            MissingColourForWildCardException::class)
    fun executeMove(command: GameCommand): Int

    /**
     * If the player in turn is a bot, it will make it's move and it will be
     * executed.
     *
     * @return command with the bot's move.
     * @throws IllegalStateException if the player in turn is not a bot
     */
    fun goBot(): GameCommand

    /**
     * Returns a copy of this instance.
     *
     * @return copy of this game
     */
    fun clone(): UnoGame
}