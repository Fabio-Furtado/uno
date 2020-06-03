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


import java.util.List;


/**
 * This interface should be applied to any player class.
 *
 * @author Fábio Furtado
 */
public interface Player {

    /**
     * Gets this player's id.
     *
     * @return player id
     */
    String getId();

    /**
     * Gets this players hand.
     *
     * @return player's hand
     */
    List<Card> getHand();

    /**
     * Adds the given card to the player's hand.
     *
     * @requires {@code card != null}
     * @param card card to be added
     */
    void addToHand(Card card);

    /**
     * Removes the card with the given index from the player's hand.
     *
     * @param index index of the card on the player's hand
     * @requires {@code index < this.getHand().size() && index > -1}
     *
     * @return removed card
     */
    Card takeFromHand(int index);

    /**
     * Returns a clone of this Player.
     *
     * @return clone of this player
     */
    Player clone();
}
