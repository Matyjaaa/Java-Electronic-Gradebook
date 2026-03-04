package com.mycompany.lab5.services;

import com.mycompany.lab5.model.*;
import com.mycompany.lab5.repository.JournalRepository;
import jakarta.inject.Inject;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import jakarta.servlet.http.Cookie;
import java.util.List;

/**
 * Servlet responsible for displaying the grade sheet history from the Database.
 * Visual style is aligned with JournalServlet.
 * 
 * @author Bartosz Matyjaszewski
 * @version 6.0
 */
@WebServlet(name = "HistoryServlet", urlPatterns = {"/HistoryService"})
public class HistoryServlet extends HttpServlet {
    
    /**
     * The repository for accessing student and grade data.
     */
    @Inject
    private JournalRepository repository;

    /**
     * Handles the HTTP <code>GET</code> method.
     * Retrieves all students from the database and displays their grades and average score.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Fetch data from DB
        List<Student> students = repository.getAllStudents();

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html><html lang='pl'><head>");
            out.println("<meta charset='UTF-8'><link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>");
            out.println("<style>body{background-color:#f8f9fa;} .card{margin-bottom:20px; border:none; box-shadow:0 4px 6px rgba(0,0,0,0.1);}</style></head><body>");
            
            renderCookieInfo(request, response, out);
            
            // Main container
            out.println("<div class='container mt-4'>");
            
            // Navigation button
            out.println("<a href='index.html' class='btn btn-outline-primary mb-4'>🏠 Menu Główne</a>");
            
            out.println("<h2 class='mb-4 text-secondary text-center'>📊 Arkusz Ocen (Baza Danych)</h2>");

            if (students == null || students.isEmpty()) {
                out.println("<div class='alert alert-light border text-center'>Brak danych w bazie do wyświetlenia. Dodaj studentów w menu głównym.</div>");
            } else {
                out.println("<div class='card shadow-sm'><div class='card-body'>");
                out.println("<table class='table table-bordered table-striped'>");
                out.println("<thead class='table-dark text-center'><tr><th>Student (ID)</th><th>Oceny szczegółowe</th><th style='width: 100px;'>Średnia</th></tr></thead><tbody>");

                for (Student s : students) {
                    double sum = 0; 
                    int count = 0; 
                    StringBuilder gradesStr = new StringBuilder();
                    
                    for (Grade g : s.getGrades()) {
                        sum += g.getGradeValue(); 
                        count++;
                        gradesStr.append("<span class='badge bg-light text-dark border me-1'>")
                                 .append(g.getSubjectName()).append(": ")
                                 .append(g.getGradeValue()).append("</span>");
                    }
                    
                    double average = (count > 0) ? sum / count : 0.0;
                    
                    out.println("<tr>");
                    out.println("<td><strong>" + s.getLastName() + " " + s.getFirstName() + "</strong> <small class='text-muted'>(" + s.getId() + ")</small></td>");
                    out.println("<td>" + (gradesStr.length() > 0 ? gradesStr.toString() : "-") + "</td>");
                    out.println("<td class='text-center fw-bold text-primary'>" + (count > 0 ? String.format("%.2f", average) : "---") + "</td>");
                    out.println("</tr>");
                }
                out.println("</tbody></table></div></div>");
            }
            out.println("</div></body></html>");
        }
    }

    /**
     * Helper method to render the cookie information bar.
     * @param request the HttpServletRequest
     * @param response the HttpServletResponse
     * @param out the PrintWriter
     */
    private void renderCookieInfo(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        String lastAction = "Brak_danych"; // Domyślna wartość
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
}