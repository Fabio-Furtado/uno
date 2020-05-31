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
import org.uno.enums.WildCardSymbol;


/**
 * Immutable abstraction of a wild card
 *
 * @author Fábio Furtado
 */
public final class WildCard implements Card, Wild, Symbolic {

    private final CardType type;
    private final WildCardSymbol symbol;
    private CardColour pickedColour;

    public WildCard(WildCardSymbol symbol) {
        this.type = CardType.WILD;
        this.symbol = symbol;
        this.pickedColour = null;
    }

    /**
     * @see Card#getType()
     */
    @Override
    public CardType getType() {
        return this.type;
    }

    /**
     * @see Wild#setPickedColour(CardColour)
     */
    @Override
    public void setPickedColour(CardColour colour) {
        this.pickedColour = colour;
    }

    /**
     * @see Wild#getPickedColour()
     */
    @Override
    public CardColour getPickedColour() {
        return pickedColour;
    }

    /**
     * @see Symbolic#getSymbol()
     */
    @Override
    public CardSymbol getSymbol() {
        return this.symbol;
    }

    /**
     * To be equal to this wild card, the other card needs to also be wild
     * and to have the same symbol or to be the exact same object.
     *
     * @see Card#equals(Card)
     */
    @Override
    public boolean equals(Card other) {
        if (this == other)
            return true;
        if (this.getType() != other.getType())
            return false;
        Symbolic symbolicOther = (Symbolic) other;
        return this.symbol == symbolicOther.getSymbol();
    }

    /**
     * @see Card#clone()
     */
    @Override
    public Card clone() {
        return new WildCard((WildCardSymbol) this.getSymbol());
    }
}