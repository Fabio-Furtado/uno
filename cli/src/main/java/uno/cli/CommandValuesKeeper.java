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

package uno.cli;

import uno.engine.CardColour;

import java.util.HashMap;
import java.util.Map;


/**
 * Contains string values associated with cli commands and colours. This strings
 * is what the cli user will enter to give a command
 *
 * @author Fábio Furtado
 */
final class CommandValuesKeeper {

    /**
     * Values for commands
     */
    private static final Map<Command, String> commands = new HashMap<>();

    /**
     * These commands are special because they can be converted to engine commands
     */
    private static final Map<Command, String> engineCommands = new HashMap<>();

    /**
     * Values for colours
     */
    private static final Map<CardColour, String> colours = new HashMap<>();

    static {
        commands.put(Command.PRINT_HAND, "p");
        commands.put(Command.PRINT_RIVALS_HAND_LENGTH, "pl");
        commands.put(Command.HELP, "help");
        commands.put(Command.EXIT, "exit");
        commands.put(Command.RESTART, "restart");
        engineCommands.put(Command.DRAW, "d");
        engineCommands.put(Command.PLAY_A_CARD, "ph");

        colours.put(CardColour.BLUE, "blue");
        colours.put(CardColour.GREEN, "green");
        colours.put(CardColour.RED, "red");
        colours.put(CardColour.YELLOW, "yellow");
    }

    /**
     * Checks if the given command is marked as convertible to an engine command.
     *
     * @param command command to check
     * @return true if is convertible, false if not or maybe not a command
     */
    static boolean isEngineConvertible(String command) {
        return engineCommands.containsValue(command);
    }

    /**
     * Checks if the given command is marked as non-convertible to an engine
     * command.
     *
     * @param command command to check
     * @return true if is a non-convertible command, false if not or maybe not
     *         a command
     */
    static boolean isCliExclusive(String command) {
        return commands.containsValue(command);
    }

    /**
     * Gets the String associated with this command
     *
     * @param command command to look for
     * @return value associated, null if no value is found
     */
    static String getValue(Command command) {
        if (commands.containsKey(command))
            return commands.get(command);
        else
            return engineCommands.get(command);
    }

    /**
     * Gets the String associated with this colour
     *
     * @param cardColour colour to look for
     * @return value associated, null if no value is found
     */
    static String getValue(CardColour cardColour) {
        return colours.get(cardColour);
    }
}
