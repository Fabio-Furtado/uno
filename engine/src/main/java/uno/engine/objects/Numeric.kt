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

/**
 * This interface must be applied to all cards which hold a numeric value.
 *
 * @author Fábio Furtado
 */
interface Numeric {

    /**
     * Returns the card's number.
     *
     * @return number of the card
     */
    val number: Int

    companion object {
        /**
         * The minimal numeric value a card can hold.
         */
        const val MIN_VALUE = 0

        /**
         * The maximal numeric value a card can hold.
         */
        const val MAX_VALUE = 9
    }
}