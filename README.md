# Task Management Panel

This project is a simple **Task Management Panel** application built with **Spring MVC** for handling HTTP requests,
**Thymeleaf** for dynamic HTML rendering, and **Spring Data JPA** for database interactions with a **MySQL** backend. 
The application provides a REST API and a web interface for managing tasks, supporting CRUD operations (Create, Read, 
Update, Delete) on tasks with different statuses.

## Features
- **Task Creation:** Add new tasks with descriptions and statuses.
- **Task Editing:** Update the description or status of existing tasks.
- **Task Deletion:** Delete tasks by their unique identifier.
- **Task Listing:** View a paginated list of all tasks.
- **Status Management:** Update and manage the status of each task (e.g., `IN_PROGRESS`, `DONE`, `PAUSED`).

---

## Project Structure
- **Backend:** Java Spring MVC framework, providing RESTful endpoints for CRUD operations.
- **Frontend:** Thymeleaf templates to render HTML views for task management.
- **Persistence:** Spring Data JPA and MySQL for data storage, configured to update the database schema automatically.
- **Embedded Tomcat:** Runs the application server on port 8080.
- **Docker Support:** Docker and Docker Compose setup to simplify local development and deployment.

## Prerequisites

- **Java 17**
- **Maven** (if building locally)
- **Docker** and **Docker Compose** for containerized setup

## Installation and Setup

### 1. Clone the Repository

```bash
git clone https://github.com/Lexoid2/spring-1.git
cd spring-1
```

### 2. Environment Configuration

The application uses Docker Compose to run both the app and MySQL database services. Ensure that `docker-compose.yml` 
and `Dockerfile` are correctly configured.

### 3. Build and Run with Docker Compose

```bash
docker-compose up --build
```

This command will:
1. Build the application using a multi-stage Dockerfile.
2. Set up a MySQL container with a persistent volume (mysql_data).
3. Start both the application and database containers.

### 4. Access the Application

Once both services are up and running, the application will be available at: `http://localhost:8080`

---

## Usage

### Task Management Endpoints

- **List Tasks:** `GET /tasks` - View paginated tasks in the main UI.
- **Create Task:** `POST /tasks` - Add a new task (JSON payload).
- **Update Task:** `PUT /tasks/edit/{id}` - Update a task's details by ID.
- **Delete Task:** `DELETE /tasks/{id}` - Remove a task by ID.

The frontend (accessible at `/tasks`) provides an interface to create, view, edit, and delete tasks, which are rendered
dynamically through Thymeleaf templates.

## Project Structure

### Key Classes and Files

- `Application.java` - Main entry point, starting an embedded Tomcat server and configuring the Spring 
ApplicationContext.
- `TaskController.java` - Handles all HTTP requests for task operations and maps them to corresponding service methods.
- `TaskService.java` - Provides business logic for CRUD operations on tasks, including database interactions.
- `TaskRepository.java` - Spring Data JPA repository interface for accessing the Task entity in the database.
- `Task.java` - Entity representing a task, with properties `id`, `description`, and `status`.
- `AppConfig.java` - Configures the Spring application context, including data source, transaction management, and 
Thymeleaf template settings.
- `docker-compose.yml` - Defines services for Docker Compose, including the app and MySQL with health checks.
- `Dockerfile` - Multi-stage build file for containerizing the application, using Maven for build and Alpine JRE for 
runtime.

---

## Testing and Development

To test and develop locally, ensure Java 17 and Maven are installed, and start the application directly:

1. **Build the Project:**

```bash
mvn clean package -DskipTests
```

2. **Run the Application:**

```bash
java -jar target/spring-1-jar-with-dependencies.jar
```

This approach is recommended when modifying code frequently.

---

## Technologies Used

- **Java 17:** Primary programming language.
- **Spring Framework:** Core framework for MVC, Dependency Injection, and REST.
- **Spring Data JPA:** Simplifies data access and repository management.
- **Thymeleaf:** Template engine for rendering dynamic HTML content.
- **MySQL:** Database for storing task data.
- **Docker:** Containerization platform for environment consistency.
- **Lombok:** Reduces boilerplate code with annotations.
- **Log4j:** Provides logging capabilities.

---

## Additional Notes

- **Database Schema:** The Task entity is mapped to a MySQL table and managed automatically via JPA.
- **Logging:** Uses Log4j for logging key actions and events throughout the application.

---

## License

This project is licensed under the MIT License.