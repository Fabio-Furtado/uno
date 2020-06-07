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

package org.uno.util;

/**
 * A a collection of objects that are inserted and removed according to the
 * last-in first-out principle.
 *
 * @param <E> data type which this Stack will work with
 * @author Fábio Furtado
 */
interface MutableStack<E> {

    /**
     * Adds element to the top of the stack.
     *
     * @param element data to be added
     */
    void push(E element);

    /**
     * Removes and returns the element at the top of the Stack.
     *
     * @return element at the top
     */
    E pop();

    /**
     * Returns the element at the top of the Stack.
     *
     * @return element at the top
     */
    E peek();

    /**
     * Returns the current size of the stack.
     *
     * @return int with the stack's size
     */
    int size();

    /**
     * Checks if the Stack is empty.
     *
     * @return true if the Stack is empty, false otherwise
     */
    boolean isEmpty();

    /**
     * Returns a String representation of the Stack.
     */
    String toString();

    /**
     * Returns a clone of this instance.
     * 
     * @return clone of this instance
     */
    MutableStack<E> clone();
}
