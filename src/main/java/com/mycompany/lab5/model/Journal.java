
package com.mycompany.lab5.model;

import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a school journal containing students and subjects.
 * Allows adding students and subjects while preventing duplicate IDs.
 * Provides access to lists of students and subjects.
 * 
 * @author Bartosz Matyjaszewski
 * @version 6.0
 */

/**
 * Creates an empty Journal instance with no students and subjects.
 */
@Getter
public class Journal {
    private List<Student> students = new ArrayList<>();
    private List<Subject> subjects = new ArrayList<>();
    
    /**
     * Adds a student to the journal.
     *
     * @param student the student to add
     * @throws DuplicateIdException if a student with the same ID already exists
     */

    public void addStudent(Student student) throws DuplicateIdException {
        if (student == null)
            throw new IllegalArgumentException("Student cannot be null");

        boolean exists = students.stream()
                .anyMatch(u -> u.getId().equals(student.getId()));

        if (exists)
            throw new DuplicateIdException("Student with ID " + student.getId() + " already exists!");

        students.add(student);
    }
    
    /**
     * Adds a subject to the journal.
     *
     * @param subject the subject to add
     * @throws DuplicateIdException if a subject with the same ID already exists
     */
    
    public void addSubject(Subject subject) throws DuplicateIdException {
        if (subject == null)
            throw new IllegalArgumentException("Subject cannot be null");

        boolean exists = subjects.stream()
                .anyMatch(s -> s.id().equals(subject.id()));

        if (exists)
            throw new DuplicateIdException("Subject with ID " + subject.id() + " already exists!");

        subjects.add(subject);
    }
    
    /**
     * Removes a student from the journal by ID.
     *
     * @param studentId ID of the student to remove
     * @return true if the student was removed, false if not found
     */
    public boolean removeStudent(String studentId) {
        if (studentId == null) return false;
        return students.removeIf(s -> s.getId().equals(studentId));
    }
    
    /**
     * Removes a subject from the journal by ID.
     *
     * @param subjectId ID of the subject to remove
     * @return true if the subject was removed, false if not found
     */
    public boolean removeSubject(String subjectId) {
        if (subjectId == null) return false;
        return subjects.removeIf(s -> s.id().equals(subjectId));
    }
    
    /**
    * Edits a student's basic data.
    *
    * @param oldId current ID of the student
    * @param newId new ID to set
    * @param newFirstName new first name
    * @param newLastName new last name
    * @return true if the student was found and edited
    * @throws DuplicateIdException if newId is used by another student
    */
    public boolean editStudent(String oldId, String newId,
                               String newFirstName, String newLastName)
            throws DuplicateIdException {

        if (oldId == null || newId == null ||
            newFirstName == null || newLastName == null)
            throw new IllegalArgumentException("Arguments cannot be null");

        Student student = students.stream()
                .filter(s -> s.getId().equals(oldId))
                .findFirst()
                .orElse(null);

        if (student == null) return false;

        if (!oldId.equals(newId)) {
            boolean exists = students.stream().anyMatch(s -> s.getId().equals(newId));
            if (exists)
                throw new DuplicateIdException("Student with ID " + newId + " already exists!");
            student.setId(newId);
        }

        student.setFirstName(newFirstName);
        student.setLastName(newLastName);
        return true;
    }
    
    /**
    * Edits an existing subject.
    *
    * @param oldId current subject ID
    * @param newId new subject ID
    * @param newName new subject name
    * @return true if the subject was found and edited
    * @throws DuplicateIdException if newId already exists
    */
    public boolean editSubject(String oldId, String newId, String newName)
            throws DuplicateIdException {

        if (oldId == null || newId == null || newName == null)
            throw new IllegalArgumentException("Arguments cannot be null");

        Subject oldSubject = subjects.stream()
                .filter(s -> s.id().equals(oldId))
                .findFirst()
                .orElse(null);

        if (oldSubject == null) return false;

        if (!oldId.equals(newId)) {
            boolean exists = subjects.stream().anyMatch(s -> s.id().equals(newId));
            if (exists)
                throw new DuplicateIdException("Subject with ID " + newId + " already exists!");
        }

        int index = subjects.indexOf(oldSubject);
        if (index < 0)
            return false; // bezpieczeństwo

        Subject updated = new Subject(newId, newName);
        subjects.set(index, updated);

        // Aktualizacja ocen studentów
        for (Student st : students) {
            for (int i = 0; i < st.getGrades().size(); i++) {
                Grade g = st.getGrades().get(i);
                if (g.getSubjectName().equals(oldSubject.name())) { 
                    st.getGrades().set(i, new Grade(updated, g.getGradeValue()));
                }
            }
        }

        return true;
    }
    
    /**
    * Returns a list of students sorted by their ID in ascending order.
    * <p>
    * Uses a stream and a lambda expression for sorting.
    * </p>
    *
    * @return a list of students sorted by ID
    */
    public List<Student> getStudentsSortedById() {
        return students.stream()
                .sorted((s1, s2) -> s1.getId().compareToIgnoreCase(s2.getId()))
                .toList();
    }

    

}
