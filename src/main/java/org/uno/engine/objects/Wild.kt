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

/**
 * Wild cards must implement this interface.
 *
 * @author Fábio Furtado
 */
interface Wild {
    /**
     * Returns the colour picked for the wild card.
     *
     * @return colour which was picked for this wild card, null if
     * no colour has been picked yet
     */
    /**
     * Sets a picked colour for the wild card
     *
     * @param colour picked colour to set for the wild card
     */
    var pickedColour: CardColour?

}