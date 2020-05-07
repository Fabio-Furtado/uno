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

import org.uno.exceptions.engineExceptions.*;
import org.uno.util.Stack;
import org.uno.util.Vector;
import org.uno.enums.CardColour;
import org.uno.enums.CardTypes;
import org.uno.enums.SpecialCardSymbols;
import org.uno.enums.WildCardSymbols;
import org.uno.engine.objects.*;

import java.util.Random;

/**
 * A uno game abstraction. Use the {@link GameFactory} class the get instances
 * of this class.
 */
public class Game {

    private final Stack<Card> deck;
    private final Stack<Card> table;
    private final Player[] players;
    private final int nOfPlayers;
    private int turn;
    private int previous;
    private int move;
    private static final int N_OF_STARTING_CARDS = 7;
    private Player winner;

    Game(Player[] players) {
        this.deck = new DeckGenerator().next();
        this.table = new Stack<>();
        this.players = new Player[players.length];
        System.arraycopy(players, 0, this.players, 0, players.length);
        this.nOfPlayers = players.length;
        this.turn = new Random().nextInt((nOfPlayers));
        this.move = 1;
        this.previous = turn;
        this.winner = null;
        start();
    }

    public int getNumberOfPlayers() {
        return players.length;
    }

    public Player getPlayer(int index) {
        return players[index];
    }

    /**
     * Returns a player a specified player.
     *
     * @param id id of the player
     * @return Player object, null if no player with this id was found
     */
    public Player getPlayer(String id) {
        Player returnValue = null;
        for (int i = 0; i < nOfPlayers; i++) {
            if (players[i].getId().equals(id))
                returnValue = players[i];
        }
        return returnValue;
    }

    public Player getPreviousPlayer() {
        return players[previous];
    }

    public Vector<Card> getPlayerHand(String id) {
        for (Player player : players) {
            if (player.getId().equals(id))
                return player.getHand();
        }
        return null;
    }

    /**
     * @deprecated
     */
    @Deprecated(forRemoval = true)
    public int getTurn() {
        return this.turn;
    }

    public String getPlayerInTurnId() {
        return players[turn].getId();
    }

    /**
     * Returns the id of the player who played in the previous move.
     * 
     * @return id of the player
     */
    public String getPreviousPlayerId() {
        return players[previous].getId();
    }

    /**
     * Gets the player in turn.
     * 
     * @return Player class with the abstraction of the player in turn
     */
    public Player getPlayerInTurn() {
        return players[turn];
    }

    public Card getDeckTop() {
        return deck.peek();
    }

    public Card getTableTop() {
        return table.peek();
    }

    public Player getWinner() {
        if (isOver())
            return winner;
        throw new IllegalStateException("This game is not over yet!");
    }

    public void start() {
        distributeAndFlip();
    }

    public boolean isOver() {
        return winner != null;
    }

    /**
     * Starts the game by distributing cards to the players and getting the first
     * card on the table.
     */
    private void distributeAndFlip() {
        for (Player player : players) {
            for (int j = 0; j < N_OF_STARTING_CARDS; j++) {
                player.addToHand(deck.pop());
            }
        }

        // Making sure the first card to be flipped is not a wild one
        if (deck.peek().getType() != CardTypes.WILD)
            table.push(deck.pop());

        else {
            Stack<Card> temp = new Stack<>();
            while (deck.peek().getType() == CardTypes.WILD)
                temp.push(deck.pop());
            table.push(deck.pop());
            for (int i = 0; i < temp.size(); i++) {
                deck.push(temp.pop());
            }
        }
    }

