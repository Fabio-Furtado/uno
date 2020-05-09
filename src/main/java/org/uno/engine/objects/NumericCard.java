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

package org.uno.engine.objects;

import org.uno.enums.CardColour;
import org.uno.enums.CardType;


/**
 * These cards have a number between 0 and 9 associated with them
 */
public class NumericCard extends Card {

    private CardColour colour;
    private int number;

    public NumericCard(CardColour colour, int number) {
        this.type = CardType.NUMERIC;
        this.colour = colour;
        this.number = number;
    }

    public CardType getType() {
        return this.type;
    }

    public CardColour getColour() {
        return colour;
    }

    public int getNumber() {
        return number;
    }

    /**
     * Checks the equality between this card and the given one.
     *
     * @param other other object to compare.
     * @return true if the cards are equal, false if not
     */
    public boolean equals(NumericCard other) {
        return
                this.type == other.getType() &&
                        this.colour == other.getColour() &&
                        this.number == other.getNumber();
    }
}
