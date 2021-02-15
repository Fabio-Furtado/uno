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
package uno.engine.objects

import uno.engine.CardColour
import uno.engine.CardType
import uno.engine.SpecialCardSymbol

/**
 * Immutable abstraction of a special card which implements internment.
 *
 * @author Fábio Furtado
 */
class SpecialCard  private constructor(_colour: CardColour, _symbol: SpecialCardSymbol):
                                                     Card, Colourful, Symbolic {

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

    private val hash = calcHash()

    companion object {
        private val pool = HashMap<SpecialCard, SpecialCard>()

        /**
         * Creates a new instance.
         */
        @JvmStatic
        fun of(_colour: CardColour, _symbol: SpecialCardSymbol): SpecialCard {
            val candidate = SpecialCard(_colour, _symbol)
            return if (pool.containsKey(candidate)) pool[candidate]!!
            else {
                pool[candidate] = candidate
                candidate
            }
        }
    }

    /**
     * Returns a instance with identical characteristics to this one except for
     * the new colour.
     */
    fun withColour(_colour: CardColour) = of(_colour, symbol)

    /**
     * Returns a instance with identical characteristics to this one except for
     * the new symbol.
     */
    fun withSymbol(_symbol: SpecialCardSymbol) = of(colour, _symbol)

    /**
     * Checks if this object is equal `other`
     *
     * As this object is immutable and implements internment this only
     * equals `other` if they're actually the same reference.
     */
    override fun equals(other: Any?) = this === other

    override fun hashCode() = hash

    private fun calcHash(): Int {
        var hash = 586741
        hash *= when (colour) {
            CardColour.BLUE -> 1
            CardColour.RED -> 2
            CardColour.GREEN -> 3
            CardColour.YELLOW -> 4
        }
        hash *= when (symbol) {
            SpecialCardSymbol.REVERSE -> 2
            SpecialCardSymbol.SKIP -> 3
            SpecialCardSymbol.DRAW_2 -> 4
        }
        return hash
    }

    override fun toString(): String = "$colour $symbol"
}