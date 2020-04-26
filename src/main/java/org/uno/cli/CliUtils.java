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

import java.util.Arrays;
import java.util.Scanner;

/**
 * @author Fábio Furtado
 */
class CliUtils {

  /**
   * Makes sure the user inputs a value within the given range.
   *
   * @param beginning beginning of the range
   * @param end       end of the range
   * @return valid value imputed by user
   */
  static int readValueInRange(int beginning, int end) {
    int choice = 0;
    try {
      do {
        Scanner scanner = new Scanner(System.in);
        choice = Integer.parseInt(scanner.next());
        if (choice < beginning || choice > end)
          System.out.printf("Invalid value, insert a number between %d and %d: ",
              beginning, end);
      } while (choice < beginning || choice > end);
    } catch (NumberFormatException e) {
      System.out.printf(
          "Invalid input, insert a number between %d and %d: ", beginning, end);
      readValueInRange(beginning, end);
    }
    return choice;
  }

  /**
   * Reads a valid option from {@code System.in}.
   * @param valid array with the valid options
   * @return chosen valid option
   */
  static String readValidOption(String... valid) {
    Scanner scanner = new Scanner(System.in);
    String choice = "";
    boolean isValid = false;

    do {
      choice = scanner.next();
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
