/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.lab5.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Unit tests for the {@link Subject} record class.
 * <p>
 * This test class covers the public methods inherited from {@link Object} (toString, equals, hashCode).
 * Constructors, accessors, and mutators are excluded from testing as per requirements.
 * </p>
 *
 * @author Bartosz Matyjaszewski
 * @version 6.0
 */
public class SubjectTest {


    /**
     * Parameterized test: Verifies that {@code toString()} returns the correctly formatted string.
     * Checks if the output matches the pattern "ID: [id] | [name]".
     *
     * @param id       the subject ID (must be digits)
     * @param name     the subject name
     * @param expected the expected string representation
     */
    @ParameterizedTest
    @CsvSource({
            "1, Matematyka, ID: 1 | Matematyka",
            "2, Fizyka, ID: 2 | Fizyka",
            "123, Test, ID: 123 | Test"
    })
    void testToString(String id, String name, String expected) {
        Subject subject = new Subject(id, name);

        assertEquals(expected, subject.toString());
    }

    /**
     * Parameterized test: Verifies {@code equals()} logic for various combinations of identical and different objects.
     * Ensures that objects with the same ID and name are considered equal, while any difference results in inequality.
     *
     * @param id1           ID of the first subject
     * @param name1         Name of the first subject
     * @param id2           ID of the second subject
     * @param name2         Name of the second subject
     * @param expectedEqual true if objects should be equal, false otherwise
     */
    @ParameterizedTest
    @CsvSource({
            "1, Matematyka, 1, Matematyka, true",
            "1, Matematyka, 2, Matematyka, false",
            "1, Matematyka, 1, Fizyka, false",
            "10, Biologia, 10, Chemia, false"
    })
    void testEquals(String id1, String name1, String id2, String name2, boolean expectedEqual) {

        Subject s1 = new Subject(id1, name1);
        Subject s2 = new Subject(id2, name2);

        if (expectedEqual) {
            assertEquals(s1, s2);
        } else {
            assertNotEquals(s1, s2);
        }
    }

    /**
     * Parameterized test: Verifies boundary cases for {@code equals()}.
     * Checks comparison against {@code null} and objects of different types (String, Integer).
     *
     * @param otherObject the object to compare against the Subject instance
     */
    @ParameterizedTest(name = "equals() with {0} → should be false")
    @CsvSource({
        "null",
        "other"
    })
    void testEqualsWithDifferentTypeOrNull(String scenario) {
        Subject subject = new Subject("1", "Matematyka");

        Object other =
                scenario.equals("null") ? null : "not a subject";

        assertNotEquals(subject, other);
    }

    /**
     * Parameterized test: Verifies that {@code hashCode()} is consistent with {@code equals()}.
     * Two objects that are equal according to {@code equals()} must have the same hash code.
     *
     * @param id   the subject ID (digits only)
     * @param name the subject name
     */
    @ParameterizedTest
    @CsvSource({
            "1, Matematyka",
            "2, Fizyka",
            "99, Y"
    })
    void testHashCode(String id, String name) {
        Subject s1 = new Subject(id, name);
        Subject s2 = new Subject(id, name);

        assertEquals(s1.hashCode(), s2.hashCode());
    }
}
