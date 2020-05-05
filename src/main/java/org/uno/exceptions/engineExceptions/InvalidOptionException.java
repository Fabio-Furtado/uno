package org.uno.exceptions.engineExceptions;

/**
 * @author Fábio Furtado
 */
public class InvalidOptionException extends EngineException {

    public InvalidOptionException() {
    }

    public InvalidOptionException(String detailMessage) {
        super(detailMessage);
    }
}
