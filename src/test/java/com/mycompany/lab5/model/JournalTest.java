package com.mycompany.lab5.model;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Journal class.
 * Covers:
 * - valid scenarios,
 * - exception throwing (DuplicateIdException, IllegalArgumentException),
 * - edge cases (no elements, null as argument).
 *
 * @author Bartosz Matyjaszewski
 * @version 6.0
 */
public class JournalTest {

    /**
     * Parameterized test: valid addition of students.
     *
     * @param id        the student ID
     * @param firstName the student's first name
     * @param lastName  the student's last name
     * @throws Exception if an error occurs
     */
    @ParameterizedTest
    @CsvSource({
            "1, Adam, Kowalski",
            "2, Ewa, Nowak"
    })
    void testAddStudentValid(String id, String firstName, String lastName) throws Exception {
        Journal journal = new Journal();
        Student s = new Student(id, firstName, lastName);
        
        journal.addStudent(s);

        assertEquals(1, journal.getStudents().size());
        assertEquals(s, journal.getStudents().get(0));
    }

    /**
     * Parameterized test: adding a student with a duplicate ID must throw an exception.
     *
     * @param id1    ID of the first student
     * @param first1 First name of the first student
     * @param last1  Last name of the first student
     * @param id2    ID of the second student (duplicate)
     * @param first2 First name of the second student
     * @param last2  Last name of the second student
     * @throws DuplicateIdException expected exception
     */
    @ParameterizedTest
    @CsvSource({
            "1, Adam, Kowalski, 1, Jan, Nowak",
            "2, Ewa, Nowak,     2, Marek, Kowal"
    })
    void testAddStudentDuplicate(String id1, String first1, String last1,
                                 String id2, String first2, String last2) throws DuplicateIdException {
        Journal journal = new Journal();
        journal.addStudent(new Student(id1, first1, last1));

        DuplicateIdException ex = assertThrows(
                DuplicateIdException.class,
                () -> journal.addStudent(new Student(id2, first2, last2))
        );

        assertTrue(ex.getMessage().contains(id2));
    }

    /**
     * Parameterized test: attempt to add null as a student.
     * Uses @NullSource to pass null to the parameterized method.
     *
     * @param student the student object (null)
     */
    @ParameterizedTest
    @NullSource
    void testAddStudentNull(Student student) {
        Journal journal = new Journal();
        
        assertThrows(IllegalArgumentException.class, 
                () -> journal.addStudent(student));
    }


    /**
     * Parameterized test: valid addition of subjects.
     *
     * @param id   the subject ID
     * @param name the subject name
     * @throws Exception if an error occurs
     */
    @ParameterizedTest
    @CsvSource({
            "1, Matematyka",
            "2, Fizyka"
    })
    void testAddSubjectValid(String id, String name) throws Exception {
        Journal journal = new Journal();
        Subject subject = new Subject(id, name);
        
        journal.addSubject(subject);

        assertEquals(1, journal.getSubjects().size());
        assertEquals(subject, journal.getSubjects().get(0));
    }

    /**
     * Parameterized test: adding a subject with a duplicate ID must throw an exception.
     *
     * @param id1   ID of the first subject
     * @param name1 Name of the first subject
     * @param id2   ID of the second subject (duplicate)
     * @param name2 Name of the second subject
     * @throws DuplicateIdException expected exception
     */
    @ParameterizedTest
    @CsvSource({
            "1, Matematyka, 1, Matematyka 2",
            "2, Fizyka,     2, Biofizyka"
    })
    void testAddSubjectDuplicate(String id1, String name1, String id2, String name2) throws DuplicateIdException {
        Journal journal = new Journal();
        journal.addSubject(new Subject(id1, name1));

        DuplicateIdException ex = assertThrows(
                DuplicateIdException.class,
                () -> journal.addSubject(new Subject(id2, name2))
        );

        assertTrue(ex.getMessage().contains(id2));
    }

    /**
     * Parameterized test: attempt to add null as a subject.
     *
     * @param subject the subject object (null)
     */
    @ParameterizedTest
    @NullSource
    void testAddSubjectNull(Subject subject) {
        Journal journal = new Journal();
        
        assertThrows(IllegalArgumentException.class, 
                () -> journal.addSubject(subject));
    }


