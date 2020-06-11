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

package org.uno.engine


/**
 * Abstraction of a command which can be passed to {@link UnoGame#executeMove(GameCommand)}
 * to be executed. There are constructors for all situations (ex: drawing, playing...)
 *
 * @author Fábio Furtado
 */
class GameCommand private constructor(val option: Int, val index: Int,
                                      val colour: CardColour?) {

    /**
     * Creates a new instance for a command to draw a card.
     */
    constructor(): this(0, -1, null)

    /**
     * Creates a new instance for a command to play a non wild from the given
     * index.
     *
     * @param index index of the card to play
     */
    constructor(index: Int): this(1, index, null)

    /**
     * Creates a new instance for a command to play a wild card or a non-wild
     * if {@code colour == null}.
     *
     * @param index  index of the card to play
     * @param colour colour to choose for the wild card
     */
    constructor(index: Int, colour: CardColour?): this(1, index, colour)
}