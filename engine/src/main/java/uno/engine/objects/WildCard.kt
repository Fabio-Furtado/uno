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
    val colour: Optional<CardColour>

    ) : Card, Symbolic {


    /**
     * @see Card.type
     */
    override val type:  CardType = CardType.WILD


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
     * This object equals another object if the other object is a `WildCard`
     * with the same symbol and colour as this one
     */
    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other == null || this.javaClass.kotlin != other.javaClass.kotlin -> false
            this.symbol == (other as WildCard).symbol -> true
            this.colour.isEmpty -> other.colour.isEmpty
            this.colour.isPresent -> this.colour.get() == other.colour.get()
            else -> false
        }
    }

    override fun hashCode(): Int {
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

    override fun toString(): String = if (colour.isPresent) "${colour.get()} Wild $symbol" else "Wild $symbol"
}