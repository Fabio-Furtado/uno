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

package org.uno.engine;

import org.uno.engine.objects.Card;
import org.uno.engine.objects.NumericCard;
import org.uno.engine.objects.SpecialCard;
import org.uno.engine.objects.WildCard;
import org.uno.util.Stack;

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
    private Stack<Card> deck;
    private int index;
    private static final int DECK_SIZE = 108;

    public DeckGenerator() {
        this.index = 0;
        deck = new Stack<>();
    }

    /**
     * Adds the wild cards the temporary deck.
     */
    private void addWilds(List<Card> listDeck) {
        for (int i = 0; i < N_WILD_EACH; i++)
            listDeck.add(this.index++, new WildCard(WildCardSymbol.CHANGE_COLOUR));

        for (int i = 0; i < N_WILD_EACH; i++)
            listDeck.add(this.index++, new WildCard(WildCardSymbol.DRAW_4));
    }

    /**
     * Adds numerals to the temporary deck.
     */
    private void addNumerals(List<Card> listDeck) {

        for (int i = 1; i < 10; i++) {
            // blue
            for (int j = 0; j < N_NUMERALS_PER_COLOUR; j++)
                listDeck.add(this.index++, NumericCard.of(CardColour.BLUE, i));
            // red
            for (int j = 0; j < N_NUMERALS_PER_COLOUR; j++)
                listDeck.add(this.index++, NumericCard.of(CardColour.RED, i));
            // green
            for (int j = 0; j < N_NUMERALS_PER_COLOUR; j++) {
                listDeck.add(this.index++, NumericCard.of(CardColour.GREEN, i));
            }
            // yellow
            for (int j = 0; j < N_NUMERALS_PER_COLOUR; j++)
                listDeck.add(this.index++, NumericCard.of(CardColour.YELLOW, i));
        }
    }

    /**
     * Adds the zeros to the temporary deck
     */
    private void addZeros(List<Card> listDeck) {

        // blue
        for (int j = 0; j < N_ZEROS_PER_COLOUR; j++)
            listDeck.add(this.index++, NumericCard.of(CardColour.BLUE, 0));

        // red
        for (int j = 0; j < N_ZEROS_PER_COLOUR; j++)
            listDeck.add(this.index++, NumericCard.of(CardColour.RED, 0));

        // green
        for (int j = 0; j < N_ZEROS_PER_COLOUR; j++)
            listDeck.add(this.index++, NumericCard.of(CardColour.GREEN, 0));

        // yellow
        for (int j = 0; j < N_ZEROS_PER_COLOUR; j++)
            listDeck.add(this.index++, NumericCard.of(CardColour.YELLOW, 0));
    }

    /**
     * Adds th especial cards to the temporary deck.
     */
    private void addSpecials(List<Card> listDeck) {
        // blue
        for (int i = 0; i < N_EACH_SPECIAL_PER_COLOUR; i++)
            listDeck.add(this.index++, SpecialCard.of(CardColour.BLUE, SpecialCardSymbol.SKIP));
        for (int i = 0; i < N_EACH_SPECIAL_PER_COLOUR; i++)
            listDeck.add(this.index++, SpecialCard.of(CardColour.BLUE, SpecialCardSymbol.REVERSE));
        for (int i = 0; i < N_EACH_SPECIAL_PER_COLOUR; i++)
            listDeck.add(this.index++, SpecialCard.of(CardColour.BLUE, SpecialCardSymbol.DRAW_2));
        // red
        for (int i = 0; i < N_EACH_SPECIAL_PER_COLOUR; i++)
            listDeck.add(this.index++, SpecialCard.of(CardColour.RED, SpecialCardSymbol.SKIP));
        for (int i = 0; i < N_EACH_SPECIAL_PER_COLOUR; i++)
            listDeck.add(this.index++, SpecialCard.of(CardColour.RED, SpecialCardSymbol.REVERSE));
        for (int i = 0; i < N_EACH_SPECIAL_PER_COLOUR; i++)
            listDeck.add(this.index++, SpecialCard.of(CardColour.RED, SpecialCardSymbol.DRAW_2));
        // green
        for (int i = 0; i < N_EACH_SPECIAL_PER_COLOUR; i++)
            listDeck.add(this.index++, SpecialCard.of(CardColour.GREEN, SpecialCardSymbol.SKIP));
        for (int i = 0; i < N_EACH_SPECIAL_PER_COLOUR; i++)
            listDeck.add(this.index++, SpecialCard.of(CardColour.GREEN, SpecialCardSymbol.REVERSE));
        for (int i = 0; i < N_EACH_SPECIAL_PER_COLOUR; i++)
            listDeck.add(this.index++, SpecialCard.of(CardColour.GREEN, SpecialCardSymbol.DRAW_2));
        // yellow
        for (int i = 0; i < N_EACH_SPECIAL_PER_COLOUR; i++)
            listDeck.add(this.index++, SpecialCard.of(CardColour.YELLOW, SpecialCardSymbol.SKIP));
        for (int i = 0; i < N_EACH_SPECIAL_PER_COLOUR; i++)
            listDeck.add(this.index++, SpecialCard.of(CardColour.YELLOW, SpecialCardSymbol.REVERSE));
        for (int i = 0; i < N_EACH_SPECIAL_PER_COLOUR; i++)
            listDeck.add(this.index++, SpecialCard.of(CardColour.YELLOW, SpecialCardSymbol.DRAW_2));
    }

    /**
     * Returns a new deck.
     *
     * @return deck with shuffled cards
     */
    public Stack<Card> next() {
        deck = new Stack<>();
        List<Card> deckList = new ArrayList<>(DECK_SIZE);
        addWilds(deckList);
        addNumerals(deckList);
        addZeros(deckList);
        addSpecials(deckList);
        Collections.shuffle(deckList);
        for (Card card : deckList)
            deck.push(card);
        index = 0;
        return deck;
    }
}
