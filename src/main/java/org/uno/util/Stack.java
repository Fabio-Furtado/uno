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

import org.uno.exceptions.EmptyStackException;

/**
 * @see org.uno.util.MutableStack
 *
 * @author Fábio Furtado
 */
public class Stack<E> implements org.uno.util.MutableStack<E> {

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
}
