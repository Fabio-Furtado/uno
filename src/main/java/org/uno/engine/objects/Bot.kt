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

import org.uno.engine.GameCommand
import org.uno.engine.UnoGame

/**
 * This interface must be applied to bot players
 *
 * @author Fábio Furtado
 */
interface Bot {

    /**
     * The bot will choose his next move based on the info it gets from the given
     * game class.
     *
     * @param game game class to which to bot will base it's decision on.
     * @return Command class with the bots decision
     */
    fun makeMove(game: UnoGame): GameCommand
}