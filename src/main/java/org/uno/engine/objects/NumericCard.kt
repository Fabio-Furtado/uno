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
import kotlin.IllegalArgumentException

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
         * Creates a new instance
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
     * To be equal to this numeric card, the other card needs to also be numeric,
     * to have the same colour and number or to be a reference to this exact same
     * object.
     * @see Card.equals
     */
    fun equals(other: Card): Boolean {
        if (this == other) return true
        if (type != other.type) return false
        val numOther = other as NumericCard
        return colour == numOther.colour &&
                number == numOther.number
    }

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other == null || this.javaClass.kotlin != other.javaClass.kotlin -> false
            this.colour == (other as NumericCard).colour && this.number == other.number -> true
            else -> false
        }
    }
}