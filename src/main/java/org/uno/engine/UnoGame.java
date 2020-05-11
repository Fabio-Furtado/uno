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

package org.uno.engine;

import org.uno.engine.objects.Card;
import org.uno.engine.objects.Player;
import org.uno.exceptions.engineExceptions.CardIndexOutOfHandBoundsException;
import org.uno.exceptions.engineExceptions.InvalidOptionException;
import org.uno.exceptions.engineExceptions.MissingColourForWildCardException;


interface UnoGame {

    /**
     * Gets the number of players of this game.
     *
     * @return number of players
     */
    int getNumberOfPlayers();

    /**
     * Gets the specified player.
     *
     * @param id id of the player
     * @return Player object, null if no player with this id was found
     */
    Player getPlayer(int id);

    /**
     * Gets the player in turn.
     *
     * @return Player class with the abstraction of the player in turn
     */
    Player getPlayerInTurn();

    /**
     * Gets previous player.
     *
     * @return Player class with the abstraction of the previous player.
     */
    Player getPreviousPlayer();

    /**
     * Gets the card on the top of the deck.
     *
     * @return card on the the top of the deck
     */
    Card getDeckTop();

    /**
     * Gets the card at the top of the table.
     *
     * @return card on the the top of the table
     */
    Card getTableTop();

    /**
     * Gets this game's winner.
     *
     * @return player which won the game
     * @throws IllegalStateException if this game is not over yet
     */
    Player getWinner();

    /**
     * Checks if the game is over.
     *
     * @return true if the game is over, false if not
     */
    boolean isOver();

    /**
     * Executes the given command for the player currently in turn.
     *
     * @param command command of the move
     * @return 0 if the play was successfully executed, 1 if the move is invalid
     * @throws InvalidOptionException if the command option given is not valid
     * @throws CardIndexOutOfHandBoundsException if the index of the card chosen
     * in the command is not valid
     * @throws MissingColourForWildCardException
     */
    int executeMove(GameCommand command)
            throws InvalidOptionException, CardIndexOutOfHandBoundsException,
            MissingColourForWildCardException;
}
