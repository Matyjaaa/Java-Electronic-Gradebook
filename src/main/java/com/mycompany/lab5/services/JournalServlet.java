package com.mycompany.lab5.services;

import com.mycompany.lab5.model.*;
import com.mycompany.lab5.repository.JournalRepository;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Main controller Servlet for the Journal application.
 * Handles all CRUD operations for Students, Subjects, and Grades via a Database Repository.
 * Manages HTTP requests and renders the HTML user interface.
 * 
 * @author Bartosz Matyjaszewskid
 * @version 6.0
 */

@WebServlet(name = "JournalServlet", urlPatterns = {"/JournalService"})
public class JournalServlet extends HttpServlet {

    /**
     * The repository used for database operations.
     * Injected by the container (CDI).
     */
    @Inject
    private JournalRepository repository;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * Handles action dispatching, cookie management, and view rendering.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        String message = "";
        String error = "";

        // Cookie management for the last performed action
        if (action != null && !action.isEmpty()) {
            String cookieValue = URLEncoder.encode("Ostatnia_operacja_" + action, StandardCharsets.UTF_8);
            Cookie lastActionCookie = new Cookie("lastAction", cookieValue);
            lastActionCookie.setMaxAge(60 * 60 * 24); 
            response.addCookie(lastActionCookie);
        }

