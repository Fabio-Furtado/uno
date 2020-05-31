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
import org.uno.util.Vector;

import java.util.Random;


/**
 * An abstraction for a bot player
 *
 * @author Fábio Furtado
 */
public final class BotPlayer implements Player, Bot {

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
     *
     * @param id a String to identify this player
     */
    public BotPlayer(String id) {
        this.id = id;
        this.hand = new Vector<>();
    }

    /**
     * This constructor is meant to be used in the {@link BotPlayer#clone()}
     * class.
     *
     * @param id a String to identify this player
     * @param hand the hand of the player
     */
    private BotPlayer(String id, Vector<Card> hand) {
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

    /**
     * @see Bot#makeMove(UnoGame)
     */
    @Override
    public GameCommand makeMove(UnoGame game) {
        int option = 0;
        int index = -1;
        CardColour colour = null;
        GameCommand command;

        for (int i = 0; i < hand.length(); i++) {
            if (game.isCardValid(hand.get(i))) {
                if (hand.get(i).getType() == CardType.WILD) {
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
        if (option == 0)
            command = new GameCommand();
        else
            command = colour == null ? new GameCommand(index) :
                    new GameCommand(index, colour);
        return command;
    }

    private CardColour chooseColour() {
        int bluesInHand = 0;
        int redsInHand = 0;
        int greensInHand = 0;
        int yellowsInHand = 0;
        CardColour chosenColour = pickRandomColour();

        for (int i = 0; i < hand.length(); i++) {
            CardType type = hand.get(i).getType();

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

    /**
     * @see Player#clone()
     */
    @Override
    public Player clone() {
        return new BotPlayer(this.id, this.hand.clone());
    }
}
