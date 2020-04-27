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

package org.uno.cli;

import org.uno.engine.Game;
import org.uno.engine.GameCommand;
import org.uno.engine.GameFactory;
import org.uno.engine.objects.Card;
import org.uno.engine.objects.HumanPlayer;
import org.uno.engine.objects.Player;
import org.uno.enums.CardColour;
import org.uno.enums.CardTypes;
import org.uno.exceptions.CommandFormatException;
import org.uno.exceptions.GameRulesException;
import org.uno.exceptions.engineExceptions.CardIndexOutOfHandBoundsException;
import org.uno.exceptions.engineExceptions.EngineException;
import org.uno.exceptions.engineExceptions.InvalidOptionException;
import org.uno.exceptions.engineExceptions.MissingColourForWildCardException;
import org.uno.util.Vector;

/**
 * @author Fábio Furtado
 */
public class CLI implements CommandLineReader {

  private Game game;
  private static final String PROMPT_SYMBOL = "> ";
  private final GameFactory gameFactory;
  private static final String COMMANDS_HELP = buildCommandsHelpString();
  private static final String INVALID_CARD_INDEX_ERROR_MESSAGE =
      "The index %d is not valid for your hand";
  private static final String INVALID_MOVE_ERROR_MESSAGE =
      "The move you chose is not valid";
  private String humanPlayerName;

  public CLI() {
    gameFactory = new GameFactory();
  }

  public void start(String humanPlayerName) {
    System.out.printf("%nWelcome to UNO!!%n%n");
    this.humanPlayerName = humanPlayerName;
    try {
      this.game = gameFactory.newGame(askForNumberOfPlayers() - 1, humanPlayerName);
      System.out.println();
    } catch (GameRulesException e) {
      e.printStackTrace();
      // TODO terminate program at this point
    }
    startPlaying();
  }

  private int askForNumberOfPlayers() {
    System.out.print("How many players will your game have?: ");
    return CliUtils.readValueInRange(GameFactory.getMinNumberOfPlayers(),
                                     GameFactory.getMaxNumberOfPlayers());
  }

  private void startPlaying() {
    boolean run = true;

    while (run) {
      printTableTop();
      System.out.print("  |  ");
      printTurn();
      System.out.println();

      if (game.getPlayerInTurn().getClass() == HumanPlayer.class) {
        if (!prompt())
          run = false;
      } else {
        GameCommand move = game.goBot();
        reportMove(move);
      }
      warnIfRivalIsAboutToWin();
      if (game.isOver()) {
        System.out.printf("%nGAME OVER!!!%n%s Won!%n", game.getWinner().getId());
        run = false;
      }
    }
  }

  private boolean prompt() {
    boolean keepPlaying = true;
    System.out.print(PROMPT_SYMBOL);
    String cliCommand = reader.nextLine();
    System.out.println();

    if (cliCommand.equals(CommandValuesKeeper.getValue(Command.HELP)))
      System.out.println(COMMANDS_HELP);
    else if (cliCommand.equals(CommandValuesKeeper.getValue(Command.PRINT_HAND))) {
      printHand(game.getPlayerInTurnId());
    }
    else if (cliCommand.equals(CommandValuesKeeper.getValue(Command.DRAW))) {
      try {
        GameCommand move = CLItoEngineCommandConverter.convert(cliCommand);
        game.executeMove(move);
        reportMove(move);
      } catch (CommandFormatException | EngineException e) {
        e.printStackTrace();
      }
    }
    else if (cliCommand.startsWith(CommandValuesKeeper.getValue(Command.PLAY_A_CARD))) {
      playACard(cliCommand);
    }
    else if(cliCommand.equals(CommandValuesKeeper.getValue(Command.PRINT_RIVALS_HAND_LENGTH))) {
      printNumberOfCardsEachRivalPlayerHas();
    }
    else if(cliCommand.equals(CommandValuesKeeper.getValue(Command.RESTART))) {
      restart();
    }
    else if (cliCommand.equals(CommandValuesKeeper.getValue((Command.EXIT))))
      keepPlaying = false;
    else
      System.out.printf("Invalid command, type <%s> to see the available ones%n%n",
                                  CommandValuesKeeper.getValue(Command.HELP));
    return keepPlaying;
  }

