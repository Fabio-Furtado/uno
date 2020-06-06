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

package org.uno.cli;

import org.uno.engine.GameCommand;
import org.uno.enums.CardColour;
import org.uno.exceptions.CommandFormatException;


/**
 * Translates cli commands into engine commands.
 *
 * @author Fábio Furtado
 */
class CLItoEngineCommandConverter {

    private static final String UNKNOWN_COMMAND_ERROR_MESSAGE =
            "Unknown command: %s";
    private static final String NOT_INDEX_ERROR_MESSAGE =
            "Expected an index after the <%s> command";
    private static final String INSUFFICIENT_ARGUMENTS_ERROR_MESSAGE =
            "Not enough arguments for the <%s> command";
    private static final String TOO_MANY_ARGUMENTS_ERROR_MESSAGE =
            "Too many arguments for the <%s> command";

    private CLItoEngineCommandConverter() {
    }

    public static String getUnknownCommandErrorMessage() {
        return UNKNOWN_COMMAND_ERROR_MESSAGE;
    }

    public static String getNotIndexErrorMessage() {
        return NOT_INDEX_ERROR_MESSAGE;
    }

    public static String getInsufficientArgumentsErrorMessage() {
        return INSUFFICIENT_ARGUMENTS_ERROR_MESSAGE;
    }

    public static String getTooManyArgumentsErrorMessage() {
        return TOO_MANY_ARGUMENTS_ERROR_MESSAGE;
    }

    static GameCommand convert(String[] cliCommand) throws CommandFormatException {
        if (cliCommand[0].equals(CommandValuesKeeper.getValue(Command.DRAW))) {
            if (cliCommand.length > 1)
                throw new CommandFormatException(
                        String.format(TOO_MANY_ARGUMENTS_ERROR_MESSAGE, cliCommand[0])
                );
            return new GameCommand();
        }
        else {
            return doIfLikelyPlayingFromHand(cliCommand);
        }
    }

    private static GameCommand doIfLikelyPlayingFromHand(String[] cliCommand)
            throws CommandFormatException {
        int index;
        CardColour colour = null;
        if (cliCommand[0].equals(CommandValuesKeeper.getValue(Command.PLAY_A_CARD))) {
            try {
                index = (Integer.parseInt(cliCommand[1]) - 1);
            } catch (NumberFormatException e) {
                throw new CommandFormatException(
                        String.format(NOT_INDEX_ERROR_MESSAGE,
                                cliCommand[0])
                );
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new CommandFormatException(String.format(
                        INSUFFICIENT_ARGUMENTS_ERROR_MESSAGE, cliCommand[0])
                );
            }
        } else {
            throw new CommandFormatException(String.format(
                    UNKNOWN_COMMAND_ERROR_MESSAGE, cliCommand[0])
            );
        }
        if (cliCommand.length == 3) {
            CardColour engineCardColourCode = null;
            if (cliCommand[2].equals(CommandValuesKeeper.getValue(CardColour.BLUE)))
                engineCardColourCode = CardColour.BLUE;
            else if (cliCommand[2].equals(CommandValuesKeeper.getValue(CardColour.RED)))
                engineCardColourCode = CardColour.RED;
            else if (cliCommand[2].equals(CommandValuesKeeper.getValue(CardColour.GREEN)))
                engineCardColourCode = CardColour.GREEN;
            else if (cliCommand[2].equals(CommandValuesKeeper.getValue(CardColour.YELLOW)))
                engineCardColourCode = CardColour.YELLOW;

            if (engineCardColourCode != null)
                colour = engineCardColourCode;
        } else if (cliCommand.length > 3)
            throw new CommandFormatException(String.format(
                    TOO_MANY_ARGUMENTS_ERROR_MESSAGE, cliCommand[0])
            );
        return new GameCommand(index, colour);
    }
}
