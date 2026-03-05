# Electronic Gradebook System (Java Web)

## Project Overview
An advanced web-based school gradebook application. The project is built on a multi-tier architecture (MVC) using **Jakarta EE (Java EE)** technologies. It allows for comprehensive management of students, subjects, and grades, with all data persistently stored in a relational database.

## Key Features
* **Student & Subject Management (CRUD):** Add, edit, and delete records with strict input validation (Regex).
* **Grading System:** Assign, modify, and remove grades for specific students and subjects.
* **Grade Sheet View (History):** A dedicated module that aggregates all student grades and automatically calculates their average.
* **State Management (Cookies):** Tracks and displays the user's most recently performed operation.
* **Defensive Programming:** Custom error handling mechanisms (e.g., `DuplicateIdException`) to ensure data integrity.

## Technologies & Tools
* **Language:** Java 17+ (featuring Switch Expressions, Streams API)
* **Backend:** Jakarta EE Servlets (`@WebServlet`)
* **Database & ORM:** JPA / Hibernate (`EntityManager`, `@Transactional`, `@Entity`, `@OneToMany`)
* **Frontend:** HTML5, CSS3, Bootstrap 5, JavaScript
* **Build Tool:** Maven (`pom.xml`)
* **IDE:** Apache NetBeans

## Architecture
The application is divided into three main layers:
1. **Model (Entities):** Database entities (Student, Grade, Subject) utilizing **Lombok** to reduce boilerplate code.
2. **Repository (DAO):** Interfaces and classes implementing the Data Access Object pattern for database communication. Uses Dependency Injection (`@Inject`, `@ApplicationScoped`).
3. **Controller / View:** Servlets acting as controllers, handling HTTP requests (GET/POST) and dynamically rendering HTML views.
