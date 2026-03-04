/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.lab5.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Unit tests for DuplicateIdException.
 * <p>
 * Note: According to the requirements "exclude constructors", strictly speaking,
 * this class might not require testing as it only contains a constructor.
 * However, these tests represent a sanity check for message propagation inheritance.
 * </p>
 * @author Bartosz Matyjaszewski
 * @version 6.0
 */
public class DuplicateIdExceptionTest {

    /**
     * Parameterized test: Verifies that the exception correctly stores and returns the message.
     * Covers valid strings, empty strings, and null (boundary conditions).
     *
     * @param message the message passed to the exception constructor
     */
    @ParameterizedTest
    @ValueSource(strings = {
            "Student with ID 1 already exists!",
            "Subject duplication detected",
            ""
    })
    @NullSource
    void testExceptionMessagePropagation(String message) {
        DuplicateIdException exception = new DuplicateIdException(message);
        assertEquals(message, exception.getMessage());
    }
}
