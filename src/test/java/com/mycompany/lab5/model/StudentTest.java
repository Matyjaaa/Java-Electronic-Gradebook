/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.lab5.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Final set of unit tests for the Student class.
 * Covers:
 * - valid scenarios,
 * - invalid scenarios forcing exceptions,
 * - edge cases (excluding simple type boundaries),
 * - fully parameterized tests,
 * - tests for all public methods except constructors and accessors/mutators.
 *
 * @author Bartosz Matyjaszewski
 * @version 6.0
 */
public class StudentTest {

    /**
     * Parameterized test: Successfully adds a grade to the student.
     *
     * @param subjectId   ID of the subject
     * @param subjectName Name of the subject
     * @param value       Value of the grade
     */
    @ParameterizedTest
    @CsvSource({
            "1, Matematyka, 5.0",
            "2, Fizyka, 3.5",
            "3, Chemia, 4.0"
    })
    void testAddGradeSuccess(String subjectId, String subjectName, double value) {
        Student s = new Student("1", "Adam", "Kowalski");
        Subject subject = new Subject(subjectId, subjectName);

        s.addGrade(new Grade(subject, value));

        assertEquals(1, s.getGrades().size());
        assertEquals(value, s.getGrades().get(0).getGradeValue());
    }

    /**
     * Invalid test: attempting to add null should throw an exception.
     */
    @Test
    void testAddGradeNullThrowsException() {
        Student s = new Student("1", "Adam", "Kowalski");
        assertThrows(IllegalArgumentException.class, () -> s.addGrade(null));
    }

    /**
     * Parameterized test: Successfully removes an existing grade.
     *
     * @param subjectId ID of the subject
     * @param name      Name of the subject
     * @param value     Value of the grade to remove
     */
    @ParameterizedTest
    @CsvSource({
            "1, Matematyka, 5.0",
            "2, Fizyka, 3.0"
    })
    void testRemoveSingleGradeSuccess(String subjectId, String name, double value) {
        Student s = new Student("1", "Adam", "Kowalski");
        Subject subject = new Subject(subjectId, name);

        s.addGrade(new Grade(subject, value));

        boolean removed = s.removeSingleGrade(subject, value);

        assertTrue(removed);
        assertTrue(s.getGrades().isEmpty());
    }

    /**
     * Parameterized test: Verifies failure when trying to remove a grade from an empty list
     * or a grade that does not exist.
     *
     * @param subjectName subject name
     * @param valToRemove value to remove
     */
    @ParameterizedTest
    @CsvSource({
            "Matematyka, 5.0",
            "Fizyka, 2.0"
    })
    void testRemoveSingleGradeFail(String subjectName, double valToRemove) {
        Student s = new Student("1", "Adam", "Kowalski");
        Subject subject = new Subject("1", subjectName);
        
        assertFalse(s.removeSingleGrade(subject, valToRemove));
    }

    /**
     * Parameterized test: Boundary case - Multiple identical grades.
     * Only the first matching grade should be removed.
     *
     * @param value the grade value to add twice and remove once
     */
    @ParameterizedTest
    @ValueSource(doubles = {5.0, 3.5})
    void testRemoveSingleGradeMultiple(double value) {
        Student s = new Student("1", "Adam", "Kowalski");
        Subject math = new Subject("1", "Math");

        s.addGrade(new Grade(math, value));
        s.addGrade(new Grade(math, value));

        boolean removed = s.removeSingleGrade(math, value);

        assertTrue(removed);
        assertEquals(1, s.getGrades().size(), "Should have 1 grade left");
    }

    /**
     * Parameterized test: Successfully edits an existing grade.
     *
     * @param subjectId subject ID
     * @param name      subject name
     * @param oldVal    current grade value
     * @param newVal    new grade value
     */
    @ParameterizedTest
    @CsvSource({
            "1, Matematyka, 5.0, 4.0",
            "2, Fizyka, 3.0, 2.5"
    })
    void testEditGradeSuccess(String subjectId, String name, double oldVal, double newVal) {
        Student s = new Student("1", "Adam", "Kowalski");
        Subject subject = new Subject(subjectId, name);

        s.addGrade(new Grade(subject, oldVal));

        boolean result = s.editGrade(subject, oldVal, newVal);

        assertTrue(result);
        assertEquals(newVal, s.getGrades().get(0).getGradeValue());
    }

    /**
     * Parameterized test: Attempt to edit a non-existent grade should fail.
     *
     * @param targetVal value that doesn't exist in the list
     */
    @ParameterizedTest
    @ValueSource(doubles = {5.0, 2.0})
    void testEditGradeFail(double targetVal) {
        Student s = new Student("1", "Adam", "Kowalski");
        Subject math = new Subject("1", "Matematyka");

        assertFalse(s.editGrade(math, targetVal, 3.0));
    }

    /**
     * Parameterized test: Boundary case - Multiple identical grades.
     * Only the first matching grade should be edited.
     *
     * @param initialVal value to be added twice
     * @param targetVal  new value for the first occurrence
     */
    @ParameterizedTest
    @CsvSource({
            "5.0, 4.0",
            "3.0, 2.0"
    })
    void testEditGradeMultiple(double initialVal, double targetVal) {
        Student s = new Student("1", "Adam", "Kowalski");
        Subject math = new Subject("1", "Math");

        s.addGrade(new Grade(math, initialVal));
        s.addGrade(new Grade(math, initialVal));

        boolean result = s.editGrade(math, initialVal, targetVal);

        assertTrue(result);
        assertEquals(targetVal, s.getGrades().get(0).getGradeValue(), "First grade should be updated");
        assertEquals(initialVal, s.getGrades().get(1).getGradeValue(), "Second grade should remain unchanged");
    }

    /**
     * Parameterized test: Verifies the string representation generated by toString().
     * Uses a custom delimiter '|' because the expected string output contains commas.
     *
     * @param id       student ID
     * @param first    first name
     * @param last     last name
     * @param expected expected output string
     */
    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
        "1 | Adam | Kowalski | Student(id=1, firstName=Adam, lastName=Kowalski)",
        "2 | Ewa  | Nowak    | Student(id=2, firstName=Ewa, lastName=Nowak)",
        "3 | Jan  | Kwiatkowski | Student(id=3, firstName=Jan, lastName=Kwiatkowski)"
    })
    void testToString(String id, String first, String last, String expected) {
        Student s = new Student(id, first, last);
        
        assertEquals(expected, s.toString());
    }
}
