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

package org.uno.cli

import org.uno.data.ConfigReader
import org.uno.engine.Game
import org.uno.engine.GameCommand
import org.uno.engine.objects.HumanPlayer
import org.uno.engine.CardColour
import org.uno.engine.CardType
import org.uno.engine.engineExceptions.CardIndexOutOfHandBoundsException
import org.uno.engine.engineExceptions.InvalidOptionException
import org.uno.engine.engineExceptions.MissingColourForWildCardException
import java.util.*

/**
 * A command line interface for the uno game.
 *
 * @author Fábio Furtado
 */
class CLI(humanPlayerName: String) : CommandLineReader {

    private val humanPlayerName = humanPlayerName
    private var game = Game.instance(askForNumberOfPlayers() - 1, humanPlayerName)

    companion object {

        private var PROMPT_SYMBOL: String
        private var ENABLE_BOT_DELAY: Boolean
        private val COMMANDS_HELP = buildCommandsHelpString()
        private const val INVALID_MOVE_ERROR_MESSAGE = "The move you chose is not valid"

        init {
            val sym = ConfigReader.get("cli", "prompt_symbol")
            PROMPT_SYMBOL = if (sym != null) sym as String else "> "
            val delay = ConfigReader.get("bot_delay")
            ENABLE_BOT_DELAY = if (delay != null) delay as Boolean else true
        }

        private fun buildCommandsHelpString(): String {
            val columnsForPrefix = 18
            val sb = StringBuilder()
            val commands = arrayOf(
                    Command.PRINT_HAND, Command.PLAY_A_CARD, Command.DRAW,
                    Command.PRINT_RIVALS_HAND_LENGTH, Command.RESTART, Command.EXIT,
                    Command.HELP)
            val messages = arrayOf(
                    "Print your hand\n", "Play a card\n", "Draw a card\n",
                    "Check how many cards your opponents have\n", "Restart the game\n",
                    "Exit the game\n", "Print this helper\n")
            for (i in commands.indices) {
                val commandValue = CommandValuesKeeper.getValue(commands[i])
                sb.append(commandValue +
                        ".".repeat(columnsForPrefix - commandValue.length))

                // The IDE may say this String.format is redundant although it's
                // necessary to format the line breaks
                sb.append(String.format(messages[i]))
            }
            return sb.toString()
        }
    }

    private fun askForNumberOfPlayers(): Int {
        println("\nWelcome to UNO!!\n")
        print("How many players will your game have?: ")
        return CliUtils.readValueInRange(
                Game.MIN_NUMBER_OF_PLAYERS,
                Game.MAX_NUMBER_OF_PLAYERS
        )
    }

    fun start() {
        var run = true
        while (run) {
            printTableTop()
            print("  |  ")
            printTurn()
            println()
            if (game.playerInTurn is HumanPlayer) {
                if (!prompt()) run = false
            } else {
                val move = game.goBot()
                if (ENABLE_BOT_DELAY) addBotThinkingDelay()
                reportMove(move)
            }
            warnIfRivalIsAboutToWin()
            if (game.isOver()) {
                println("\nGAME OVER!!!\n${game.winner!!.id} Won!")
                run = false
            }
        }
    }

    private fun prompt(): Boolean {
        var keepPlaying = true
        print(PROMPT_SYMBOL)
        val cliCommand = CommandLineReader.reader.nextLine().strip().split(" ").toTypedArray()
        println()
        if (cliCommand.isNotEmpty() && cliCommand[0].isNotEmpty()) {
            if (CommandValuesKeeper.isEngineConvertible(cliCommand[0]))
                drawOrPlayACard(cliCommand)
            else if (CommandValuesKeeper.isCliExclusive(cliCommand[0])) {
                if (cliCommand.size == 1)
                    keepPlaying = doForCliExclusiveCommand(cliCommand[0])
                else println(
                    "No arguments expected for the <${cliCommand[0]}> command\n"
                )
            } else println(
                "Invalid command <${cliCommand[0]}>, type " +
                "<${CommandValuesKeeper.getValue(Command.HELP)}> to see the " +
                "available ones\n"
            )
        } else {
            prompt()
        }
        return keepPlaying
    }

    private fun doForCliExclusiveCommand(cliCommand: String): Boolean {
        var keepPlaying = true
        when (cliCommand) {
            CommandValuesKeeper.getValue(Command.HELP)
                          -> println(COMMANDS_HELP)
            CommandValuesKeeper.getValue(Command.PRINT_HAND)
                          -> printHand(game.playerInTurn.id)
            CommandValuesKeeper.getValue(Command.PRINT_RIVALS_HAND_LENGTH)
                          -> printNumberOfCardsEachRivalPlayerHas()
            CommandValuesKeeper.getValue(Command.RESTART) -> restart()
            CommandValuesKeeper.getValue(Command.EXIT) -> keepPlaying = false
        }
        return keepPlaying
    }

