
package com.mycompany.lab5.model;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

/**
 * Represents a grade for a specific subject.
 * This entity is stored in the "GRADES" table in the database.
 * 
 * @author Bartosz Matyjaszewski
 * @version 6.0
 */


@Entity
@Table(name = "GRADES")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = "db_id")
public class Grade implements Serializable {

    /**
     * The unique database identifier for the grade (Primary Key).
     * Generated automatically by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long db_id;

    private String subjectName;
    private double gradeValue;

    /**
     * Constructor for creating a new grade instance with validation.
     * * @param subject the subject associated with the grade (cannot be null)
     * @param value the numeric value of the grade (must be between 1.0 and 6.0)
     * @throws IllegalArgumentException if the subject is null or the value is out of range
     */
    public Grade(Subject subject, double value) {
        if (subject == null)
            throw new IllegalArgumentException("Subject cannot be null");

        if (value < 1.0 || value > 6.0)
            throw new IllegalArgumentException("Grade value must be between 1.0 and 6.0");
            
        this.subjectName = subject.name();
        this.gradeValue = value;
    }

    /**
     * Returns a string representation of the grade.
     * * @return a string containing the subject name and the grade value
     */
    @Override
    public String toString() {
        return subjectName + ": " + gradeValue;
    }
}

