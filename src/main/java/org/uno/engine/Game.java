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

import org.uno.engine.objects.*;
import org.uno.enums.CardColour;
import org.uno.enums.CardType;
import org.uno.enums.SpecialCardSymbol;
import org.uno.enums.WildCardSymbol;
import org.uno.exceptions.engineExceptions.CardIndexOutOfHandBoundsException;
import org.uno.exceptions.engineExceptions.EngineException;
import org.uno.exceptions.engineExceptions.InvalidOptionException;
import org.uno.exceptions.engineExceptions.MissingColourForWildCardException;
import org.uno.util.Stack;
import org.uno.util.Vector;

import java.util.Arrays;
import java.util.Random;


/**
 * A uno game abstraction. Use the {@link GameFactory} class the get instances
 * of this class.
 */
public final class Game implements UnoGame {

    /**
     * All the cards on the deck. The deck is where the players draw new cards.
     * It will get refilled with the cards from the table if it's close to get
     * empty.
     */
    private final Stack<Card> deck;

    /**
     * The collection of the cards which have been played
     */
    private final Stack<Card> table;

    /**
     * The players of this game
     */
    private final Player[] players;

    /**
     * Number of players on this game
     */
    private final int nOfPlayers;

    /**
     * The index of the player in turn
     */
    private int turn;

    /**
     * The index of the player who played previously
     */
    private int previous;

    /**
     * The direction to which the turn to play moves. 1 means it's moving towards
     * the player with higher index, -1 means it's moving towards the one with
     * lower index.
     */
    private int direction;

    /**
     * The number of cards each player starts with
     */
    private static final int N_OF_STARTING_CARDS = 7;

    /**
     * The winner of this game. It's meant to be null until the game is over
     */
    private Player winner;

    /**
     * Creates a new instance.
     *
     * @param players players for this game
     */
    Game(Player[] players) {
        this.deck = new DeckGenerator().next();
        this.table = new Stack<>();
        this.players = new Player[players.length];
        System.arraycopy(players, 0, this.players, 0, players.length);
        this.nOfPlayers = players.length;
        this.turn = new Random().nextInt(nOfPlayers);
        this.direction = 1;
        this.previous = turn;
        this.winner = null;
        distributeAndFlip();
    }

    /**
     * @see UnoGame#getNumberOfPlayers()
     */
    @Override
    public int getNumberOfPlayers() {
        return players.length;
    }

    /**
     * @see UnoGame#getPlayer(int)
     */
    @Override
    public Player getPlayer(int index) {
        return players[index];
    }

    /**
     * @see UnoGame#getPlayer(String)
     */
    @Override
    public Player getPlayer(String id) {
        Player returnValue = null;
        for (int i = 0; i < nOfPlayers; i++) {
            if (players[i].getId().equals(id))
                returnValue = players[i];
        }
        return returnValue;
    }

    /**
     * @see UnoGame#getPlayers()
     */
    @Override
    public Player[] getPlayers() {
        return Arrays.copyOf(players, players.length);
    }

    /**
     * @see UnoGame#getPreviousPlayer()
     */
    @Override
    public Player getPreviousPlayer() {
        return players[previous];
    }

    /**
     * @see UnoGame#getIndex(String)
     */
    @Override
    public int getIndex(String id) {
        for (int i = 0; i < players.length; i++) {
            if (players[i].getId().equals(id))
                return i;
        }
        return -1;
    }

    /**
     * @see UnoGame#getPlayerInTurn()
     */
    @Override
    public Player getPlayerInTurn() {
        return players[turn];
    }

    /**
     * @see UnoGame#getDeckTop()
     */
    @Override
    public Card getDeckTop() {
        return deck.peek();
    }

    /**
     * @see UnoGame#getTableTop()
     */
    @Override
    public Card getTableTop() {
        return table.peek();
    }

    /**
     * @see UnoGame#getWinner()
     */
    @Override
    public Player getWinner() {
        if (isOver())
            return winner;
        throw new IllegalStateException("This game is not over yet!");
    }

    /**
     * @see UnoGame#isOver()
     */
    @Override
    public boolean isOver() {
        return winner != null;
    }

    /**
     * Distributes the cards to the players and puts the first
     * card on the table.
     */
    private void distributeAndFlip() {
        for (Player player : players) {
            for (int i = 0; i < N_OF_STARTING_CARDS; i++) {
                player.addToHand(deck.pop());
            }
        }

        // Making sure the first card to be flipped is a numeric one
        CardType topType = deck.peek().getType();
        if (topType == CardType.NUMERIC)
            table.push(deck.pop());
        else {
            Stack<Card> temp = new Stack<>();
            while (deck.peek().getType() != CardType.NUMERIC)
                temp.push(deck.pop());
            table.push(deck.pop());
            for (int i = 0; i < temp.size(); i++) {
                deck.push(temp.pop());
            }
        }
    }