  private void playACard(String cliCommand) {
    GameCommand move = null;
    int executionResult;
    try {
      move = CLItoEngineCommandConverter.convert(cliCommand);
      executionResult = game.executeMove(move);

      if (executionResult == 0)
        reportMove(move);
      else if (executionResult == 1)
        System.out.println(INVALID_MOVE_ERROR_MESSAGE);
    } catch (CommandFormatException e) {
      System.out.println(e.getMessage());
      System.out.println();
    } catch (InvalidOptionException invalidOptionException) {

      // The exception above already prevents this exception
      invalidOptionException.printStackTrace();
    } catch (CardIndexOutOfHandBoundsException cardIndexOutOfHandBoundsException) {
      System.out.printf("Invalid card index%n%n");
    } catch (MissingColourForWildCardException e) {
      GameCommand corrected = new GameCommand(move.getOption(), move.getIndex(), pickWildCardColour());
      reportMove(corrected);
      try {
        executionResult = game.executeMove(corrected);
      } catch (EngineException engineException) {

        // All engine exceptions are dealt with by this point so this exception
        // should not be thrown
        e.printStackTrace();
      }
    }
  }

  private CardColour pickWildCardColour() {
    CardColour colour;
    System.out.printf(
        "You have to pick a colour for your wild card,%n" +
            "1) Blue%n" +
            "2) Red%n" +
            "3) Green%n" +
            "4) Yellow%n" +
            "please choose a colour: ");
    int choice = CliUtils.readValueInRange(1, 4);
    System.out.println();
    if (choice == 1)
      colour = CardColour.BLUE;
    else if (choice == 2)
      colour = CardColour.RED;
    else if (choice == 3)
      colour = CardColour.GREEN;
    else
      colour = CardColour.YELLOW;
    return colour;
  }

  private void printTableTop() {
    System.out.print("Table Top: ");
    CardPrinter.printCard(game.getTableTop());
  }

  private void printTurn() {
    System.out.printf("It's %s's turn%n", game.getPlayerInTurnId());
  }

  /**
   * Prints a String representation of the chosen player's hand.
   *
   * @param playerID player in stake.
   */
  private void printHand(String playerID) {
    System.out.printf("%s's hand: %n", playerID);
    Vector<Card> hand = game.getPlayerHand(playerID);

    for (int i = 0; i < hand.length(); i++) {
      System.out.printf("%d - ", i + 1);
      CardPrinter.printCard(hand.get(i));
      System.out.println();
    }
    System.out.println();
  }

  private void reportMove(GameCommand move) {
    if (move.getOption() == 0)
      System.out.printf("%s has drawn a card%n", game.getPreviousPlayerId());
    else if (move.getOption() == 1) {
      System.out.printf("%s has played a ", game.getPreviousPlayerId());
      CardPrinter.printCard(game.getTableTop());
      System.out.println();
      if (game.getTableTop().getType() == CardTypes.WILD) {
        System.out.printf("and has chosen %s%n", move.getColour());
      }
    }
    System.out.println();
  }

  private void printNumberOfCardsEachRivalPlayerHas() {
    final int numberOfPlayers = game.getNumberOfPlayers();
    final String card = "card";
    final String cards = "cards";

    for (int i = 0; i < numberOfPlayers; i++) {
      Player player = game.getPlayer(i);
      if(!player.getId().equals(humanPlayerName)) {
        Vector<Card> hand = player.getHand();
        if (hand.length() > 1)
          System.out.printf("%s -> %d %s left%n",
              player.getId(), player.getHand().length(), cards);
        else
          System.out.printf("%s -> %d %s left%n",
              player.getId(), player.getHand().length(), card);
        System.out.println();
      }
    }
  }

  private void warnIfRivalIsAboutToWin() {

    if (game.getPlayerInTurn().getHand().length() == 1 &&
        !game.getPreviousPlayer().getId().equals(humanPlayerName))
      System.out.printf("CAREFUL: %s has only one card left%n%n",
          game.getPlayerInTurn().getId());
  }

  private static String buildCommandsHelpString() {
    final int columnsForPrefix = 10;
    StringBuilder sb = new StringBuilder();
    Command[] commands = new Command[] {Command.PRINT_HAND, Command.PLAY_A_CARD,
                                        Command.DRAW, Command.HELP};
    String[] messages = new String[] {
        "Print your hand%n",
        "Play a card%n",
        "Draw a card%n",
        "Print this helper%n"};

    for (int i = 0; i < commands.length; i++) {
      String commandValue = CommandValuesKeeper.getValue(commands[i]);
      sb.append(commandValue.concat(".".repeat(columnsForPrefix - commandValue.length())));

      // The IDE may say this String.format is redundant although it's necessary
      // to format the line breaks
      sb.append(String.format(messages[i]));
    }
    return sb.toString();
  }

  private void restart() {
    start(humanPlayerName);
  }

  public static void close() {
    CommandLineReader.reader.close();
  }
}
