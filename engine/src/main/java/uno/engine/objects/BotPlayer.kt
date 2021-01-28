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
package uno.engine.objects

import uno.engine.CardColour
import uno.engine.CardType
import uno.engine.GameCommand
import uno.engine.UnoGame
import kotlin.random.Random

/**
 * An abstraction for a bot player.
 *
 * @constructor creates an instance with the given id and starting hand
 * @author Fábio Furtado
 */
class BotPlayer private constructor(_id: String,
                                    _hand: List<Card>) : Player, Bot {

    /**
     * @see Player.id
     */
    override val id = _id

    /**
     * An ADT with the cards on the player's hand.
     */
    override val hand: MutableList<Card> = _hand.toMutableList()

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
                return GameCommand.of(index, colour)
            }
        }
        return GameCommand.of()
    }

    private fun chooseColour(): CardColour {
        var bluesInHand = 0
        var redsInHand = 0
        var greensInHand = 0
        var yellowsInHand = 0
        for (card in hand) {
            if (card is Colourful) {
                when ((card as Colourful).colour) {
                    CardColour.BLUE -> bluesInHand++
                    CardColour.RED -> redsInHand++
                    CardColour.GREEN -> greensInHand++
                    CardColour.YELLOW -> yellowsInHand++
                }
            }
        }

        val arr = intArrayOf(bluesInHand, redsInHand, greensInHand, yellowsInHand)
        var biggerIndex = Random.nextInt(4)
        for ((index, value) in arr.withIndex()) {
            if (value > arr[biggerIndex])
                biggerIndex = index
        }
        return when (biggerIndex) {
            0 -> CardColour.BLUE
            1 -> CardColour.RED
            2 -> CardColour.GREEN
            else -> CardColour.YELLOW
        }
    }

    /**
     * @see Player.clone
     */
    override fun clone(): Player {
        val handCopy: MutableList<Card> = ArrayList(hand.size)
        for (card in hand) handCopy.add(card)
        return BotPlayer(id, handCopy)
    }

    /**
     * @see Object.equals
     */
    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other == null || this.javaClass.kotlin != other.javaClass.kotlin -> false
            this.id == (other as Player).id-> true
            else -> false
        }
    }

    /**
     * @see Object.hashCode
     */
    override fun hashCode() = Player.hashCode(this)
}
