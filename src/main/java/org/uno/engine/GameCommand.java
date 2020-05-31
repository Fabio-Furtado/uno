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
 * Abstraction of a command which can be passed to {@link UnoGame#executeMove(GameCommand)}
 * to be executed. There are constructors for all situations (ex: drawing, playing...)
 *
 * @author Fábio Furtado
 */
public final class GameCommand {

    private final int option;
    private final int index;
    private final CardColour colour;

    /**
     * Creates a new instance for a command to play a wild card.
     *
     * @param index  index of the card to play
     * @param colour colour to choose for the wild card
     * @requires {@code colour != null && index > -1}
     */
    public GameCommand(int index, CardColour colour) {
        this.option = 1;
        this.index = index;
        this.colour = colour;
    }

    /**
     * Creates a new instance for a command to draw a card.
     */
    public GameCommand() {
        this.option = 0;
        this.index = -1;
        this.colour = null;
    }

    /**
     * Creates a new instance for a command to play a non wild from the given
     * index.
     *
     * @param index index of the card to play
     * @requires {@code index > -1}
     */
    public GameCommand(int index) {
        this.option = 1;
        this.index = index;
        this.colour = null;
    }

    /**
     * Gets the option of this command.
     *
     * @return 0 if the option is draw, 1 if it is to play
     */
    public int getOption() {
        return option;
    }

    /**
     * Gets the index of the card this command plays.
     *
     * @return index of the card this command plays, -1 this command is for
     * drawing
     */
    public int getIndex() {
        return index;
    }

    /**
     * Gets the colour chosen for the wild card this command wants to play.
     *
     * @return {@link CardColour} if this command plays a wild card, null if not
     */
    public CardColour getColour() {
        return colour;
    }
}
