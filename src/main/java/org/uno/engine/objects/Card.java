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

import org.uno.enums.CardType;


/**
 * All cards must implement this interface
 *
 * @author Fábio Furtado
 */
public interface Card {

    /**
     * Returns the type of the card.
     *
     * @return type of the card
     */
    CardType getType();

    /**
     * Checks the equality between this card and the given one.
     *
     * @param other other object to compare.
     * @return true if the cards are equal, false if not
     */
    boolean equals(Card other);

    /**
     * Returns a clone of tis card.
     */
    Card clone();
}
