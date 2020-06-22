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

import org.uno.engine.CardColour
import org.uno.engine.CardType
import org.uno.engine.WildCardSymbol

/**
 * Immutable abstraction of a wild card which implements internment.
 *
 * @author Fábio Furtado
 */
// Todo(implement internment)
class WildCard private constructor(_symbol: WildCardSymbol) : Card, Symbolic {

    /**
     * @see Card.type
     */
    override val type: CardType = CardType.WILD

    override val symbol: WildCardSymbol = _symbol

    companion object {
        private val pool = HashMap<WildCard, WildCard>()

        /**
         * Returns an instance
         */
        @JvmStatic
        fun of(_symbol: WildCardSymbol): WildCard {
            val candidate = WildCard(_symbol)
            return if (pool.containsKey(candidate)) pool[candidate]!!
            else {
                pool[candidate] = candidate
                candidate
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other == null || this.javaClass.kotlin != other.javaClass.kotlin -> false
            this.symbol == (other as WildCard).symbol -> true
            else -> false
        }
    }
}