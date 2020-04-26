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

import org.uno.util.Vector;
import org.uno.enums.CardColour;
import org.uno.enums.SpecialCardSymbols;
import org.uno.enums.WildCardSymbols;
import org.uno.engine.objects.Card;
import org.uno.engine.objects.NumericCard;
import org.uno.engine.objects.WildCard;
import org.uno.engine.objects.SpecialCard;
import org.uno.util.Stack;

/**
 * 
 * @author Fábio Furtado
 *
 */
class DeckGenerator {

  private static final int N_WILD_SIMPLE = 4;
  private static final int N_WILD_DRAW_FOUR = 4;
  private static final int N_ZEROS_PER_COLOUR = 1;
  private static final int N_NUMERICS_PER_COLOUR = 2;
  private static final int N_EACH_SPECIAL_PER_COLOUR = 2;
  private Vector<Card> temporaryDeck;
  private Stack<Card> deck;
  private int index;
  private static final int DECK_SIZE = 108;

  public DeckGenerator() {
    this.index = 0;
    temporaryDeck = new Vector<>();
    deck = new Stack<>();
  }

  /**
   * Adds the wild cards the temporary deck.
   */
  private void addWilds() {
    for (int i = 0; i < N_WILD_SIMPLE; i++)
      temporaryDeck.add(this.index++, new WildCard(WildCardSymbols.CHANGE_COLOUR));

    for (int i = 0; i < N_WILD_DRAW_FOUR; i++)
      temporaryDeck.add(this.index++, new WildCard(WildCardSymbols.DRAW_4));
  }

  /**
   * Adds numerals to the temporary deck.
   */
  private void addNumerals() {

    for (int i = 1; i < 10; i++) {
      // blue
      for (int j = 0; j < N_NUMERICS_PER_COLOUR; j++)
        temporaryDeck.add(this.index++, new NumericCard(CardColour.BLUE, i));
      // red
      for (int j = 0; j < N_NUMERICS_PER_COLOUR; j++)
        temporaryDeck.add(this.index++, new NumericCard(CardColour.RED, i));
      // green
      for (int j = 0; j < N_NUMERICS_PER_COLOUR; j++) {
        temporaryDeck.add(this.index++, new NumericCard(CardColour.GREEN, i));
      }
      // yellow
      for (int j = 0; j < N_NUMERICS_PER_COLOUR; j++)
        temporaryDeck.add(this.index++, new NumericCard(CardColour.YELLOW, i));
    }
  }

  /**
   * Adds the zeros to the temporary deck
   */
  private void addZeros() {
    // blue
    for (int j = 0; j < N_ZEROS_PER_COLOUR; j++)
      temporaryDeck.add(this.index++, new NumericCard(CardColour.BLUE, 0));
    // red
    for (int j = 0; j < N_ZEROS_PER_COLOUR; j++)
      temporaryDeck.add(this.index++, new NumericCard(CardColour.RED, 0));
    // green
    for (int j = 0; j < N_ZEROS_PER_COLOUR; j++)
      temporaryDeck.add(this.index++, new NumericCard(CardColour.GREEN, 0));
    // yellow
    for (int j = 0; j < N_ZEROS_PER_COLOUR; j++)
      temporaryDeck.add(this.index++, new NumericCard(CardColour.YELLOW, 0));
  }

  /**
   * Adds th especial cards to the temporary deck.
   */
  private void addSpecials() {
    // blue
    for (int i = 0; i < N_EACH_SPECIAL_PER_COLOUR; i++)
      temporaryDeck.add(this.index++, new SpecialCard(CardColour.BLUE, SpecialCardSymbols.SKIP));
    for (int i = 0; i < N_EACH_SPECIAL_PER_COLOUR; i++)
      temporaryDeck.add(this.index++, new SpecialCard(CardColour.BLUE, SpecialCardSymbols.REVERSE));
    for (int i = 0; i < N_EACH_SPECIAL_PER_COLOUR; i++)
      temporaryDeck.add(this.index++, new SpecialCard(CardColour.BLUE, SpecialCardSymbols.DRAW_2));
    // red
    for (int i = 0; i < N_EACH_SPECIAL_PER_COLOUR; i++)
      temporaryDeck.add(this.index++, new SpecialCard(CardColour.RED, SpecialCardSymbols.SKIP));
    for (int i = 0; i < N_EACH_SPECIAL_PER_COLOUR; i++)
      temporaryDeck.add(this.index++, new SpecialCard(CardColour.RED, SpecialCardSymbols.REVERSE));
    for (int i = 0; i < N_EACH_SPECIAL_PER_COLOUR; i++)
      temporaryDeck.add(this.index++, new SpecialCard(CardColour.RED, SpecialCardSymbols.DRAW_2));
    // green
    for (int i = 0; i < N_EACH_SPECIAL_PER_COLOUR; i++)
      temporaryDeck.add(this.index++, new SpecialCard(CardColour.GREEN, SpecialCardSymbols.SKIP));
    for (int i = 0; i < N_EACH_SPECIAL_PER_COLOUR; i++)
      temporaryDeck.add(this.index++, new SpecialCard(CardColour.GREEN, SpecialCardSymbols.REVERSE));
    for (int i = 0; i < N_EACH_SPECIAL_PER_COLOUR; i++)
      temporaryDeck.add(this.index++, new SpecialCard(CardColour.GREEN, SpecialCardSymbols.DRAW_2));
    // yellow
    for (int i = 0; i < N_EACH_SPECIAL_PER_COLOUR; i++)
      temporaryDeck.add(this.index++, new SpecialCard(CardColour.YELLOW, SpecialCardSymbols.SKIP));
    for (int i = 0; i < N_EACH_SPECIAL_PER_COLOUR; i++)
      temporaryDeck.add(this.index++, new SpecialCard(CardColour.YELLOW, SpecialCardSymbols.REVERSE));
    for (int i = 0; i < N_EACH_SPECIAL_PER_COLOUR; i++)
      temporaryDeck.add(this.index++, new SpecialCard(CardColour.YELLOW, SpecialCardSymbols.DRAW_2));
  }

  /**
   * Returns a new deck.
   *
   * @return stack with the deck
   */
  public Stack<Card> next() {
    addWilds();
    addNumerals();
    addZeros();
    addSpecials();

    temporaryDeck.shuffle();

    for (int i = 0; i < DECK_SIZE; i++)
      deck.push(temporaryDeck.get(i));

    index = 0;
    return deck;
  }
}
