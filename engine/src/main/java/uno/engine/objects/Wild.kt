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
import java.util.Optional

/**
 * This interface is used for wild cards.
 *
 * It should be used instead of `Colourful` for wild cards because these may
 * or not have a colour depending if they are in the stack, on the table or
 * in a player's hand.
 */
interface Wild {

    /**
     * An optional which will be empty if the card has no colour
     */
    val colour: Optional<CardColour>
}