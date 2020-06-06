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

import org.uno.engine.objects.BotPlayer;
import org.uno.engine.objects.HumanPlayer;
import org.uno.engine.objects.Player;
import org.uno.exceptions.GameRulesException;


/**
 * Creates new instances of the game class
 *
 * @author Fábio Furtado
 */
public final class GameFactory {

    /**
     * All bots name's will start with this prefix.
     */
    private static final String BOT_PLAYER_NAME_PREFIX = "Bot";

    /**
     * Min number of players for each game.
     */
    private static final int MIN_NUMBER_OF_PLAYERS = 2;

    /**
     * Max number of players for each game.
     */
    private static final int MAX_NUMBER_OF_PLAYERS = 8;

     /**
     * Returns the minumum number of players allowed for each game.
     * 
     * @return min number of players
     */
    public static int getMinNumberOfPlayers() {
        return MIN_NUMBER_OF_PLAYERS;
    }

    /**
     * Returns the max number of players allowed for each game.
     * 
     * @return max number of players
     */
    public static int getMaxNumberOfPlayers() {
        return MAX_NUMBER_OF_PLAYERS;
    }

    /**
     * Creates a new Game with the given players.
     *
     * @param numberOfBots      number of bot's to add
     * @param humanPlayersNames array with the name of each human player
     * @return a new {@code Game} instance
     */
    public Game newGame(int numberOfBots, String... humanPlayersNames)
            throws GameRulesException {
        Player[] players = new Player[numberOfBots + humanPlayersNames.length];

        checkIfNumberOfPlayersIsLegal(players.length);
        for (int i = 0; i < numberOfBots; i++)
            players[i] = new BotPlayer(String.format("%s%d", BOT_PLAYER_NAME_PREFIX, i));
        int j = 0;
        for (int i = numberOfBots; i < players.length; i++)
            players[i] = new HumanPlayer(humanPlayersNames[j++]);
        return new Game(players);
    }

    /**
     * Checks if the given number of players is legal, throwing an Exception case
     * not.
     *
     * @param numberOfPlayers number of players to test
     * @throws GameRulesException if the given number of players is illegal
     */
    private void checkIfNumberOfPlayersIsLegal(int numberOfPlayers)
            throws GameRulesException {
        String exceptionMessage = null;

        if (numberOfPlayers > MAX_NUMBER_OF_PLAYERS)
            exceptionMessage = String.format(
                    "%d players exceed the players limit of %d",
                    numberOfPlayers, MAX_NUMBER_OF_PLAYERS
            );
        else if (numberOfPlayers < MIN_NUMBER_OF_PLAYERS) {
            exceptionMessage = String.format(
                    "%d players is not enough to start a game, minimum of %d is needed",
                    numberOfPlayers, MIN_NUMBER_OF_PLAYERS
            );
        }
        if (exceptionMessage != null)
            throw new GameRulesException(exceptionMessage);
    }
}
