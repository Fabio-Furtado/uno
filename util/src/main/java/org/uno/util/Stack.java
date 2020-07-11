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

import java.util.Iterator;


/**
 * @author Fábio Furtado
 * @see org.uno.util.MutableStack
 */
public class Stack<E> implements org.uno.util.MutableStack<E>, Iterable<E> {

    private SingleLinkNode<E> top;
    private int size;

    /**
     * Create a new empty Stack
     */
    public Stack() {
        this.top = null;
        this.size = 0;
    }

    /**
     * @see org.uno.util.MutableStack#push(Object)
     */
    @Override
    public void push(E element) {
        top = new SingleLinkNode<>(element, top);
        size++;
    }

    /**
     * @see org.uno.util.MutableStack#pop()
     */
    @Override
    public E pop() {
        if (isEmpty())
            throw new EmptyStackException();
        SingleLinkNode<E> oldTop = top;
        top = top.next();
        size--;
        return oldTop.getNodeContent();
    }

    /**
     * @see org.uno.util.MutableStack#peek()
     */
    @Override
    public E peek() {
        if (isEmpty())
            throw new EmptyStackException();
        return top.getNodeContent();
    }

    /**
     * @see org.uno.util.MutableStack#size()
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * @see MutableStack#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return top == null;
    }


    /**
     * @see MutableStack#toArray()
     */
    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        int i = size - 1;
        for (E element : this) {
            arr[i] = element;
            i--;
        }
        return arr;
    }

    /**
     * @see MutableStack#clone()
     */
    @Override
    public Stack<E> clone() {
        if (isEmpty())
            return new Stack<>();

        SingleLinkNode<E> next = top;
        Stack<E> aux = new Stack<>();
        Stack<E> clone = new Stack<>();
        while(next != null) {
            aux.push(next.getNodeContent());
            next = next.next();
        }
        while(!aux.isEmpty())
            clone.push(aux.pop());
        return clone;
    }

    @Override
    public Iterator<E> iterator() {
        return new StackIterator();
    }

    /**
     * Iterator for this class.
     * <p> The element at the top is the first iteration and the element at the
     * bottom the last.
     */
    private class StackIterator implements Iterator<E> {

        private SingleLinkNode<E> next;

        /**
         * Creates an instance.
         */
        private StackIterator() {
            this.next = top;
        }

        /**
         * @see Iterator#hasNext()
         */
        @Override
        public boolean hasNext() {
            return next != null;
        }

        /**
         * @see Iterator#next()
         */
        @Override
        public E next() {
            E returnValue = next.getNodeContent();
            next = next.next();
            return returnValue;
        }
    }
}
