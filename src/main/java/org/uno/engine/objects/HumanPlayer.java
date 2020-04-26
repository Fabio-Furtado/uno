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

import org.uno.engine.Game;
import org.uno.engine.GameCommand;
import org.uno.util.Vector;

/**
 * Abstracts a human player.
 * @author Fábio Furtado
 */
public class HumanPlayer extends Player {

  private GameCommand nextMove;
  private static final String type = "human";

  public HumanPlayer(String id) {
    this.hand = new Vector<>();
    this.handSize = 0;
    this.id = id;
  }

  public void setNextMove(GameCommand nextMove) {
    this.nextMove = nextMove;
  }

  @Override
  public GameCommand makeMove(Game game) {
    return nextMove;
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
}