        try {
            if (action != null) {
                switch (action) {
                    case "addStudent" -> {
                        Student s = new Student(request.getParameter("id"), request.getParameter("firstName"), request.getParameter("lastName"));
                        repository.add(s); 
                        message = "Dodano studenta do bazy.";
                    }
                    case "removeStudent" -> {
                        repository.remove(request.getParameter("id")); 
                        message = "Usunięto studenta z bazy.";
                    }
                    case "editStudent" -> {
                        Student s = repository.findById(request.getParameter("oldId"));
                        if (s != null) {
                            String newFirst = request.getParameter("newFirstName");
                            String newLast = request.getParameter("newLastName");
                            if(newFirst != null && !newFirst.isEmpty()) s.setFirstName(newFirst);
                            if(newLast != null && !newLast.isEmpty()) s.setLastName(newLast);
                            repository.update(s);
                            message = "Zaktualizowano dane studenta.";
                        } else {
                            error = "Nie znaleziono studenta.";
                        }
                    }

                    case "addSubject" -> {
                        Subject sub = new Subject(request.getParameter("id"), request.getParameter("name"));
                        repository.addSubject(sub);
                        message = "Dodano przedmiot do bazy.";
                    }
                    case "removeSubject" -> {
                        repository.removeSubject(request.getParameter("id"));
                        message = "Usunięto przedmiot z bazy.";
                    }
                    case "editSubject" -> {
                        String id = request.getParameter("oldId");
                        Subject s = repository.findSubjectById(id);
                        
                        if(s != null) {
                            String newName = request.getParameter("newName");
                            if(newName != null && !newName.isEmpty()) {
                                s.setName(newName);
                                repository.updateSubject(s);
                                message = "Zaktualizowano nazwę przedmiotu.";
                            }
                        } else {
                            error = "Nie znaleziono przedmiotu o podanym ID.";
                        }
                    }

                    case "addGrade" -> {
                        String sId = request.getParameter("studentId");
                        String subId = request.getParameter("subjectId");
                        double val = Double.parseDouble(request.getParameter("value"));

                        Student st = repository.findById(sId);
                        Subject sb = repository.findSubjectById(subId);

                        if (st != null && sb != null) {
                            st.addGrade(new Grade(sb, val));
                            repository.update(st); 
                            message = "Dodano ocenę do bazy.";
                        } else {
                            error = "Błąd: Nie znaleziono studenta lub przedmiotu w bazie.";
                        }
                    }
                    case "editGrade" -> {
                        String sId = request.getParameter("studentId");
                        String subId = request.getParameter("subjectId");
                        double oldVal = Double.parseDouble(request.getParameter("oldValue"));
                        double newVal = Double.parseDouble(request.getParameter("newValue"));

                        Student st = repository.findById(sId);
                        Subject sb = repository.findSubjectById(subId);

                        if (st != null && sb != null) {
                            boolean updated = st.editGrade(sb, oldVal, newVal);
                            if (updated) {
                                repository.update(st);
                                message = "Pomyślnie zaktualizowano ocenę.";
                            } else {
                                error = "Nie znaleziono takiej oceny u studenta.";
                            }
                        } else {
                            error = "Błąd danych.";
                        }
                    }
                    case "removeGrade" -> {
                        String sId = request.getParameter("studentId");
                        Long gradeDbId = Long.parseLong(request.getParameter("gradeId"));

                        Student st = repository.findById(sId);
                        if (st != null) {
                            boolean removed = st.getGrades().removeIf(g -> g.getDb_id() != null && g.getDb_id().equals(gradeDbId));
                            if (removed) {
                                repository.update(st);
                                message = "Usunięto ocenę.";
                            } else {
                                error = "Nie znaleziono wskazanej oceny.";
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            error = "Błąd: " + e.getMessage();
        }

        List<Student> studentList = repository.getAllStudents();
        List<Subject> subjectList = repository.getAllSubjects();

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html><html lang='pl'><head><meta charset='UTF-8'>");
            out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>");
            out.println("<style>body{background-color:#f8f9fa;} .card{margin-bottom:20px; border:none; box-shadow:0 4px 6px rgba(0,0,0,0.1);}</style>");
            
            // JavaScript functions for UI interaction
            out.println("<script>");
            out.println("function fillEditForm(studentId, subjectName, gradeValue) {");
            out.println("  document.getElementById('editStudentSelect').value = studentId;");
            out.println("  document.getElementById('editOldValue').value = gradeValue;");
            out.println("  let subjectSelect = document.getElementById('editSubjectSelect');");
            out.println("  for(let i=0; i < subjectSelect.options.length; i++) {");
            out.println("    if(subjectSelect.options[i].text === subjectName) {");
            out.println("      subjectSelect.selectedIndex = i; break;");
            out.println("    }");
            out.println("  }");
            out.println("  window.location.hash = '#editGradeCard';"); 
            out.println("}");
            
            out.println("function fillStudentEditForm(id) {");
            out.println("  document.getElementById('editStudentOldId').value = id;");
            out.println("  document.getElementById('editStudentFirstName').value = '';"); 
            out.println("  document.getElementById('editStudentLastName').value = '';");  
            out.println("  window.location.hash = '#editStudentCard';");
            out.println("}");
            
            out.println("function fillSubjectEditForm(id) {");
            out.println("  document.getElementById('editSubjectOldId').value = id;");
            out.println("  document.getElementById('editSubjectNewName').value = '';"); 
            out.println("  window.location.hash = '#editSubjectCard';");
            out.println("}");
            out.println("</script>");
            
            out.println("</head><body>");
            renderCookieInfo(request, response, out);
            out.println("<div class='container mt-4'><a href='index.html' class='btn btn-outline-primary mb-4'>🏠 Menu Główne</a>");

            // Dispatch to specific view renderer
            if (action != null && (action.contains("Student") || action.equals("manageStudents"))) {
                renderStudentManager(out, studentList, message, error);
            } else if (action != null && (action.contains("Subject") || action.equals("manageSubjects"))) {
                renderSubjectManager(out, subjectList, message, error);
            } else if (action != null && (action.contains("Grade") || action.equals("manageGrades"))) {
                renderGradeManager(out, studentList, subjectList, message, error);
            }
            out.println("</div></body></html>");
        }
    }

    /**
     * Renders the HTML section for managing Students.
     * @param out the PrintWriter to write HTML response
     * @param students list of students to display
     * @param msg success message (can be empty)
     * @param err error message (can be empty)
     */
    private void renderStudentManager(PrintWriter out, List<Student> students, String msg, String err) {
        out.println("<h2 class='text-primary mb-4'>👨‍🎓 Zarządzanie Studentami (DB)</h2>");
        if(!msg.isEmpty()) out.println("<div class='alert alert-success'>"+msg+"</div>");
        if(!err.isEmpty()) out.println("<div class='alert alert-danger'>"+err+"</div>");

        out.println("<div class='row'>");
        out.println("<div class='col-md-4'><div class='card'><div class='card-body'><h6>Dodaj Studenta</h6><form action='JournalService' method='POST'>");
        out.println("<input type='hidden' name='action' value='addStudent'>");
        out.println("<input type='text' name='id' class='form-control mb-2' placeholder='ID' required><input type='text' name='firstName' class='form-control mb-2' placeholder='Imię' required><input type='text' name='lastName' class='form-control mb-2' placeholder='Nazwisko' required><button class='btn btn-success w-100 mb-2'>Zapisz w bazie</button></form></div></div>");
        
        out.println("<div class='card' id='editStudentCard'><div class='card-header bg-warning'>Edytuj Studenta</div><div class='card-body'><form action='JournalService' method='POST'><input type='hidden' name='action' value='editStudent'>");
        out.println("<input type='text' name='oldId' id='editStudentOldId' class='form-control mb-2' placeholder='Obecne ID' readonly><input type='text' name='newFirstName' id='editStudentFirstName' class='form-control mb-2' placeholder='Nowe Imię'><input type='text' name='newLastName' id='editStudentLastName' class='form-control mb-2' placeholder='Nowe Nazwisko'><button class='btn btn-warning w-100'>Zaktualizuj</button></form></div></div></div>");
        
        out.println("<div class='col-md-8'><div class='card'><div class='card-body'><h6>Lista z Bazy Danych</h6><table class='table table-hover'><thead><tr><th>ID</th><th>Imię Nazwisko</th><th>Akcja</th></tr></thead><tbody>");
        for (Student s : students) {
            out.println("<tr><td>"+s.getId()+"</td><td>"+s.getFirstName()+" "+s.getLastName()+"</td><td><div class='d-flex gap-1'><button type='button' class='btn btn-warning btn-sm' onclick=\"fillStudentEditForm('"+s.getId()+"')\">✏️</button><form action='JournalService' method='POST' style='margin:0;'><input type='hidden' name='action' value='removeStudent'><input type='hidden' name='id' value='"+s.getId()+"'><button class='btn btn-danger btn-sm'>🗑️</button></form></div></td></tr>");
        }
        out.println("</tbody></table></div></div></div></div>");
    }

    /**
     * Renders the HTML section for managing Subjects.
     * @param out the PrintWriter to write HTML response
     * @param subjects list of subjects to display
     * @param msg success message (can be empty)
     * @param err error message (can be empty)
     */
    private void renderSubjectManager(PrintWriter out, List<Subject> subjects, String msg, String err) {
        out.println("<h2 class='text-secondary mb-4'>📚 Zarządzanie Przedmiotami (DB)</h2>");
        if(!msg.isEmpty()) out.println("<div class='alert alert-success'>"+msg+"</div>");
        if(!err.isEmpty()) out.println("<div class='alert alert-danger'>"+err+"</div>");
        
        out.println("<div class='row'>");
        out.println("<div class='col-md-4'><div class='card'><div class='card-body'><h6>Dodaj Przedmiot</h6><form action='JournalService' method='POST'><input type='hidden' name='action' value='addSubject'>");
        out.println("<input type='text' name='id' class='form-control mb-2' placeholder='ID' required><input type='text' name='name' class='form-control mb-2' placeholder='Nazwa' required><button class='btn btn-secondary w-100 mb-2'>Dodaj</button></form></div></div>");
        
        out.println("<div class='card' id='editSubjectCard'><div class='card-header bg-warning'>Edytuj Przedmiot</div><div class='card-body'><form action='JournalService' method='POST'><input type='hidden' name='action' value='editSubject'>");
        out.println("<input type='text' name='oldId' id='editSubjectOldId' class='form-control mb-2' placeholder='Obecne ID' readonly>");
        out.println("<input type='text' name='newName' id='editSubjectNewName' class='form-control mb-2' placeholder='Nowa Nazwa'><button class='btn btn-warning w-100'>Zmień Nazwę</button></form></div></div></div>");

        out.println("<div class='col-md-8'><div class='card'><div class='card-body'><h6>Lista z Bazy Danych</h6><table class='table'><thead><tr><th>ID</th><th>Nazwa</th><th>Akcja</th></tr></thead><tbody>");
        for (Subject s : subjects) {
            out.println("<tr><td>"+s.getId()+"</td><td>"+s.getName()+"</td><td><div class='d-flex gap-1'><button type='button' class='btn btn-warning btn-sm' onclick=\"fillSubjectEditForm('"+s.getId()+"')\">✏️</button><form action='JournalService' method='POST' style='margin:0;'><input type='hidden' name='action' value='removeSubject'><input type='hidden' name='id' value='"+s.getId()+"'><button class='btn btn-outline-danger btn-sm'>🗑️</button></form></div></td></tr>");
        }
        out.println("</tbody></table></div></div></div></div>");
    }

    /**
     * Renders the HTML section for managing Grades.
     * @param out the PrintWriter to write HTML response
     * @param students list of students (for selection and display)
     * @param subjects list of subjects (for selection)
     * @param msg success message (can be empty)
     * @param err error message (can be empty)
     */
    private void renderGradeManager(PrintWriter out, List<Student> students, List<Subject> subjects, String msg, String err) {
        out.println("<h2 class='text-info mb-4'>📝 Wystawianie i Edycja Ocen</h2>");
        if(!msg.isEmpty()) out.println("<div class='alert alert-success'>"+msg+"</div>");
        if(!err.isEmpty()) out.println("<div class='alert alert-danger'>"+err+"</div>");

        out.println("<div class='row'><div class='col-md-5'><div class='card'><div class='card-header'>Dodaj nową ocenę</div><div class='card-body'><form action='JournalService' method='POST' class='row g-2'><input type='hidden' name='action' value='addGrade'>");
        out.println("<div class='col-12'><select name='studentId' class='form-select'>");
        for (Student s : students) out.println("<option value='"+s.getId()+"'>"+s.getLastName()+" "+s.getFirstName()+"</option>");
        out.println("</select></div><div class='col-12'><select name='subjectId' class='form-select'>");
        for (Subject sub : subjects) out.println("<option value='"+sub.getId()+"'>"+sub.getName()+"</option>");
        out.println("</select></div>");
        out.println("<div class='col-6'><input type='number' step='0.5' min='1.0' max='6.0' name='value' class='form-control' placeholder='Ocena' required></div><div class='col-6'><button class='btn btn-primary w-100'>Dodaj</button></div></form></div></div>");
        
        out.println("<div class='card mt-3' id='editGradeCard'><div class='card-header bg-warning text-dark'>Edytuj ocenę</div><div class='card-body'><form action='JournalService' method='POST' class='row g-2'><input type='hidden' name='action' value='editGrade'>");
        out.println("<div class='col-12'><select name='studentId' id='editStudentSelect' class='form-select'>");
        for (Student s : students) out.println("<option value='"+s.getId()+"'>"+s.getLastName()+" "+s.getFirstName()+"</option>");
        out.println("</select></div><div class='col-12'><select name='subjectId' id='editSubjectSelect' class='form-select'>");
        for (Subject sub : subjects) out.println("<option value='"+sub.getId()+"'>"+sub.getName()+"</option>");
        out.println("</select></div>");
        out.println("<div class='col-6'><input type='number' step='0.5' min='1.0' max='6.0' name='oldValue' id='editOldValue' class='form-control' placeholder='Stara' required></div><div class='col-6'><input type='number' step='0.5' min='1.0' max='6.0' name='newValue' class='form-control' placeholder='Nowa' required></div><div class='col-12'><button class='btn btn-warning w-100'>Zmień</button></div></form></div></div></div>");

        out.println("<div class='col-md-7'><div class='card'><div class='card-body'><h6>📋 Tabela Ocen</h6><table class='table table-striped table-sm'><thead class='table-dark'><tr><th>Student</th><th>Przedmiot</th><th>Ocena</th><th>Akcje</th></tr></thead><tbody>");
        boolean hasGrades = false;
        for (Student s : students) {
            for (Grade g : s.getGrades()) {
                hasGrades = true;
                out.println("<tr><td>" + s.getLastName() + " " + s.getFirstName() + "</td><td>" + g.getSubjectName() + "</td><td><span class='badge bg-secondary'>" + g.getGradeValue() + "</span></td>");
                out.println("<td><div class='d-flex gap-1'><button type='button' class='btn btn-warning btn-sm' onclick=\"fillEditForm('"+s.getId()+"', '"+g.getSubjectName()+"', '"+g.getGradeValue()+"')\">✏️</button>");
                out.println("<form action='JournalService' method='POST' style='margin:0;'><input type='hidden' name='action' value='removeGrade'><input type='hidden' name='studentId' value='"+s.getId()+"'><input type='hidden' name='gradeId' value='"+g.getDb_id()+"'><button class='btn btn-danger btn-sm'>🗑️</button></form></div></td></tr>");
            }
        }
        if (!hasGrades) out.println("<tr><td colspan='4' class='text-center text-muted'>Brak wpisanych ocen w bazie.</td></tr>");
        out.println("</tbody></table></div></div></div></div>");
    }

    /**
     * Renders the cookie information bar.
     * @param request servlet request
     * @param response servlet response
     * @param out PrintWriter output
     */
    private void renderCookieInfo(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        String lastAction = "Brak_danych";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("lastAction".equals(c.getName())) {
                    lastAction = URLDecoder.decode(c.getValue(), StandardCharsets.UTF_8);
                }
            }
        }
        out.println("<div class='container-fluid bg-dark text-white p-2 mb-3'>");
        out.println("<small>ℹ️ Ostatnia operacja (<strong>Dane odczytane z pliku Cookie</strong>): <strong>" + lastAction + "</strong></small>");
        out.println("</div>");
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param req servlet request
     * @param resp servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { processRequest(req, resp); }
    
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param req servlet request
     * @param resp servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { processRequest(req, resp); }
}