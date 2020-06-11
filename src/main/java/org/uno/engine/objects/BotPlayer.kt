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
package org.uno.engine.objects

import org.uno.engine.CardColour
import org.uno.engine.CardType
import org.uno.engine.GameCommand
import org.uno.engine.UnoGame
import kotlin.random.Random

/**
 * An abstraction for a bot player.
 *
 * @author Fábio Furtado
 */
class BotPlayer private constructor(_id: String, _hand: MutableList<Card>) : Player, Bot {

    /**
     * @see Player.id
     */
    override val id = _id

    /**
     * An ADT with the cards on the player's hand.
     */
    override val hand: MutableList<Card> = _hand

    /**
     * Creates a new instance.
     *
     * @param id a String to identify this player
     */
    constructor(id: String): this(id, ArrayList())

    /**
     * @see Player.addToHand
     */
    override fun addToHand(card: Card) {
        hand.add(card)
    }

    /**
     * @see Player.takeFromHand
     */
    override fun takeFromHand(index: Int): Card {
        val returnValue = hand[index]
        hand.removeAt(index)
        return returnValue
    }

    /**
     * @see Bot.makeMove
     */
    override fun makeMove(game: UnoGame): GameCommand {
        val index: Int
        var colour: CardColour? = null
        for (i in hand.indices) {
            if (game.isCardValid(hand[i])) {
                index = i
                if (hand[i].type === CardType.WILD) colour = chooseColour()
                return GameCommand(index, colour)
            }
        }
        return GameCommand()
    }

    private fun chooseColour(): CardColour {
        var bluesInHand = 0
        var redsInHand = 0
        var greensInHand = 0
        var yellowsInHand = 0
        for (card in hand) {
            if (card is Colourful) {
                when ((card as Colourful).colour) {
                    CardColour.BLUE -> {
                        bluesInHand++
                        redsInHand++
                        greensInHand++
                        yellowsInHand++
                    }
                    CardColour.RED -> {
                        redsInHand++
                        greensInHand++
                        yellowsInHand++
                    }
                    CardColour.GREEN -> {
                        greensInHand++
                        yellowsInHand++
                    }
                    CardColour.YELLOW -> yellowsInHand++
                }
            }
        }
        var chosenColour = pickRandomColour()
        val arr = intArrayOf(bluesInHand, redsInHand, greensInHand, yellowsInHand)
        var biggerValue = arr[Random.nextInt(4)]
        if (bluesInHand > biggerValue) {
            biggerValue = bluesInHand
            chosenColour = CardColour.BLUE
        }
        if (redsInHand > biggerValue) {
            biggerValue = redsInHand
            chosenColour = CardColour.RED
        }
        if (greensInHand > biggerValue) {
            biggerValue = greensInHand
            chosenColour = CardColour.GREEN
        }
        if (yellowsInHand > biggerValue) {
            biggerValue = yellowsInHand
            chosenColour = CardColour.YELLOW
        }
        return chosenColour
    }

    private fun pickRandomColour(): CardColour {
        val colours = CardColour.values()
        return colours[Random.nextInt(colours.size)]
    }

    /**
     * @see Player.clone
     */
    override fun clone(): Player {
        val handCopy: MutableList<Card> = ArrayList(hand.size)
        for (card in hand) handCopy.add(card.clone())
        return BotPlayer(id, handCopy)
    }

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other == null || this.javaClass.kotlin != other.javaClass.kotlin -> false
            this.id == (other as Player).id && this.hand == other.hand -> true
            else -> false
        }
    }
}
