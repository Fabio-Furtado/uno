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

/**
 * A uno game abstraction. Use the {@link GameFactory} class the get instances.
 *
 * @author Fábio Furtado
 */
public interface UnoGame {

    /**
     * Gets the number of players of this game.
     *
     * @return number of players
     */
    int getNumberOfPlayers();

    /**
     * Gets a copy of the player by index.
     *
     * @param index index of the player
     * @requires {@code index > 0 && index < this.getNumberOfPlayers - 1}
     * @return Player object, null if no player with this index was found
     */
    Player getPlayer(int index);

    /**
     * Gets a copy of the player by id.
     *
     * @param id id of the player
     * @return Player object, null if no player with this id was found
     */
    Player getPlayer(String id);

    /**
     * Gets an array with a copy of all the players in this game.
     *
     * @return all players in this game
     */
    Player[] getPlayers();

    /**
     * Returns the index of the player.
     *
     * @param id player identification
     * @return index of the player, -1 if there's no player with the given id
     */
    int getIndex(String id);

    /**
     * Gets a copy of the player in turn.
     *
     * @return Player class with the abstraction of the player in turn
     */
    Player getPlayerInTurn();

    /**
     * Gets a copy of the player which played previously. Notice that this does
     * not return the player at index {@code turn - 1}, but the player which
     * indeed made the last move.
     *
     * @return Player class with the abstraction of the previous player.
     */
    Player getPreviousPlayer();

    /**
     * Gets a copy of the card on the top of the deck.
     *
     * @return copy of the card on the the top of the deck
     */
    Card getDeckTop();

    /**
     * Gets a copy of the card at the top of the table.
     *
     * @return copy of the card on the the top of the table
     */
    Card getTableTop();

    /**
     * Gets a copy of this game's winner.
     *
     * @return player which won the game
     * @throws IllegalStateException if {@code !this.isOver()}
     */
    Player getWinner();

    /**
     * Checks if the game is over.
     *
     * @return true if the game is over, false if not
     */
    boolean isOver();

    /**
     * Checks if it's valid to play the given card taking into account what's on
     * top of the table.
     *
     * @param card card to check validity
     * @return true if the card is valid, false if not
     */
    boolean isCardValid(Card card);

    /**
     * Executes the given command for the player currently in turn.
     *
     * @param command command of the move
     * @return 0 if the play was successfully executed, 1 if the move is invalid
     * @throws InvalidOptionException            if the command option given is
     *                                           not valid
     * @throws CardIndexOutOfHandBoundsException if the index of the card chosen
     *                                           in the command is not valid
     * @throws MissingColourForWildCardException if no colour was specified when
     *                                           trying to play a wild card
     * @throws IllegalStateException             if the game is already over
     */
    int executeMove(GameCommand command)
            throws InvalidOptionException,
            CardIndexOutOfHandBoundsException,
            MissingColourForWildCardException;

    /**
     * If the player in turn is a bot, it will make it's move and it will be
     * executed.
     *
     * @return command with the bot's move.
     */
    GameCommand goBot();

    /**
     * Returns a copy of this instance.
     * 
     * @return copy of this game
     */
    UnoGame clone();
}
