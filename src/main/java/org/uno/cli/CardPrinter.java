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

import org.uno.engine.objects.Card;
import org.uno.engine.objects.NumericCard;
import org.uno.engine.objects.SpecialCard;
import org.uno.engine.objects.WildCard;

/**
 * Print information about a card in the console.
 *
 * @author Fábio Furtado
 */
class CardPrinter {

    /**
     * Prints String description of the card.
     *
     * @param card card to be printed
     */
    static void printCard(Card card) {

        switch (card.getType()) {

        case NUMERIC:
            NumericCard numerical = (NumericCard) card;
            printNumerical(numerical);
            break;

        case WILD:
            WildCard wild = (WildCard) card;
            printWild(wild);
            break;

        case SPECIAL:
            SpecialCard special = (SpecialCard) card;
            printSpecial(special);
            break;
        }
    }

    private static void printNumerical(NumericCard numCard) {
        System.out.printf("%s %d", numCard.getColour(), numCard.getNumber());
    }

    private static void printSpecial(SpecialCard specialCard) {
        System.out.printf("%s %s", specialCard.getColour(), specialCard.getSymbol());
    }

    private static void printWild(WildCard wildCard) {
        if (wildCard.getPickedColour() == null)
            System.out.printf("Wild %s", wildCard.getSymbol());
        else
            System.out.printf("%s %s", wildCard.getPickedColour(), wildCard.getSymbol());
    }
}
