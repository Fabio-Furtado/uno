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

package org.uno.engine.objects;

import org.uno.engine.GameCommand;
import org.uno.util.Vector;

import org.uno.engine.Game;
import org.uno.enums.CardColour;
import org.uno.enums.CardTypes;

import java.util.Random;


public class BotPlayer extends Player {

  public BotPlayer(String id) {
    this.hand = new Vector<>();
    this.id = id;
    this.handSize = 0;

  }

  @Override
  public void addToHand(Card card) {
    hand.add(card);
    handSize++;
  }

  @Override
  public Card takeFromHand(int index) {
    Card returnValue = this.hand.get(index);
    hand.remove(index);
    handSize--;
    return returnValue;
  }

  @Override
  public GameCommand makeMove(Game game) {
    int option = 0;
    int index = -1;
    CardColour colour = null;
    int returnValue = 0; // draw
    for (int i = 0; i < hand.length(); i++) {
      if (game.isCardValid(hand.get(i))) {
        if (hand.get(i).getType() == CardTypes.WILD) {
          // wild card
          option = 1;
          index = i;
          colour = chooseColour();
        } else {

          // non-wild card
          option = 1;
          index = i;
        }
        break;
      }
    }
    return new GameCommand(option, index, colour);
  }

  private CardColour chooseColour() {
    int bluesInHand = 0;
    int redsInHand = 0;
    int greensInHand = 0;
    int yellowsInHand = 0;
    CardColour chosenColour = pickRandomColour();

    for (int i = 0; i < hand.length(); i++) {
      CardTypes type = hand.get(i).getType();

      switch (type) {
      case NUMERIC:
        NumericCard card = (NumericCard) hand.get(i);
        if (card.getColour() == CardColour.BLUE)
          bluesInHand++;
        else if (card.getColour() == CardColour.RED)
          redsInHand++;
        else if (card.getColour() == CardColour.GREEN)
          greensInHand++;
        else if (card.getColour() == CardColour.YELLOW)
          yellowsInHand++;
        break;

      case SPECIAL:
        SpecialCard card_2 = (SpecialCard) hand.get(i);
        if (card_2.getColour() == CardColour.BLUE)
          bluesInHand++;
        else if (card_2.getColour() == CardColour.RED)
          redsInHand++;
        else if (card_2.getColour() == CardColour.GREEN)
          greensInHand++;
        else if (card_2.getColour() == CardColour.YELLOW)
          yellowsInHand++;
        break;

      default: // Wild
        break;
      }
    }

    int[] arr = {bluesInHand, redsInHand, greensInHand, yellowsInHand};
    int biggerValue = arr[new Random().nextInt(4)];

    if (bluesInHand > biggerValue) {
      biggerValue = bluesInHand;
      chosenColour = CardColour.BLUE;
    }
    if (redsInHand > biggerValue) {
      biggerValue = redsInHand;
      chosenColour = CardColour.RED;
    }
    if (greensInHand > biggerValue) {
      biggerValue = greensInHand;
      chosenColour = CardColour.GREEN;
    }
    if (yellowsInHand > biggerValue) {
      biggerValue = yellowsInHand;
      chosenColour = CardColour.YELLOW;
    }

    return chosenColour;
  }

  private CardColour pickRandomColour() {
    CardColour[] colours = CardColour.values();
    Random rand = new Random();
    return colours[rand.nextInt(colours.length)];
  }
}
