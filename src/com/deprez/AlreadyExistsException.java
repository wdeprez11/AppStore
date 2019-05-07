package com.deprez;

/**
 * Exception to be throw if an item was found within a unique list.
 */
public class AlreadyExistsException extends Exception {
    /**
     * Constructor for an AlreadyExistsException.
     *
     * @param message The message to send to the exception class
     */
    public AlreadyExistsException(String message) {
        super(message);
    }
}