    /**
     * @see UnoGame#goBot()
     */
    @Override
    public GameCommand goBot() {
        GameCommand command = null;
        if (getPlayerInTurn().getClass() == BotPlayer.class) {
            Bot bot = (BotPlayer) getPlayerInTurn();
            command = bot.makeMove(this);
            try {
                executeMove(command);
            } catch (EngineException e) {
                // Bot should not cause game exceptions
                e.printStackTrace();
            }
        }
        return command;
    }

    /**
     * @see UnoGame#executeMove(GameCommand)
     */
    @Override
    public int executeMove(GameCommand command)
            throws InvalidOptionException,
            CardIndexOutOfHandBoundsException,
            MissingColourForWildCardException {
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
            throws MissingColourForWildCardException,
            CardIndexOutOfHandBoundsException,
            InvalidOptionException {
        if (command.getOption() == 0) {
            if (command.getIndex() != -1 || command.getColour() != null) {
                throw new IllegalStateException(
                        "Not expecting values for index and colour" +
                                                          "when option is 0"
                );
            }
        } else if (command.getOption() == 1) {
            if (command.getIndex() > players[turn].getHand().length() - 1 ||
                    command.getIndex() < 0)
                throw new CardIndexOutOfHandBoundsException(String.format(
                        "%d is out of hand range of %d",
                        command.getIndex(), players[turn].getHand().length())
                );
            if (players[turn].getHand().get(command.getIndex()).getClass() ==
                    WildCard.class && command.getColour() == null) {
                throw new MissingColourForWildCardException();
            }
        } else
            throw new InvalidOptionException("Invalid option");
    }

    private void doCaseSpecial() {
        SpecialCard tableTop = (SpecialCard) table.peek();
        if (tableTop.getSymbol() == SpecialCardSymbol.DRAW_2) {
            moveToNextPlayer();
            for (int i = 0; i < 2; i++)
                players[turn].addToHand(deck.pop());
            moveToNextPlayer();
        } else if (tableTop.getSymbol() == SpecialCardSymbol.REVERSE) {
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
        if (tableTop.getSymbol() == WildCardSymbol.DRAW_4) {
            moveToNextPlayer();
            for (int i = 0; i < 4; i++)
                players[turn].addToHand(deck.pop());
            moveToNextPlayer();
        } else {
            moveToNextPlayer();
        }
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

    /**
     * @see UnoGame#isCardValid(Card)
     */
    @Override
    public boolean isCardValid(Card card) {
        CardType tableTopType = table.peek().getType();
        boolean isValid;

        if (tableTopType == CardType.NUMERIC) {
            isValid = checkValidityForNumericTop(card);
        } else if (tableTopType == CardType.SPECIAL) {
            isValid = checkValidityForSpecialTop(card);
        } else { // wild in table
            isValid = checkValidityForWildTop(card);
        }
        return isValid;
    }

    private boolean checkValidityForNumericTop(Card card) {
        boolean returnValue;
        NumericCard tableTop = (NumericCard) table.peek();

        if (card.getType() == CardType.SPECIAL || card.getType() == CardType.NUMERIC) {
            if (card.getType() == CardType.SPECIAL) {
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

        if (card.getType() == CardType.NUMERIC) {
            NumericCard specialCard = (NumericCard) card;
            returnValue = specialCard.getColour() == tableTop.getColour();
        } else if (card.getType() == CardType.SPECIAL) {
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

        if (card.getType() == CardType.NUMERIC) {
            NumericCard numericCard = (NumericCard) card;
            returnValue = numericCard.getColour() == tableTop.getPickedColour();
        } else if (card.getType() == CardType.SPECIAL) {
            SpecialCard specialCard = (SpecialCard) card;
            returnValue = specialCard.getColour() == tableTop.getPickedColour();
        } else if (card.getType() == CardType.WILD) {
            returnValue = true;
        }
        return returnValue;
    }

    /**
     * Moves the turn to the next player
     */
    private void moveToNextPlayer() {
        if (direction > 0 && turn == nOfPlayers - 1)
            turn = 0;
        else if (direction < 0 && turn == 0)
            turn = nOfPlayers - 1;
        else
            turn += direction;
    }

    /**
     * Jumps a player
     */
    private void jumpAPlayer() {

        // The turnBackup variable is needed to store the current turn in order
        // to get right value to previous turn, otherwise the value in previous
        // will be incorrect
        moveToNextPlayer();
        moveToNextPlayer();
    }

    /**
     * Reverts the game. Usually done when someone plays a revert card
     */
    private void revert() {
        direction *= -1;
    }
}
