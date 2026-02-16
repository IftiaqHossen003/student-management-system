# Student Management System

A Spring Boot web application demonstrating Role-Based Access Control (RBAC) with Spring Security, containerized using Docker, and database versioning with Liquibase.

## Features

### 1. Authentication and Authorization (Spring Security)
- **Role-Based Access Control (RBAC)** with two roles:
  - **ADMIN**: Full access (view, add, delete students)
  - **USER**: Read-only access (view students only)
- Custom UserDetailsService implementation
- BCrypt password encoding
- Login/Logout functionality
- Method-level security with `@PreAuthorize`

### 2. Docker Containerization
- Multi-stage Dockerfile for optimized builds
- Docker Compose configuration with:
  - PostgreSQL 15 database
  - Application container
  - Health checks
  - Network isolation
  - Volume persistence

### 3. Database Migration (Liquibase)
- Version-controlled database schema
- Automatic migrations on startup
- Separate changesets for:
  - Students table
  - Users and roles tables
  - Sample data insertion

## Technology Stack

- **Backend**: Spring Boot 3.2.0, Spring Security, Spring Data JPA
- **Frontend**: Thymeleaf, HTML, CSS
- **Database**: PostgreSQL 15
- **Migration**: Liquibase
- **Containerization**: Docker, Docker Compose
- **Build Tool**: Maven
- **Java Version**: 17

## Project Structure

```
webapp/
├── src/
│   ├── main/
│   │   ├── java/com/example/webapp/
│   │   │   ├── config/
│   │   │   │   ├── AppConfig.java
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── HomeController.java
│   │   │   │   └── StudentController.java
│   │   │   ├── dto/
│   │   │   │   └── StudentDTO.java
│   │   │   ├── entity/
│   │   │   │   ├── Student.java
│   │   │   │   └── User.java
│   │   │   ├── repository/
│   │   │   │   ├── StudentRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   ├── security/
│   │   │   │   ├── CustomUserDetails.java
│   │   │   │   └── CustomUserDetailsService.java
│   │   │   ├── service/
│   │   │   │   └── StudentService.java
│   │   │   └── WebappApplication.java
│   │   └── resources/
│   │       ├── db/changelog/
│   │       │   ├── db.changelog-master.xml
│   │       │   ├── 001-create-students-table.xml
│   │       │   ├── 002-create-users-table.xml
│   │       │   └── 003-insert-sample-data.xml
│   │       ├── templates/
│   │       │   ├── home.html
│   │       │   ├── login.html
│   │       │   ├── students.html
│   │       │   └── student-form.html
│   │       └── application.properties
├── Dockerfile
├── docker-compose.yaml
└── pom.xml
```

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Docker and Docker Compose (for containerized deployment)

### Running with Docker Compose (Recommended)

1. **Clone and navigate to the project directory**
   ```bash
   cd "d:\New folder (2)"
   ```

2. **Build and run with Docker Compose**
   ```bash
   docker-compose up --build
   ```

3. **Access the application**
   - Open browser: http://localhost:8080
   - The database and application will start automatically

4. **Stop the application**
   ```bash
   docker-compose down
   ```

### Running Locally (Without Docker)

1. **Ensure PostgreSQL is running**
   ```bash
   # Update src/main/resources/application.properties with your database credentials
   ```

2. **Build the project**
   ```bash
   ./mvnw clean package
   ```

3. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Access the application**
   - Open browser: http://localhost:8080

## Demo Credentials

### Admin User (Full Access)
- **Username**: `admin`
- **Password**: `admin123`
- **Permissions**: View, Add, Delete students

### Regular User (Read-Only)
- **Username**: `user`
- **Password**: `user123`
- **Permissions**: View students only

## API Endpoints

| Endpoint | Method | Role Required | Description |
|----------|--------|---------------|-------------|
| `/` | GET | Public | Home page |
| `/login` | GET/POST | Public | Login page |
| `/logout` | POST | Authenticated | Logout |
| `/students` | GET | USER, ADMIN | View all students |
| `/students/add` | GET | ADMIN | Show add student form |
| `/students/store` | POST | ADMIN | Save new student |
| `/students/delete/{id}` | POST | ADMIN | Delete student |

## Database Schema

### Students Table
```sql
CREATE TABLE students (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    roll VARCHAR(255)
);
```

### Users Table
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN DEFAULT true
);

CREATE TABLE user_roles (
    user_id BIGINT REFERENCES users(id),
    role VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, role)
);
```

## Security Features

1. **Password Encryption**: BCrypt with strength 10
2. **Role-Based Access**: Method-level security using `@PreAuthorize`
3. **CSRF Protection**: Enabled by default
4. **Session Management**: Server-side session management
5. **Form-Based Login**: Custom login page with logout

## Liquibase Migrations

Liquibase automatically manages database schema versions:
- **Changelog 001**: Creates students table
- **Changelog 002**: Creates users and user_roles tables
- **Changelog 003**: Inserts sample data (3 students, 2 users)

## Docker Configuration

### Dockerfile
- Multi-stage build for smaller image size
- Uses Alpine Linux for minimal footprint
- Exposes port 8080

### Docker Compose
- PostgreSQL service with health checks
- Application waits for database to be ready
- Persistent volume for database data
- Isolated network for services

## Development

### Adding New Users
Add users via Liquibase changeset or programmatically:
```java
User user = User.builder()
    .username("newuser")
    .password(passwordEncoder.encode("password"))
    .enabled(true)
    .roles(Set.of("USER"))
    .build();
userRepository.save(user);
```

### Adding New Roles
Update `SecurityConfig.java` and add role checks:
```java
.requestMatchers("/admin/**").hasRole("ADMIN")
```

## Troubleshooting

### Port Already in Use
```bash
# Change port in application.properties
server.port=8081
```

### Database Connection Issues
```bash
# Check PostgreSQL is running
docker ps

# View logs
docker-compose logs postgres
```

### Liquibase Migration Errors
```bash
# Clear Liquibase lock
docker exec -it studentdb psql -U postgres -d studentdb
DELETE FROM databasechangeloglock;
```

## Testing

Run tests with:
```bash
./mvnw test
```

## Building for Production

1. **Build the JAR**
   ```bash
   ./mvnw clean package -DskipTests
   ```

2. **Run the JAR**
   ```bash
   java -jar target/webapp-0.0.1-SNAPSHOT.jar
   ```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is created for educational purposes (KUET CSE Department).

## Acknowledgments

- Spring Boot Documentation
- Spring Security Reference
- Liquibase Documentation
- Docker Documentation
