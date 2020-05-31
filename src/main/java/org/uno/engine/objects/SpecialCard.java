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
import org.uno.enums.CardSymbol;
import org.uno.enums.CardType;
import org.uno.enums.SpecialCardSymbol;


/**
 * Immutable abstraction of a special card
 *
 * @author Fábio Furtado
 */
public final class SpecialCard implements Card, Colourful, Symbolic {

    private final CardColour colour;
    private final SpecialCardSymbol symbol;
    private final CardType type;

    /**
     * Creates a new instance.
     *
     * @param colour colour for this card
     * @param symbol symbol for this card
     */
    public SpecialCard(CardColour colour, SpecialCardSymbol symbol) {
        this.type = CardType.SPECIAL;
        this.colour = colour;
        this.symbol = symbol;
    }

    /**
     * @see Card#getType()
     */
    @Override
    public CardType getType() {
        return type;
    }

    /**
     * @see Colourful#getColour()
     */
    public CardColour getColour() {
        return colour;
    }

    /**
     * @see Symbolic#getSymbol()
     */
    @Override
    public CardSymbol getSymbol() {
        return symbol;
    }

    /**
     * To be equal to this special card, the other card needs to also be special
     * and to have the same symbol and colour or to be the exact same object.
     *
     * @see Card#equals(Card)
     */
    @Override
    public boolean equals(Card other) {
        if (this == other)
            return true;
        if (this.getType() != other.getType())
            return false;
        SpecialCard specialOther = (SpecialCard) other;
        return
            this.colour == specialOther.getColour() &&
            this.symbol == specialOther.getSymbol();
    }

    /**
     * @see Card#clone()
     */
    @Override
    public Card clone() {
        return new SpecialCard(this.colour, this.symbol);
    }
}
