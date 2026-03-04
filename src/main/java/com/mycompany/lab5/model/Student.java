package com.mycompany.lab5.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * Represents a student entity in the system.
 * Contains personal details (ID, names) and a relationship to the Grade entity.
 * Maps to the "STUDENTS" table in the database.
 * 
 * @author Bartosz Matyjaszewski
 * @version 6.0
 */

@Entity
@Table(name = "STUDENTS")
@Getter
@Setter
@NoArgsConstructor
@ToString(of = {"id", "firstName", "lastName"})
public class Student implements Serializable {
    
    /**
     * The unique identifier for the student (Primary Key).
     * Must consist of digits only.
     */
    @Id
    private String id;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    /**
     * One-to-Many relationship with the Grade entity.
     * Defines that one student can have multiple grades.
     * Operations (persist, remove) are cascaded to the grades.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "student_id")
    
    private List<Grade> grades = new ArrayList<>();

    
    /**
     * Constructs a new Student with the specified ID and names.
     * Performs validation on the input data.
     *
     * @param id the unique student ID (must be digits only)
     * @param firstName the student's first name (must be capitalized)
     * @param lastName the student's last name (must be capitalized)
     * @throws IllegalArgumentException if any parameter is null, blank, or in an invalid format
     */
    public Student(String id, String firstName, String lastName) {

        if (id == null || id.isBlank())
            throw new IllegalArgumentException("Student ID cannot be null or blank");
        if (!id.matches("\\d+"))
            throw new IllegalArgumentException("Student ID must contain only digits!");

        validateName(firstName, "First name");
        validateName(lastName, "Last name");

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;

        this.grades = new ArrayList<>();
    }
    
    /**
     * Validates a name string (first name or last name).
     *
     * @param value the name string to validate
     * @param fieldName the name of the field for error reporting
     * @throws IllegalArgumentException if the value is null, blank, does not start with a capital letter, or contains non-letters
     */
    private void validateName(String value, String fieldName) {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");

        if (!value.matches("[A-Z][a-zA-Z]*"))
            throw new IllegalArgumentException(fieldName + " must start with a capital letter and contain only letters");
    }
    
    /**
     * Adds a grade to the student's list of grades.
     *
     * @param grade the Grade object to add
     * @throws IllegalArgumentException if the provided grade object is null
     */
    public void addGrade(Grade grade) {
        if (grade == null) {
            throw new IllegalArgumentException("Grade cannot be null");
        }
        grades.add(grade);
    }
    
    
    /**
     * Removes a single grade of a specific value from the specified subject.
     *
     * @param subject the subject from which the grade should be removed
     * @param value the numeric value of the grade to remove
     * @return true if a matching grade was found and removed, false otherwise
     * @throws IllegalArgumentException if the subject is null
     */
    public boolean removeSingleGrade(Subject subject, double value) {
        if (subject == null)
            throw new IllegalArgumentException("Subject cannot be null");

        for (int i = 0; i < grades.size(); i++) {
            Grade g = grades.get(i);
            if (g.getSubjectName().equals(subject.name()) && g.getGradeValue() == value) {
                grades.remove(i);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Edits an existing grade for a given subject by changing its value.
     *
     * @param subject the subject of the grade to edit
     * @param oldValue the current value of the grade to find
     * @param newValue the new value to set
     * @return true if the grade was found and updated, false otherwise
     * @throws IllegalArgumentException if the new value is invalid (handled by Grade constructor)
     */
    public boolean editGrade(Subject subject, double oldValue, double newValue) {
        for (int i = 0; i < grades.size(); i++) {
            Grade g = grades.get(i);
            if (g.getSubjectName().equals(subject.name()) && g.getGradeValue() == oldValue) {
                grades.set(i, new Grade(subject, newValue));
                return true;
            }
        }
    return false;
    }   

}
