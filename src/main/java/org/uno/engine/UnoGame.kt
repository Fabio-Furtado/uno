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
import org.uno.engine.engineExceptions.MissingColourForWildCardException
import org.uno.engine.objects.Card
import org.uno.engine.objects.Player

/**
 * A uno game abstraction. Use [Game.instance]  the get instances.
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
     *
     * The getter will return a copy
     */
    val deckTop: Card

    /**
     * The card at the top of the table.
     *
     * The getter will return a copy
     */
    val tableTop: Card

    /**
     * Number of players in this game.
     *
     * It is a redundancy to `Game.players.size`
     */
    val numberOfPlayers: Int

    /**
     * True if this game is over, false if not
     */
    val isOver: Boolean

    /**
     * Gets a copy of the player by index.
     *
     * @param index index of the player
     * @requires `index > 0 && index < this.getNumberOfPlayers - 1`
     * @return Player object, null if no player with this index was found
     */
    fun getPlayer(index: Int): Player?

    /**
     * Gets a copy of the player by id.
     *
     * @param id id of the player
     * @return Player object, null if no player with this id was found
     */
    fun getPlayer(id: String?): Player?


    /**
     * Returns the index of the player.
     *
     * @param id player identification
     * @return index of the player, -1 if there's no player with the given id
     */
    fun getIndex(id: String): Int


    /**
     * Checks if it's valid to play the given `card`.
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