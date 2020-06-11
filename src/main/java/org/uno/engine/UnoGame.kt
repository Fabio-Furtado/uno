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

import org.uno.engine.objects.Card
import org.uno.engine.objects.Player
import org.uno.engine.engineExceptions.CardIndexOutOfHandBoundsException
import org.uno.engine.engineExceptions.InvalidOptionException
import org.uno.engine.engineExceptions.MissingColourForWildCardException

/**
 * A uno game abstraction. Use the [Game.Factory] class the get instances.
 *
 * @author Fábio Furtado
 */
interface UnoGame {

    /**
     * Gets an array with a copy of all the players in this game.
     *
     * @return all players in this game
     */
    val players: Array<Player>

    /**
     * Gets a copy of the player in turn.
     *
     * @return Player class with the abstraction of the player in turn
     */
    val playerInTurn: Player

    /**
     * Gets a copy of the player which played previously. Notice that this method
     * does not return the player at index `turn - 1`, but the player which
     * indeed made the last move.
     * if called when the game just started and no moves have been made, the
     * return type is meaningless.
     *
     * @return Player class with the abstraction of the previous player.
     */
    val previousPlayer: Player

    /**
     * Gets a copy of this game's winner.
     *
     * @return player which won the game or null if the game is not over yet
     */
    val winner: Player?

    /**
     * Gets a copy of the card on the top of the deck.
     *
     * @return copy of the card on the the top of the deck
     */
    fun getDeckTop(): Card

    /**
     * Gets a copy of the card at the top of the table.
     *
     * @return copy of the card on the the top of the table
     */
    fun getTableTop(): Card

    /**
     * Gets the number of players of this game.
     *
     * @return number of players
     */
    fun getNumberOfPlayers(): Int

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
     * Checks if the game is over.
     *
     * @return true if the game is over, false if not
     */
    fun isOver(): Boolean


    /**
     * Checks if it's valid to play the given card taking into account what's on
     * top of the table.
     *
     * @param card card to check validity
     * @return true if the card is valid, false if not
     */
    fun isCardValid(card: Card): Boolean

    /**
     * Executes the given command for the player currently in turn.
     *
     * @param command command of the move
     * @return 0 if the play was successfully executed, 1 if the move is invalid
     * @throws InvalidOptionException            if the command option given is
     * not valid
     * @throws CardIndexOutOfHandBoundsException if the index of the card chosen
     * in the command is not valid
     * @throws MissingColourForWildCardException if no colour was specified when
     * trying to play a wild card
     * @throws IllegalStateException             if the game is already over
     */
    @Throws(InvalidOptionException::class,
            CardIndexOutOfHandBoundsException::class,
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