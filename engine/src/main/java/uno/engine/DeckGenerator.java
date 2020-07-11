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

package uno.engine;

import uno.engine.objects.Card;
import uno.engine.objects.NumericCard;
import uno.engine.objects.SpecialCard;
import uno.engine.objects.WildCard;
import uno.util.Stack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Generator class for creating decks
 *
 * @author Fábio Furtado
 */
final class DeckGenerator {

    private static final int N_WILD_EACH = 4;
    private static final int N_ZEROS_PER_COLOUR = 1;
    private static final int N_NUMERALS_PER_COLOUR = 2;
    private static final int N_EACH_SPECIAL_PER_COLOUR = 2;
    private static final int DECK_SIZE = 108;

    private DeckGenerator(){}

    /**
     * Adds the wild cards the temporary deck.
     */
    private static void addWilds(List<Card> listDeck) {
        for (int i = 0; i < N_WILD_EACH; i++) {
            for (WildCardSymbol symbol : WildCardSymbol.values())
                listDeck.add(WildCard.of(symbol));
        }
    }

    /**
     * Adds numerals to the temporary deck.
     */
    private static void addNumerals(List<Card> listDeck) {
        for (int i = 1; i < 10; i++) {
            for (CardColour colour : CardColour.values()) {
                for (int j = 0; j < N_NUMERALS_PER_COLOUR; j++)
                    listDeck.add(NumericCard.of(colour, i));
            }
        }
    }

    /**
     * Adds the zeros to the temporary deck
     */
    private static void addZeros(List<Card> listDeck) {
        for (CardColour colour : CardColour.values()) {
            for (int i = 0; i < N_ZEROS_PER_COLOUR; i++)
                listDeck.add(NumericCard.of(colour, 0));
        }
    }

    /**
     * Adds th especial cards to the temporary deck.
     */
    private static void addSpecials(List<Card> listDeck) {
        for (CardColour colour : CardColour.values()) {
            for (int i = 0; i < N_EACH_SPECIAL_PER_COLOUR; i++) {
                for (SpecialCardSymbol symbol : SpecialCardSymbol.values())
                    listDeck.add(SpecialCard.of(colour, symbol));
            }
        }
    }

    /**
     * Returns a new deck.
     *
     * @return deck with shuffled cards
     */
    public static Stack<Card> next() {
        Stack<Card> deck = new Stack<>();
        List<Card> deckList = new ArrayList<>(DECK_SIZE);
        addWilds(deckList);
        addNumerals(deckList);
        addZeros(deckList);
        addSpecials(deckList);
        Collections.shuffle(deckList);
        for (Card card : deckList)
            deck.push(card);
        return deck;
    }
}
