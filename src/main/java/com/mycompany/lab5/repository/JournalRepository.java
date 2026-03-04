package com.mycompany.lab5.repository;

import com.mycompany.lab5.model.Student;
import com.mycompany.lab5.model.Subject;
import java.util.List;

public interface JournalRepository {
    void add(Student student);
    void update(Student student);
    void remove(String id);
    List<Student> getAllStudents();
    Student findById(String id);
    
    void addSubject(Subject subject);
    void removeSubject(String id);
    void updateSubject(Subject subject);
    List<Subject> getAllSubjects();
    Subject findSubjectById(String id);
}