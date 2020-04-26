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

import org.uno.enums.CardColour;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Fábio Furtado
 */
class CommandValuesKeeper {

  private static final Map<Command, String> commands = new HashMap<>();
  private static final Map<CardColour, String> colours = new HashMap<>();

  static {
    commands.put(Command.DRAW, "d");
    commands.put(Command.PLAY_A_CARD, "ph");
    commands.put(Command.PRINT_HAND, "p");
    commands.put(Command.HELP, "help");
    commands.put(Command.EXIT, "exit");

    colours.put(CardColour.BLUE, "blue");
    colours.put(CardColour.GREEN, "green");
    colours.put(CardColour.RED, "red");
    colours.put(CardColour.YELLOW, "yellow");
  }

  static String getValue(Command command) {
    return commands.get(command);
  }

  static String getValue(CardColour cardColour) {
    return colours.get(cardColour);
  }
}
