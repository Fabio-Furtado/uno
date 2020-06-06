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

package org.uno.cli;

import java.util.Scanner;


/**
 * Implemented in all classes which would need to read from {@code System.in}
 * so that they can share the same Scanner.
 * 
 * @author Fábio Furtado
 */
interface CommandLineReader {

    /**
     * Scanner reading to {@code System.in} which should be shared by all
     * classes reading which must read user imput by that source.
     */
    Scanner reader = new Scanner(System.in);
}
