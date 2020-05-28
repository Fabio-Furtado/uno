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

import org.uno.util.Vector;


/**
 * An Abstraction for a human player.
 *
 * @author Fábio Furtado
 */
public final class HumanPlayer implements Player {

    /**
     * A String to identify this player
     */
    private final String id;

    /**
     * An ADT with the cards on the player's hand
     */
    private final Vector<Card> hand;

    /**
     * Creates a new instance.
     * @param id a String to identify this player
     */
    public HumanPlayer(String id) {
        this.id = id;
        this.hand = new Vector<>();
    }

    /**
     * @see Player#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * @see Player#getHand()
     */
    @Override
    public Vector<Card> getHand() {
        return hand;
    }

    /**
     * @see Player#addToHand(Card)
     */
    @Override
    public void addToHand(Card card) {
        hand.add(card);
    }

    /**
     * @see Player#takeFromHand(int)
     */
    @Override
    public Card takeFromHand(int index) {
        Card returnValue = this.hand.get(index);
        hand.remove(index);
        return returnValue;
    }
}
