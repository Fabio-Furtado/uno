package org.uno.exceptions.engineExceptions;

/**
 * Thrown when a invalid game command is passed to the engine for execution.
 *
 * @author Fábio Furtado
 */
public class EngineException extends Exception {

    public EngineException() {
        super();
    }

    public EngineException(String detailMessage) {
        super(detailMessage);
    }
}