    /**
     * Executes the move made by the player. {@code code} consists of an integer
     * with 3 numbers at the most, being the first the move ('0' to draw a card, '1'
     * to play from the hand), the second the chosen colour in case of an wild card
     * ('0' for blue, '1' for red, '2' from green and '3' for yellow) and the last
     * being the index of the card the player wishes to play in case he chose to do
     * so.
     *
     * @param command command of the move
     * @return 0 if the play was successfully executed, 1 if the move is invalid
     */
    public int executeMove(GameCommand command)
            throws InvalidOptionException, CardIndexOutOfHandBoundsException, MissingColourForWildCardException {
        if (!isOver()) {
            makeSureDeckDoesNotGetEmpty();
            int returnValue = 1;
            checkCommandValidity(command);

            if (command.getOption() == 0) { // chose to draw
                draw();
                returnValue = 0;
            } else if (command.getOption() == 1) { // chose to play
                returnValue = play(command);
                if (players[previous].getHand().isEmpty())
                    winner = players[previous];
            }
            return returnValue;
        } else
            throw new IllegalStateException("This game is already over!");
    }

    private void draw() {
        // player draws a card
        players[turn].addToHand(deck.pop());
        // player does not stop drawing until it finds a playable card
        previous = turn;
        moveToNextPlayer();
    }

    private int play(GameCommand gameCommand) {
        int returnValue = 1;
        Card card = players[turn].getHand().get(gameCommand.getIndex());

        if (isCardValid(card)) {
            previous = turn;
            table.push(players[turn].takeFromHand(gameCommand.getIndex()));

            // execute the action demanded by the played card
            switch (table.peek().getType()) {
            case SPECIAL:
                doCaseSpecial();
                break;

            case WILD:
                doCaseWild(gameCommand);
                break;

            case NUMERIC:
                // No special interactions to do in case of an numeral card so
                // we just have to move to the next player
                moveToNextPlayer();
                break;
            }
            returnValue = 0;
        }
        return returnValue;
    }

    private void checkCommandValidity(GameCommand command)
            throws MissingColourForWildCardException, CardIndexOutOfHandBoundsException, InvalidOptionException {
        if (command.getOption() == 0) {
            if (command.getIndex() != -1 || command.getColour() != null) {
                throw new IllegalStateException("Not expecting values for index and colour" + "when option is 0");
            }
        } else if (command.getOption() == 1) {
            if (command.getIndex() > players[turn].getHand().length() - 1)
                throw new CardIndexOutOfHandBoundsException(String.format("%d is out of hand range of %d",
                        command.getIndex(), players[turn].getHand().length()));
            if (players[turn].getHand().get(command.getIndex()).getClass() == WildCard.class
                    && command.getColour() == null) {
                throw new MissingColourForWildCardException();
            }
        } else
            throw new InvalidOptionException("Invalid option");
    }

    private void doCaseSpecial() {
        SpecialCard tableTop = (SpecialCard) table.peek();
        if (tableTop.getSymbol() == SpecialCardSymbols.DRAW_2) {
            moveToNextPlayer();
            for (int i = 0; i < 2; i++)
                players[turn].addToHand(deck.pop());
            moveToNextPlayer();
        } else if (tableTop.getSymbol() == SpecialCardSymbols.REVERSE) {
            revert();
            if (nOfPlayers > 2)
                moveToNextPlayer();
        } else { // skip card
            jumpAPlayer();
        }
    }

    /**
     * Sets the chosen colour for the wild card and adds 4 cards to the next
     * player's hand in case the card is a wild 4.
     *
     * @param command command of the move
     */
    private void doCaseWild(GameCommand command) {
        CardColour colour = command.getColour();

        WildCard c = (WildCard) table.pop();
        c.setPickedColour(colour);
        table.push(c);

        WildCard tableTop = (WildCard) table.peek();
        if (tableTop.getSymbol() == WildCardSymbols.DRAW_4) {
            moveToNextPlayer();
            for (int i = 0; i < 4; i++)
                players[turn].addToHand(deck.pop());
            moveToNextPlayer();
        } else {
            moveToNextPlayer();
        }
    }

