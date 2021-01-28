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

/**
 * An Abstraction for a human player.
 *
 * @constructor creates an instance with the given `id` and starting `hand`
 * @author Fábio Furtado
 */
class HumanPlayer(_id: String, _hand: List<Card>): Player {

    /**
     * @see Player.id
     */
    override val id: String = _id

    /**
     * This player's cards
     */
    override val hand: MutableList<Card> = _hand.toMutableList()

    /**
     * Creates a player with an empty hand.
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
     * @see Object.equals
     */
    override fun equals(other: Any?) =
        when {
            this === other -> true
            other == null || this.javaClass.kotlin != other.javaClass.kotlin -> false
            this.id == (other as Player).id -> true
            else -> false
        }

    /**
     * @see Player.clone
     */
    override fun clone(): Player {
        val handCopy = ArrayList<Card>(hand.size)
        for (card in hand) handCopy.add(card)
        return HumanPlayer(id, handCopy)
    }

    /**
     * @see Object.hashCode
     */
    override fun hashCode() = Player.hashCode(this)
}