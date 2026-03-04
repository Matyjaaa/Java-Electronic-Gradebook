/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.lab5.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Unit tests for the Grade class.
 * Covers:
 * - Valid instantiation (boundaries included),
 * - Invalid instantiation (exceptions),
 * - Parameterized tests for all scenarios.
 *
 * @author Bartosz Matyjaszewski
 * @version 6.0
 */
public class GradeTest {

    /**
     * Parameterized test: Valid creation of a Grade object.
     * Covers boundary values for grades (1.0 and 6.0).
     *
     * @param subjectId   id of the subject
     * @param subjectName name of the subject
     * @param value       grade value
     */
    @ParameterizedTest
    @CsvSource({
            "1, Math, 1.0",
            "2, Phys, 3.5",
            "3, Chem, 6.0"
    })
    void testConstructorValid(String subjectId, String subjectName, double value) {
        // Arrange
        Subject subject = new Subject(subjectId, subjectName);

        // Act
        Grade grade = new Grade(subject, value);

        // Assert
        assertAll("Verify grade properties",
                () -> assertEquals(subject.name(), grade.getSubjectName()),
                () -> assertEquals(value, grade.getGradeValue())
        );
    }

    /**
     * Parameterized test: Invalid data should throw IllegalArgumentException.
     * Tests null subject and grade values outside the range [1.0, 6.0].
     * Uses 'NULL' token in CSV to represent actual null.
     *
     * @param subjectName name of the subject (or NULL)
     * @param value       grade value
     */
    @ParameterizedTest
    @CsvSource(nullValues = "NULL", value = {
            "NULL, 5.0",
            "Math, 0.9",
            "Math, 0.0",
            "Math, 6.1",
            "Math, 10.0"
    })
    void testConstructorInvalid(String subjectName, double value) {
        Subject subject = (subjectName == null) ? null : new Subject("1", subjectName);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Grade(subject, value)
        );

        assertNotNull(exception.getMessage());
    }

    /**
     * Parameterized test: Verify correct string formatting.
     *
     * @param subjectId   subject ID
     * @param subjectName subject name
     * @param value       grade value
     * @param expected    expected string result
     */
    @ParameterizedTest
    @CsvSource({
            "1, Matematyka, 5.0, Matematyka: 5.0",
            "2, Fizyka, 4.5, Fizyka: 4.5",
            "3, Chemia, 3.0, Chemia: 3.0"
    })
    void testToString(String subjectId, String subjectName, double value, String expected) {
        Grade grade = new Grade(new Subject(subjectId, subjectName), value);
        assertEquals(expected, grade.toString());
    }

    /**
     * Parameterized test: Verify equals() for different combinations of objects.
     *
     * @param id1           ID of first subject
     * @param name1         Name of first subject
     * @param value1        Value of first grade
     * @param id2           ID of second subject
     * @param name2         Name of second subject
     * @param value2        Value of second grade
     * @param expectedEqual Expected result
     */
    @ParameterizedTest
    @CsvSource({
            "1, Matematyka, 5.0,   1, Matematyka, 5.0,   true",  // Identical
            "1, Matematyka, 5.0,   1, Fizyka, 5.0,   false", // Different Subject Name
            "1, Matematyka, 4.0,   1, Matematyka, 5.0,   false"  // Different Grade Value
    })
    void testEqualsLogic(String id1, String name1, double value1,
                         String id2, String name2, double value2,
                         boolean expectedEqual) {

        Grade g1 = new Grade(new Subject(id1, name1), value1);
        Grade g2 = new Grade(new Subject(id2, name2), value2);

        if (expectedEqual) {
            assertEquals(g1, g2);
        } else {
            assertNotEquals(g1, g2);
        }
    }


    /**
     * Parameterized test: Objects that are equal must have the same hashCode.
     *
     * @param subjectId   subject ID
     * @param subjectName subject name
     * @param value       grade value
     */
    @ParameterizedTest
    @CsvSource({
            "1, Matematyka, 5.0",
            "2, Fizyka, 4.0",
            "3, Chemia, 3.5"
    })
    void testHashCode(String subjectId, String subjectName, double value) {
        Grade g1 = new Grade(new Subject(subjectId, subjectName), value);
        Grade g2 = new Grade(new Subject(subjectId, subjectName), value);

        assertEquals(g1.hashCode(), g2.hashCode());
    }
}