    /**
     * Parameterized test: removing a student (existing, non-existing, null).
     * A null value should return false rather than throw an exception (according to model logic).
     *
     * @param id             ID of the initial student
     * @param first          First name of the initial student
     * @param last           Last name of the initial student
     * @param toRemove       ID of the student to remove
     * @param expectedResult expected boolean result of the operation
     * @throws Exception if an error occurs
     */
    @ParameterizedTest
    @CsvSource(nullValues = "NULL", value = {
            "1, Adam, Kowalski, 1, true",
            "1, Adam, Kowalski, 2, false",
            "1, Adam, Kowalski, NULL, false"
    })
    void testRemoveStudent(String id, String first, String last,
                           String toRemove, boolean expectedResult) throws Exception {
        Journal journal = new Journal();
        journal.addStudent(new Student(id, first, last));

        boolean removed = journal.removeStudent(toRemove);
        assertEquals(expectedResult, removed);
    }


    /**
     * Parameterized test: removing a subject (existing, non-existing, null).
     *
     * @param id             ID of the initial subject
     * @param name           Name of the initial subject
     * @param toRemove       ID of the subject to remove
     * @param expectedResult expected boolean result of the operation
     * @throws Exception if an error occurs
     */
    @ParameterizedTest
    @CsvSource(nullValues = "NULL", value = {
            "1, Matematyka, 1, true",
            "1, Matematyka, 2, false",
            "1, Matematyka, NULL, false"
    })
    void testRemoveSubject(String id, String name,
                           String toRemove, boolean expectedResult) throws Exception {
        Journal journal = new Journal();
        journal.addSubject(new Subject(id, name));

        boolean removed = journal.removeSubject(toRemove);
        assertEquals(expectedResult, removed);
    }


    /**
     * Parameterized test: valid editing of student data.
     *
     * @param oldId    original ID
     * @param oldFirst original first name
     * @param oldLast  original last name
     * @param newId    new ID
     * @param newFirst new first name
     * @param newLast  new last name
     * @throws Exception if an error occurs
     */
    @ParameterizedTest
    @CsvSource({
            "1, Adam, Kowalski,    3, Jan, Kowal",
            "2, Ewa, Nowak,        2, Anna, Nowakowska"  // bez zmiany ID
    })
    void testEditStudentValid(String oldId, String oldFirst, String oldLast,
                              String newId, String newFirst, String newLast) throws Exception {
        Journal journal = new Journal();
        journal.addStudent(new Student(oldId, oldFirst, oldLast));

        boolean edited = journal.editStudent(oldId, newId, newFirst, newLast);
        assertTrue(edited);

        Student updated = journal.getStudents().stream()
                .filter(s -> s.getId().equals(newId))
                .findFirst()
                .orElse(null);

        assertNotNull(updated);
        assertEquals(newFirst, updated.getFirstName());
        assertEquals(newLast, updated.getLastName());
    }

    /**
     * Parameterized test: editing a non-existent student should return false.
     *
     * @param oldId ID to search for
     * @param newId new ID
     * @param first new first name
     * @param last  new last name
     * @throws DuplicateIdException if ID duplication occurs (not expected here)
     */
    @ParameterizedTest
    @CsvSource({
            "99, 100, Jan, Nowak"
    })
    void testEditStudentNonExistent(String oldId, String newId, String first, String last) throws DuplicateIdException {
        Journal journal = new Journal();
        boolean result = journal.editStudent(oldId, newId, first, last);
        assertFalse(result);
    }

    /**
     * Parameterized test: attempt to change ID to an existing one must throw an exception.
     *
     * @param id1             ID of first student
     * @param first1          First name of first student
     * @param last1           Last name of first student
     * @param id2             ID of second student
     * @param first2          First name of second student
     * @param last2           Last name of second student
     * @param duplicateTarget the ID target causing duplication
     * @throws Exception if an error occurs
     */
    @ParameterizedTest
    @CsvSource({
            "1, Adam, Kowal, 2, Ewa, Nowak, 1"
    })
    void testEditStudentDuplicateId(String id1, String first1, String last1,
                                    String id2, String first2, String last2,
                                    String duplicateTarget) throws Exception {
        Journal journal = new Journal();
        journal.addStudent(new Student(id1, first1, last1));
        journal.addStudent(new Student(id2, first2, last2));

        assertThrows(DuplicateIdException.class,
                () -> journal.editStudent(id2, duplicateTarget, "X", "Y"));
    }

