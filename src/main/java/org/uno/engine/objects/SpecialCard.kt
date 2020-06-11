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
package org.uno.engine.objects

import org.uno.enums.CardColour
import org.uno.enums.CardType
import org.uno.enums.SpecialCardSymbol

/**
 * Immutable abstraction of a special card.
 *
 * @author Fábio Furtado
 */
class SpecialCard(_colour: CardColour, _symbol: SpecialCardSymbol): Card, Colourful, Symbolic {

    /**
     * @see Card.type
     */
    override val type: CardType = CardType.SPECIAL

    /**
     * @see Colourful.colour
     */
    override val colour: CardColour = _colour

    /**
     * @see Symbolic.symbol
     */
    override val symbol: SpecialCardSymbol = _symbol

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other == null || this.javaClass.kotlin != other.javaClass.kotlin -> false
            this.colour == (other as SpecialCard).colour && this.symbol == other.symbol -> true
            else -> false
        }
    }

    /**
     * @see Card.clone
     */
    override fun clone(): Card {
        return SpecialCard(colour, symbol)
    }
}