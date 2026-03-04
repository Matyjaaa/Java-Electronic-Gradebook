 
package com.mycompany.lab5.model;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;

/**
 * Represents a subject entity in the database.
 * Converted from Record to Entity Class for JPA support.
 * This class maps to the "SUBJECTS" table.
 * 
 * @author Bartosz Matyjaszewski
 * @version 6.0
 */
@Entity
@Table(name = "SUBJECTS")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Subject implements Serializable {

    /**
     * The unique identifier for the subject (Primary Key).
     * Must consist of digits only.
     */
    @Id
    private String id;

    /**
     * The name of the subject.
     */
    @Column(name = "subject_name")
    private String name;

    /**
     * Constructs a new Subject with validation logic.
     * @param id the unique subject ID (must be digits only)
     * @param name the name of the subject (cannot be empty)
     * @throws IllegalArgumentException if id or name are invalid
     */
    public Subject(String id, String name) {
        validateId(id);
        validateName(name);
        this.id = id;
        this.name = name;
    }
    
    
    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    /**
     * Validates the subject ID.
     * @param id the ID to check
     * @throws IllegalArgumentException if ID is null, blank, or contains non-digits
     */
    private void validateId(String id) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("Subject ID cannot be null or blank");
        if (!id.matches("\\d+")) 
            throw new IllegalArgumentException("Subject ID must contain only digits!");
    }

    /**
     * Validates the subject name.
     * @param name the name to check
     * @throws IllegalArgumentException if name is null or blank
     */
    private void validateName(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Subject name cannot be null or blank");
    }

    /**
     * Sets a new ID for the subject after validation.
     * @param id the new ID
     * @throws IllegalArgumentException if the ID is invalid
     */
    public void setId(String id) {
        validateId(id);
        this.id = id;
    }

    /**
     * Sets a new name for the subject after validation.
     * @param name the new name
     * @throws IllegalArgumentException if the name is invalid
     */
    public void setName(String name) {
        validateName(name);
        this.name = name;
    }
    
    /**
     * Returns a string representation of the Subject in a specific format.
     * Required for passing unit tests.
     * @return a string formatted as "ID: [id] | [name]"
     */
    @Override
    public String toString() {
        return "ID: " + id + " | " + name;
    }
}