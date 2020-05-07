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

    private static final String SEPARATOR_SYMBOL = " ";
    private static final int ENGINE_DRAW_COMMAND = 0;
    private static final int ENGINE_PlAY_COMMAND = 1;
    private static final String UNKNOWN_COMMAND_ERROR_MESSAGE =
        "Unknown command: %s";
    private static final String UNKNOWN_ARGUMENT_ERROR_MESSAGE =
        "Expected an index after the <%s> command";
    private static final String INSUFFICIENT_ARGUMENTS_ERROR_MESSAGE =
        "Not enough arguments for the <%s> command";
    private static final String TOO_MANY_ARGUMENTS_EXCEPTION =
        "Too many arguments for the <%s> command";

    private CLItoEngineCommandConverter() {
    }

    static GameCommand convert(String cliCommand) throws CommandFormatException {
        int option;
        int index;
        CardColour colour;

        if (cliCommand.equals(CommandValuesKeeper.getValue(Command.DRAW)))
            return new GameCommand(ENGINE_DRAW_COMMAND);
        else {
            return doIfLikelyPlayingFromHand(cliCommand);
        }
    }

    private static GameCommand doIfLikelyPlayingFromHand(String cliCommand)
        throws CommandFormatException {
        int option;
        int index;
        CardColour colour = null;
        String[] commandElements = cliCommand.split(SEPARATOR_SYMBOL);

        if (commandElements[0].equals(CommandValuesKeeper.getValue(Command.PLAY_A_CARD))) {
            option = ENGINE_PlAY_COMMAND;
            try {
                index = (Integer.parseInt(commandElements[1]) - 1);
            } catch (NumberFormatException e) {
                throw new CommandFormatException(
                    String.format(UNKNOWN_ARGUMENT_ERROR_MESSAGE,
                        commandElements[0])
                );
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new CommandFormatException(String.format(
                    INSUFFICIENT_ARGUMENTS_ERROR_MESSAGE, commandElements[0])
                );
            }
        } else {
            throw new CommandFormatException(String.format(
                UNKNOWN_COMMAND_ERROR_MESSAGE, commandElements[0])
            );
        }
        if (commandElements.length == 3) {
            CardColour engineCardColourCode = null;
            if (commandElements[2].equals(CommandValuesKeeper.getValue(CardColour.BLUE)))
                engineCardColourCode = CardColour.BLUE;
            else if (commandElements[2].equals(CommandValuesKeeper.getValue(CardColour.RED)))
                engineCardColourCode = CardColour.RED;
            else if (commandElements[2].equals(CommandValuesKeeper.getValue(CardColour.GREEN)))
                engineCardColourCode = CardColour.GREEN;
            else if (commandElements[2].equals(CommandValuesKeeper.getValue(CardColour.YELLOW)))
                engineCardColourCode = CardColour.YELLOW;

            if (engineCardColourCode != null)
                colour = engineCardColourCode;
        } else if (commandElements.length > 3)
            throw new CommandFormatException(String.format(
                TOO_MANY_ARGUMENTS_EXCEPTION, commandElements[0])
            );
        return new GameCommand(option, index, colour);
    }
}