    /**
     * Parameterized test: passing null in edit arguments must throw IllegalArgumentException.
     * Tests combinations where one of the arguments is null.
     *
     * @param oldId original ID
     * @param newId new ID
     * @param first new first name
     * @param last  new last name
     */
    @ParameterizedTest
    @CsvSource(nullValues = "NULL", value = {
            "NULL, 2, Jan, Kowal",
            "1, NULL, Jan, Kowal",
            "1, 2, NULL, Kowal",
            "1, 2, Jan, NULL"
    })
    void testEditStudentInvalidArgs(String oldId, String newId, String first, String last) {
        Journal journal = new Journal();
        assertThrows(IllegalArgumentException.class, 
                () -> journal.editStudent(oldId, newId, first, last));
    }


    /**
     * Parameterized test: valid subject editing + update of student grades.
     *
     * @param oldId      original subject ID
     * @param oldName    original subject name
     * @param newId      new subject ID
     * @param newName    new subject name
     * @param gradeValue value of the grade to verify update
     * @throws Exception if an error occurs
     */
    @ParameterizedTest
    @CsvSource({
            "1, Matematyka, 3, Analiza, 5.0"
    })
    void testEditSubjectUpdatesGrades(String oldId, String oldName,
                                      String newId, String newName,
                                      double gradeValue) throws Exception {
        Journal journal = new Journal();
        Subject oldSubject = new Subject(oldId, oldName);
        journal.addSubject(oldSubject);

        Student st = new Student("10", "Adam", "Kowal");
        st.addGrade(new Grade(oldSubject, gradeValue));
        journal.addStudent(st);

        boolean edited = journal.editSubject(oldId, newId, newName);
        assertTrue(edited);

        Subject updated = journal.getSubjects().stream()
                .filter(s -> s.id().equals(newId))
                .findFirst()
                .orElse(null);
        assertNotNull(updated);

        Grade g = st.getGrades().get(0);
        assertEquals(newName, g.getSubjectName());
    }

    /**
     * Parameterized test: duplicate ID during subject editing must throw an exception.
     *
     * @param id1             ID of first subject
     * @param name1           Name of first subject
     * @param id2             ID of second subject
     * @param name2           Name of second subject
     * @param duplicateTarget ID target causing duplication
     * @throws Exception if an error occurs
     */
    @ParameterizedTest
    @CsvSource({
            "1, Matematyka, 2, Fizyka, 1"
    })
    void testEditSubjectDuplicateId(String id1, String name1,
                                    String id2, String name2,
                                    String duplicateTarget) throws Exception {
        Journal journal = new Journal();
        journal.addSubject(new Subject(id1, name1));
        journal.addSubject(new Subject(id2, name2));

        assertThrows(DuplicateIdException.class,
                () -> journal.editSubject(id2, duplicateTarget, "Nowy"));
    }

    /**
     * Parameterized test: null in editSubject arguments.
     *
     * @param oldId original ID
     * @param newId new ID
     * @param name  new name
     */
    @ParameterizedTest
    @CsvSource(nullValues = "NULL", value = {
            "NULL, 2, Nazwa",
            "1, NULL, Nazwa",
            "1, 2, NULL"
    })
    void testEditSubjectInvalidArgs(String oldId, String newId, String name) {
        Journal journal = new Journal();
        assertThrows(IllegalArgumentException.class, 
                () -> journal.editSubject(oldId, newId, name));
    }


    /**
     * Parameterized test: valid sorting of students by ID.
     *
     * @param studentsInput string representation of students to add
     * @param expectedOrder expected order of IDs after sorting
     * @throws Exception if an error occurs
     */
    @ParameterizedTest
    @CsvSource(value = {
            "3,Jan,Kowal | 1,Adam,Kowalski | 2,Ewa,Nowak ; 1,2,3", // standard
            "10,A,B | 2,C,D ; 10,2"
    }, delimiter = ';')
    void testGetStudentsSortedById(String studentsInput, String expectedOrder) throws Exception {
        Journal journal = new Journal();

        for (String entry : studentsInput.split("\\|")) {
            String[] p = entry.trim().split(",");
            journal.addStudent(new Student(p[0], p[1], p[2]));
        }

        List<Student> sorted = journal.getStudentsSortedById();
        String[] expectedIds = expectedOrder.split(",");

        assertEquals(expectedIds.length, sorted.size());
        for(int i=0; i<expectedIds.length; i++) {
             assertEquals(expectedIds[i], sorted.get(i).getId());
        }
    }
    
    /**
     * Parameterized test: sorting an empty list (edge case).
     *
     * @param emptyInput empty input source
     */
    @ParameterizedTest
    @ValueSource(strings = {""})
    void testGetStudentsSortedByIdEmpty(String emptyInput) {
        Journal journal = new Journal();
        List<Student> sorted = journal.getStudentsSortedById();
        assertTrue(sorted.isEmpty());
    }
}