    private fun drawOrPlayACard(cliCommand: Array<String>) {
        val move: GameCommand
        val executionResult: Int
        try {
            move = CLItoEngineCommandConverter.convert(cliCommand)
            executionResult = game.executeMove(move)
            if (executionResult == 0)
                reportMove(move)
            else if (executionResult == 1)
                println(INVALID_MOVE_ERROR_MESSAGE)
        } catch (e: CommandFormatException) {
            if (e.message == String.format(
                        CLItoEngineCommandConverter.insufficientArgumentsErrorMessage,
                        cliCommand[0]
                    )) {
                val corrected = cliCommand.copyOf(cliCommand.size + 1) as Array<String>
                corrected[1] = pickIndex().toString()
                drawOrPlayACard(corrected)
            } else {
                println(e.message)
                println()
            }
        } catch (e: InvalidOptionException) {

            // The exception catch above already prevents this exception
            e.printStackTrace()
        } catch (cardIndexOutOfHandBoundsException: CardIndexOutOfHandBoundsException) {
            println("Invalid card index\n")
        } catch (e: MissingColourForWildCardException) {
            val corrected = cliCommand.copyOf(cliCommand.size + 1) as Array<String>
            corrected[2] = pickWildCardColour()
            drawOrPlayACard(corrected)
        }
    }

    private fun pickIndex(): Int {
        val index: Int
        val playerInTurn = game.playerInTurn
        println("You have to choose the index of the card you wish to play")
        printHand(playerInTurn.id)
        print("Choose please: ")
        index = CliUtils.readValueInRange(1, playerInTurn.hand.size)
        return index
    }

    private fun pickWildCardColour(): String {
        val colour: String
        print("You have to pick a colour for your wild card,\n"
                + "1) Blue\n" + "2) Red\n" + "3) Green\n"
                + "4) Yellow\n" + "please choose a colour: ")
        val choice = CliUtils.readValueInRange(1, 4)
        println()
        colour = when (choice) {
            1 -> CommandValuesKeeper.getValue(CardColour.BLUE)
            2 -> CommandValuesKeeper.getValue(CardColour.RED)
            3 -> CommandValuesKeeper.getValue(CardColour.GREEN)
            else -> CommandValuesKeeper.getValue(CardColour.YELLOW)
        }
        return colour
    }

    private fun printTableTop() {
        print("Table Top: ")
        CardPrinter.printCard(game.getTableTop())
    }

    private fun printTurn() {
        println("It's ${game.playerInTurn.id}'s turn")
    }

    /**
     * Prints a String representation of the chosen player's hand.
     *
     * @param playerID player in stake.
     */
    private fun printHand(playerID: String) {
        println("${playerID}'s hand: \n")
        val hand = game.getPlayer(playerID)!!.hand
        for (i in hand.indices) {
            print("${i + 1} - ")
            CardPrinter.printCard(hand[i])
            println()
        }
        println()
    }

    private fun reportMove(move: GameCommand?) {
        val playerID = game.previousPlayer.id
        if (move!!.option == 0)
            println("$playerID has drawn a card")
        else if (move.option == 1) {
            print("$playerID has played a ")
            CardPrinter.printCard(game.getTableTop())
            println()
            if (game.getTableTop().type == CardType.WILD) {
                println("and has chosen ${move.colour}")
            }
        }
        println()
    }

    private fun printNumberOfCardsEachRivalPlayerHas() {
        val numberOfPlayers = game.getNumberOfPlayers()
        val card = "card"
        val cards = "cards"
        for (i in 0 until numberOfPlayers) {
            val player = game.getPlayer(i)
            if (player.id != humanPlayerName) {
                val hand = player.hand
                if (hand.size > 1)
                    println("${player.id} -> ${player.hand.size} $cards left")
                else
                    println("${player.id} -> ${player.hand.size} $card left")
                println()
            }
        }
    }

    private fun warnIfRivalIsAboutToWin() {
        val previousPlayer = game.previousPlayer
        if (previousPlayer.hand.size == 1 && previousPlayer.id != humanPlayerName)
            println("CAREFUL: ${previousPlayer.id} has only one card left\n")
    }

    private fun restart() {
        game = Game.instance(askForNumberOfPlayers() - 1, humanPlayerName)
        start()
    }

    private fun addBotThinkingDelay() {
        try {
            Thread.sleep((Random().nextInt(4) - 2 + 2) * 1000.toLong())
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}