    /**
     * If the player in turn is a bot, it will make it's move and it will be
     * executed.
     *
     * @return bot player move code.
     */
    public GameCommand goBot() {
        GameCommand command = null;
        if (getPlayerInTurn().getClass() == BotPlayer.class) {
            command = getPlayerInTurn().makeMove(this);
            try {
                executeMove(command);
            } catch (EngineException e) {
                // Bot should not cause game exceptions
                e.printStackTrace();
            }
        }
        return command;
    }

    private void makeSureDeckDoesNotGetEmpty() {
        if (deck.size() < 4) {
            Card tableTop = table.pop();
            Vector<Card> temp = new Vector<>(table.size());
            for (int i = 0; i < table.size(); i++)
                temp.add(i, table.pop());
            table.push(tableTop);
            temp.shuffle();
            for (int i = 0; i < temp.length(); i++)
                deck.push(temp.get(i));
        }
    }

    public boolean isCardValid(Card card) {
        CardTypes tableTopType = table.peek().getType();
        boolean isValid;

        if (tableTopType == CardTypes.NUMERIC) {
            isValid = checkValidityForNumericTop(card);
        } else if (tableTopType == CardTypes.SPECIAL) {
            isValid = checkValidityForSpecialTop(card);
        } else { // wild in table
            isValid = checkValidityForWildTop(card);
        }
        return isValid;
    }

    private boolean checkValidityForNumericTop(Card card) {
        boolean returnValue;
        NumericCard tableTop = (NumericCard) table.peek();

        if (card.getType() == CardTypes.SPECIAL || card.getType() == CardTypes.NUMERIC) {
            if (card.getType() == CardTypes.SPECIAL) {
                SpecialCard numericCard = (SpecialCard) card;
                returnValue = numericCard.getColour() == tableTop.getColour();
            } else {
                NumericCard numericCard = (NumericCard) card;
                returnValue = numericCard.getColour() == tableTop.getColour()
                        || numericCard.getNumber() == tableTop.getNumber();
            }
        } else { // wild card
            returnValue = true;
        }
        return returnValue;
    }

    private boolean checkValidityForSpecialTop(Card card) {
        SpecialCard tableTop = (SpecialCard) table.peek();
        boolean returnValue;

        if (card.getType() == CardTypes.NUMERIC) {
            NumericCard specialCard = (NumericCard) card;
            returnValue = specialCard.getColour() == tableTop.getColour();
        } else if (card.getType() == CardTypes.SPECIAL) {
            SpecialCard specialCard = (SpecialCard) card;
            returnValue = specialCard.getColour() == tableTop.getColour()
                    || specialCard.getSymbol() == tableTop.getSymbol();
        } else {
            // No restrictions for wild card
            returnValue = true;
        }
        return returnValue;
    }

    private boolean checkValidityForWildTop(Card card) {
        WildCard tableTop = (WildCard) table.peek();
        boolean returnValue = false;

        if (card.getType() == CardTypes.NUMERIC) {
            NumericCard numericCard = (NumericCard) card;
            returnValue = numericCard.getColour() == tableTop.getPickedColour();
        } else if (card.getType() == CardTypes.SPECIAL) {
            SpecialCard specialCard = (SpecialCard) card;
            returnValue = specialCard.getColour() == tableTop.getPickedColour();
        } else if (card.getType() == CardTypes.WILD) {
            returnValue = true;
        }
        return returnValue;
    }

    private void moveToNextPlayer() {
        if (move > 0 && turn == nOfPlayers - 1)
            turn = 0;
        else if (move < 0 && turn == 0)
            turn = nOfPlayers - 1;
        else
            turn += move;
    }

    /**
     * Jumps a player
     */
    private void jumpAPlayer() {

        // The turnBackup variable is needed to store the current turn in order to
        // get right value to previous turn, otherwise the value in previous will be
        // incorrect
        moveToNextPlayer();
        moveToNextPlayer();
    }

    private void revert() {
        move *= -1;
    }
}
