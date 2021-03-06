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
package uno.cli

import uno.engine.CardColour
import uno.engine.GameCommand

/**
 * Translates cli commands into engine commands.
 *
 * @author Fábio Furtado
 */
internal object CLItoEngineCommandConverter {
    const val unknownCommandErrorMessage = "Unknown command: %s"
    const val notIndexErrorMessage = "Expected an index after the <%s> command"
    const val insufficientArgumentsErrorMessage = "Not enough arguments for the <%s> command"
    const val tooManyArgumentsErrorMessage = "Too many arguments for the <%s> command"

    @Throws(CommandFormatException::class)
    fun convert(cliCommand: Array<String>): GameCommand {
        return if (cliCommand[0] == CommandValuesKeeper.getValue(Command.DRAW)) {
            if (cliCommand.size > 1) throw CommandFormatException(String.format(tooManyArgumentsErrorMessage, cliCommand[0]))
            GameCommand.of()
        } else {
            doIfLikelyPlayingFromHand(cliCommand)
        }
    }

    @Throws(CommandFormatException::class)
    private fun doIfLikelyPlayingFromHand(cliCommand: Array<String>): GameCommand {
        var colour: CardColour? = null
        val index: Int = if (cliCommand[0] == CommandValuesKeeper.getValue(Command.PLAY_A_CARD)) {
            try {
                cliCommand[1].toInt() - 1
            } catch (e: NumberFormatException) {
                throw CommandFormatException(String.format(notIndexErrorMessage,
                        cliCommand[0]))
            } catch (e: ArrayIndexOutOfBoundsException) {
                throw CommandFormatException(String.format(
                        insufficientArgumentsErrorMessage, cliCommand[0]))
            }
        } else {
            throw CommandFormatException(String.format(
                    unknownCommandErrorMessage, cliCommand[0]))
        }
        if (cliCommand.size == 3) {
            var engineCardColourCode: CardColour? = null
            when {
                cliCommand[2] == CommandValuesKeeper.getValue(CardColour.BLUE) ->
                    engineCardColourCode = CardColour.BLUE
                cliCommand[2] == CommandValuesKeeper.getValue(CardColour.RED) ->
                    engineCardColourCode = CardColour.RED
                cliCommand[2] == CommandValuesKeeper.getValue(CardColour.GREEN) ->
                    engineCardColourCode = CardColour.GREEN
                cliCommand[2] == CommandValuesKeeper.getValue(CardColour.YELLOW) ->
                    engineCardColourCode = CardColour.YELLOW
            }
            if (engineCardColourCode != null)
                colour = engineCardColourCode
        } else if (cliCommand.size > 3) throw CommandFormatException(String.format(
                tooManyArgumentsErrorMessage, cliCommand[0]))
        return GameCommand.of(index, colour)
    }
}