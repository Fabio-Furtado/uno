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
import org.uno.engine.UnoGame;
import org.uno.enums.CardColour;
import org.uno.enums.CardType;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * An abstraction for a bot player.
 *
 * @author Fábio Furtado
 */
public final class BotPlayer implements Player, Bot {

    /**
     * A String to identify this player.
     */
    private final String id;

    /**
     * An ADT with the cards on the player's hand.
     */
    private final List<Card> hand;

    /**
     * Creates a new instance.
     *
     * @param id a String to identify this player
     */
    public BotPlayer(String id) {
        this.id = id;
        this.hand = new ArrayList<>();
    }

    /**
     * This constructor is meant to be used in the {@link BotPlayer#clone()}
     * class.
     *
     * @param id a String to identify this player
     * @param hand the hand of the player
     */
    private BotPlayer(String id, List<Card> hand) {
        this.id = id;
        this.hand = hand;
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
    public List<Card> getHand() {
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

    /**
     * @see Bot#makeMove(UnoGame)
     */
    @Override
    public GameCommand makeMove(UnoGame game) {
        int index = -1;
        CardColour colour = null;

        for (int i = 0; i < hand.size(); i++) {
            if (game.isCardValid(hand.get(i))) {
                index = i;
                if (hand.get(i).getType() == CardType.WILD)
                    colour = chooseColour();
                return new GameCommand(index, colour);
            }
        }
        return new GameCommand();
    }

    private CardColour chooseColour() {
        int bluesInHand = 0;
        int redsInHand = 0;
        int greensInHand = 0;
        int yellowsInHand = 0;
        
        for (Card card : hand) {
            if (card instanceof Colourful) {
                CardColour colour = ((Colourful) card).getColour();
                switch(colour) {
                    case BLUE:
                    bluesInHand++;
                    case RED:
                    redsInHand++;
                    case GREEN:
                    greensInHand++;
                    case YELLOW:
                    yellowsInHand++;
                }
                
            }
        }
        
        CardColour chosenColour = pickRandomColour();
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

    /**
     * @see Player#clone()
     */
    @Override
    public Player clone() {
        return new BotPlayer(this.id, new ArrayList<>(this.hand));
    }
}
