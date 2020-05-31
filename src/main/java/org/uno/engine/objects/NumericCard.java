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
 * Immutable abstraction of a numeric card
 *
 * @author Fábio Furtado
 */
public final class NumericCard implements Card, Numeric, Colourful {

    private final CardColour colour;
    private final int number;
    private final CardType type;

    /**
     * Creates a new instance.
     *
     * @param colour colour of the card
     * @param number number the card will hold
     */
    public NumericCard(CardColour colour, int number) {
        this.type = CardType.NUMERIC;
        this.colour = colour;
        this.number = number;
    }

    /**
     * @see Card#getType()
     */
    public CardType getType() {
        return this.type;
    }

    /**
     * @see Colourful#getColour()
     */
    @Override
    public CardColour getColour() {
        return colour;
    }

    /**
     * @see Numeric#getNumber()
     */
    @Override
    public int getNumber() {
        return number;
    }

    /**
     * To be equal to this numeric card, the other card needs to also be numeric,
     * to have the same colour and number or to be a reference to this exact same
     * object.
     * @see Card#equals(Card)
     */
    @Override
    public boolean equals(Card other) {
        if (this == other)
            return true;
        if (this.getType() != other.getType())
            return false;
        NumericCard numOther = (NumericCard) other;
        return
            this.colour == numOther.getColour() &&
            this.number == numOther.getNumber();
    }

    /**
     * @see Card#clone()
     */
    @Override
    public Card clone() {
        return new NumericCard(this.getColour(), this.getNumber());
    }
}
