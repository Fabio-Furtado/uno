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
import org.uno.enums.CardTypes;
import org.uno.enums.SpecialCardSymbols;


public class SpecialCard extends Card {

    private CardColour colour;
    private SpecialCardSymbols symbol;

    public SpecialCard(CardColour colour, SpecialCardSymbols symbol) {
        this.type = CardTypes.SPECIAL;
        this.colour = colour;
        this.symbol = symbol;
    }

    @Override
    public CardTypes getType() {
        return this.type;
    }

    /**
     * Retrns the colour of the card.
     *
     * @return colour of thte card
     */
    public CardColour getColour() {
        return this.colour;
    }

    /**
     * Returns the symbols of the card.
     *
     * @return symbols of the card
     */
    public SpecialCardSymbols getSymbol() {
        return this.symbol;
    }

    /**
     * Checks the equality between this card and the given one.
     *
     * @param other other object to compare.
     * @return true if the cards are equal, false if not
     */
    public boolean equals(SpecialCard other) {
        return
                this.type == other.getType() &&
                        this.colour == other.getColour() &&
                        this.symbol == other.getSymbol();
    }
}
