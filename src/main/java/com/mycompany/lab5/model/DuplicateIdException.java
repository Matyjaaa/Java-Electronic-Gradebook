
package com.mycompany.lab5.model;

/**
 * Exception thrown when attempting to add a student or subject with a duplicate ID.
 * This ensures that each student or subject in the journal has a unique identifier.
 * @author Bartosz Matyjaszewski
 * @version 6.0
 */
public class DuplicateIdException extends Exception {
    /**
     * Constructs a new DuplicateIdException with the specified detail message.
     *
     * @param message the detail message describing the exception
     */
    public DuplicateIdException(String message) {
        super(message);
    }
}
