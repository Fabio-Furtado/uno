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

import java.util.InputMismatchException;


/**
 * Contains some utility console functions.
 *
 * @author Fábio Furtado
 */
final class CliUtils implements CommandLineReader {

    /**
     * Not to be instantiated.
     */
    private CliUtils() {}

    /**
     * Makes sure the user inputs a value within the given range.
     *
     * @param beginning beginning of the range
     * @param end       end of the range
     * @return valid value imputed by user
     */
    static int readValueInRange(int beginning, int end) {
        int choice = 0;
        do {
            try {
                choice = reader.nextInt();
                if (choice < beginning || choice > end)
                    System.out.printf(
                            "Invalid value, insert a number between %d and %d: ",
                            beginning, end);
            } catch (InputMismatchException e) {
                System.out.printf(
                        "Invalid input, insert a number between %d and %d: ",
                        beginning, end);
                readValueInRange(beginning, end);
            }
        } while (choice < beginning || choice > end);
        return choice;
    }

    /**
     * Reads a valid option from {@code System.in}.
     *
     * @param valid array with the valid options
     * @return chosen valid option
     */
    static String readValidOption(String... valid) {
        String choice;
        boolean isValid = false;

        do {
            choice = reader.next();
            for (String element : valid) {
                if (element.equals(choice)) {
                    isValid = true;
                    break;
                }
            }
        } while (!isValid);
        return choice;
    }
}
