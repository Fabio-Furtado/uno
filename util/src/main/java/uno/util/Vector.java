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

package uno.util;

import java.util.Random;


/**
 * @param <E> data type
 * @author Fábio Furtado
 * @deprecated This class is just awful and bad code
 */
@Deprecated
public class Vector<E> {

    private static final int DEFAULT_SIZE = 0;
    private int length;
    private Object[] values;

    public Vector(int length) {
        this.length = length;
        this.values = new Object[length];
    }

    public Vector() {
        this(DEFAULT_SIZE);
    }

    /**
     * Adds a new element at the given index.
     *
     * @param index index where to put the element
     * @param e     element to be inserted
     */
    public void add(int index, E e) {
        incrementSize(index);
        values[index] = e;
    }

    /**
     * Increments the size of the vector and adds a new element at the end of the
     * vector.
     *
     * @param e element to be inserted
     */
    public void add(E e) {
        add(length, e);
    }

    /**
     * Removes the element at the given index from the vector and decrements its
     * size.
     *
     * @param index index of the element to be removed
     */
    public void remove(int index) {
        decrementSize(index);
    }

    /**
     * Removes the last element from the vector and decrements its size..
     */
    public void remove() {
        remove(length - 1);
    }

    public void replace(int index, E e) {
        values[index] = e;
    }

    /**
     * Increments the size of the vector adding an empty entry at the given index.
     */
    private void incrementSize(int index) {
        Object[] aux = new Object[length];
        System.arraycopy(values, 0, aux, 0, length);
        length++;
        values = new Object[length];
        System.arraycopy(aux, 0, values, 0, index);
        System.arraycopy(aux, index, values, index + 1, length - (index + 1));
    }

    /**
     * Increments the size of the vector adding an empty entry at the end of it.
     */
    private void incrementSize() {
        if (isEmpty())
            incrementSize(0);
        else
            incrementSize(length - 1);
    }

    /**
     * Decrements the size of the array removing the element at the given index.
     *
     * @param indexToRemove index of the element witch will be removed
     */
    private void decrementSize(int indexToRemove) {
        Object[] aux = new Object[length];
        System.arraycopy(values, 0, aux, 0, length);
        length--;
        values = new Object[length];
        System.arraycopy(aux, 0, values, 0, indexToRemove);
        System.arraycopy(aux, indexToRemove + 1, values, indexToRemove, aux.length - indexToRemove - 1);
    }

    /**
     * Returns the current sized of the vector.
     */
    public int length() {
        return this.length;
    }

    /**
     * Returns the value at the given index.
     *
     * @param index index of the value
     * @return value found at the given index
     */
    public E get(int index) {
        return (E) values[index];
    }

    /**
     * Clears the vector setting its size to 0.
     */
    public void clear() {
        this.length = 0;
        this.values = null;
    }

    /**
     * Checks if the vector is empty.
     *
     * @return true if it is, false if not
     */
    public boolean isEmpty() {
        return this.values == null || this.length == 0;
    }

    /**
     * Shuffles all the values in the vector.
     */
    public void shuffle() {
        for (int i = 0; i < length; i++) {
            Object tmp = values[i];
            int otherIndex = new Random().nextInt(length);
            values[i] = values[otherIndex];
            values[otherIndex] = tmp;
        }
    }

    /**
     * Returns a clone of this vector.
     *
     * @return vector with the clone
     */
    public Vector clone() {
        Vector<E> clone = new Vector<>();
        for (int i = 0; i < length; i++) {
            clone.add(i, (E) values[i]);
        }
        return clone;
    }

    /**
     * Returns an array with all the elements in the vector in the same order.
     *
     * @return array with the elements
     */
    public Object[] toArray() {
        Object[] copy = new Object[length];
        System.arraycopy(values, 0, copy, 0, length);
        return copy;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.length; i++)
            sb.append(values[i] + "\n");
        return sb.toString();
    }
}
