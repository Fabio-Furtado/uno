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

import org.uno.enums.CardColour;

/**
 * @author Fábio Furtado
 */
public class GameCommand {
    
    private final int option;
    private final int index;
    private final CardColour colour;

    public GameCommand(int option, int index, CardColour colour) {
        this.option = option;
        this.index = index;
        this.colour = colour;
    }

    public GameCommand(int option) {
        this.option = option;
        this.index = -1;
        this.colour = null;
    }

    public GameCommand(int option, int index) {
        this.option = option;
        this.index = index;
        this.colour = null;
    }

    public int getOption() {
        return option;
    }

    public int getIndex() {
        return index;
    }

    public CardColour getColour() {
        return colour;
    }
}
