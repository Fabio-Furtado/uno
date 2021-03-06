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
import uno.engine.WildCardSymbol
import java.util.Optional

/**
 * Immutable abstraction of a wild card which implements internment.
 *
 * @author Fábio Furtado
 */
class WildCard private constructor(

    override val symbol: WildCardSymbol,
    override val colour: Optional<CardColour>
    ) : Card, Symbolic, Wild {


    /**
     * @see Card.type
     */
    override val type:  CardType = CardType.WILD

    private val hash = calcHash()


    companion object {

        private val pool = HashMap<WildCard, WildCard>()

        /**
         * Returns an instance with the given `symbol` and whose `::colour`
         * will be and empty Optional
         */
        @JvmStatic
        fun of(_symbol: WildCardSymbol): WildCard {
            val candidate = WildCard(_symbol, Optional.empty())
            return if (pool.containsKey(candidate)) pool[candidate]!!
            else {
                pool[candidate] = candidate
                candidate
            }
        }

        /**
         * Returns an instance with the given `symbol` and `colour`
         */
        @JvmStatic
        fun of(_symbol: WildCardSymbol, _colour: CardColour): WildCard {
            val candidate = WildCard(_symbol, Optional.of(_colour))
            return if (pool.containsKey(candidate)) pool[candidate]!!
            else {
                pool[candidate] = candidate
                candidate
            }
        }
    }

    /**
     * Returns an instance similar to this one but with the given colour
     */
    fun withColour(_colour: CardColour) = of(symbol, _colour)

    fun withSymbol(_symbol: WildCardSymbol) =
        if (colour.isPresent)
            of(_symbol, colour.get())
        else of(_symbol) // with empty colour

    /**
     * Checks if this object is equal `other`
     *
     * As this object is immutable and implements internment this only
     * equals `other` if they're actually the same reference.
     */
    override fun equals(other: Any?) = this === other

    /**
     * @see Object.hashCode
     */
    private fun calcHash(): Int {
        var hash = 586742
        hash *= if (symbol == WildCardSymbol.CHANGE_COLOUR) 2 else 3
        hash += when {
            colour.isPresent -> {
                when {
                    colour.get() == CardColour.BLUE -> 1
                    colour.get() == CardColour.GREEN -> 2
                    colour.get() == CardColour.RED -> 3
                    colour.get() == CardColour.YELLOW -> 4
                    else -> 5
                }
            }
            else -> 6
        }
        return hash
    }

    override fun hashCode() = hash

    /**
     * @see Object.toString
     */
    override fun toString() = if (colour.isPresent) "${colour.get()} Wild $symbol"
                              else "Wild $symbol"
}