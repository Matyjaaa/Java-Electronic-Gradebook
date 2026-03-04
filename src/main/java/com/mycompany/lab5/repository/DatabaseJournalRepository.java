package com.mycompany.lab5.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import java.util.List;
import com.mycompany.lab5.model.Student;
import com.mycompany.lab5.model.Subject;

/**
 * Implementation of the JournalRepository interface using JPA and a database.
 * Annotated with @ApplicationScoped to ensure a single instance is created for the application lifecycle.
 * 
 * @author Bartosz Matyjaszewski
 * @version 6.0
 */
@ApplicationScoped 
public class DatabaseJournalRepository implements JournalRepository {

    /**
     * The EntityManager for interacting with the persistence context.
     * Injected by the container based on the persistence.xml configuration.
     */
    @PersistenceContext(unitName = "my_persistence_unit")
    private EntityManager em;

    /**
     * Retrieves all students from the database.
     * @return a list of all students
     */
    @Override
    public List<Student> getAllStudents() {
        Query query = em.createQuery("SELECT s FROM Student s");
        return query.getResultList();
    }

    /**
     * Adds a new student to the database.
     * @param student the student entity to add
     */
    @Override
    @Transactional
    public void add(Student student) {
        try {
            em.persist(student);
        } catch (Exception ex) {
        }
    }

    /**
     * Removes a student from the database by ID.
     * @param id the ID of the student to remove
     */
    @Override
    @Transactional
    public void remove(String id) {
        try {
            Student student = em.find(Student.class, id);
            if (student != null) {
                em.remove(student);
            }
        } catch (Exception ex) {
        }
    }

    /**
     * Updates an existing student's data in the database.
     * @param student the student entity with updated data
     */
    @Override
    @Transactional
    public void update(Student student) {
        try {
            em.merge(student);
        } catch (Exception ex) {
        }
    }

    /**
     * Finds a student by their ID.
     * @param id the ID of the student
     * @return the Student entity, or null if not found
     */
    @Override
    public Student findById(String id) {
        return em.find(Student.class, id);
    }
    
    /**
     * Retrieves all subjects from the database.
     * @return a list of all subjects
     */
    @Override
    public List<Subject> getAllSubjects() {
        return em.createQuery("SELECT s FROM Subject s", Subject.class).getResultList();
    }

    /**
     * Adds a new subject to the database.
     * * @param subject the subject entity to add
     */
    @Override
    @Transactional
    public void addSubject(Subject subject) {
        em.persist(subject);
    }

    /**
     * Removes a subject from the database by ID.
     * @param id the ID of the subject to remove
     */
    @Override
    @Transactional
    public void removeSubject(String id) {
        Subject s = em.find(Subject.class, id);
        if (s != null) em.remove(s);
    }
    
    /**
     * Updates an existing subject in the database.
     * @param subject the subject entity to update
     */
    @Override
    @Transactional
    public void updateSubject(Subject subject) {
        em.merge(subject);
    }

    /**
     * Finds a subject by its ID.
     * @param id the ID of the subject
     * @return the Subject entity, or null if not found
     */
    @Override
    public Subject findSubjectById(String id) {
        return em.find(Subject.class, id);
    }
}