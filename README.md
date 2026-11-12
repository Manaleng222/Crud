# Employee Management CRUD

A Java-based Employee Management System with MySQL, built to demonstrate clean architecture and design patterns. Includes two separate front-ends (JavaFX and Swing) sharing the same backend.

## Features
- Create, Read, Update, Delete employees
- Dual UI: JavaFX and Swing
- Repository pattern for database abstraction
- Template Method pattern with functional interfaces to eliminate JDBC boilerplate
- Clean separation of UI and database logic

## Tech Stack
- Java (JDK 11+)
- MySQL
- JavaFX & Swing
- Maven
- Design Patterns: Repository, Template Method
- 
## Architecture Highlights
-Repository Pattern – isolates business logic from database access.

-Template Method Pattern – abstract JDBC repository handles connection lifecycle, using functional interfaces for query logic.

-UI Decoupling – both JavaFX and Swing UIs use the same repository layer, proving the backend is UI-agnostic.
## Project Structure
src/
├── main/
│ ├── java/
│ │ └── com/crud/
│ │ ├── model/ # Employee entity
│ │ ├── repository/ # Abstract JDBC repository & interfaces
│ │ ├── repository/impl # Concrete SQL implementations
│ │ ├── ui/javafx/ # JavaFX controllers & views
│ │ └── ui/swing/ # Swing frames & panels



## Setup

### Prerequisites
- Java JDK 11+
- MySQL Server
- Maven

### Database Setup
1. Create a database named `employee_db`.
2. Run this SQL:

```sql
CREATE TABLE employees (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    country VARCHAR(50),
    role VARCHAR(50)
);

│ └── resources/
│ ├── db.properties # Database config
│ └── fxml/ # JavaFX layouts


