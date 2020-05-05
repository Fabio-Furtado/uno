package org.uno.engine;

import org.uno.enums.CardColour;

/**
 * @author Fábio Furtado
 */
public class GameCommand {
    
    private final int option;
    private final int index;
    private final CardColour colour;

    public GameCommand(int option, int index, CardColour colour) {
        this.option = option;
        this.index = index;
        this.colour = colour;
    }

    public GameCommand(int option) {
        this.option = option;
        this.index = -1;
        this.colour = null;
    }

    public GameCommand(int option, int index) {
        this.option = option;
        this.index = index;
        this.colour = null;
    }

    public int getOption() {
        return option;
    }

    public int getIndex() {
        return index;
    }

    public CardColour getColour() {
        return colour;
    }
}
