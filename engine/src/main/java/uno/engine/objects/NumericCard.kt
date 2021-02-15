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

/**
 * Immutable abstraction of a numeric card which implements internment.
 *
 * @author Fábio Furtado
 */
class NumericCard private constructor(_colour: CardColour, _number: Int) :
                                                      Card, Numeric, Colourful {

    companion object {
        private val pool = HashMap<NumericCard, NumericCard>()

        /**
         * Returns an instance
         *
         * @throws IllegalArgumentException if the number is not valid. A valid
         * one must be no shorter than [Numeric.MIN_VALUE] and no
         * longer than [Numeric.MAX_VALUE].
         */
        @JvmStatic
        fun of(_colour: CardColour, _number: Int): NumericCard {

            // validate the number
            if (_number < Numeric.MIN_VALUE || _number > Numeric.MAX_VALUE)
                throw IllegalArgumentException("$_number is not a valid number." +
                        "A valid one must be no shorter than ${Numeric.MIN_VALUE}" +
                        "and no longer than ${Numeric.MAX_VALUE}")

            val candidate = NumericCard(_colour, _number)
            return if (pool.containsKey(candidate)) pool[candidate]!!
            else {
                pool[candidate] = candidate
                candidate
            }
        }
    }

    /**
     * @see Card.type
     */
    override val type = CardType.NUMERIC

    /**
     * @see Colourful.colour
     */
    override val colour = _colour

    /**
     * @see Numeric.number
     */
    override val number = _number

    /**
     * Returns an instance with identical characteristics to this one except
     * for the new number.
     */
    fun withNumber(_number: Int) = of(colour, _number)


    /**
     * Returns an instance with identical characteristics to this one except
     * for the new colour.
     */
    fun withColour(_colour: CardColour) = of(_colour, number)

    /**
     * Checks if this object is equal `other`
     *
     * As this object is immutable and implements internment this only
     * equals `other` if they're actually the same reference.
     */
    override fun equals(other: Any?) = this === other

    override fun hashCode(): Int {
        var hash = 586740
        hash *= when (colour) {
            CardColour.BLUE -> 2
            CardColour.RED -> 3
            CardColour.GREEN -> 4
            CardColour.YELLOW -> 5
        }
        hash *= number + 1
        return hash
    }

    override fun toString(): String = "$colour $number"
}