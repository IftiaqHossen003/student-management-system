# Quick Start Guide

## Running the Application

### Option 1: Using Docker Compose (Easiest)

1. Open PowerShell in the project directory:
   ```powershell
   cd "d:\New folder (2)"
   ```

2. Start the application:
   ```powershell
   docker-compose up --build
   ```

3. Access the application at: http://localhost:8080

4. Login with:
   - **Admin**: username: `admin`, password: `admin123`
   - **User**: username: `user`, password: `user123`

5. Stop the application:
   ```powershell
   docker-compose down
   ```

### Option 2: Using Maven (Local Development)

1. Ensure PostgreSQL is running locally on port 5432 with:
   - Database: `studentdb`
   - Username: `postgres`
   - Password: `postgres`

2. Build and run:
   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

3. Access at: http://localhost:8080

## Project Features

### ✅ Task 1: Authentication & Authorization (Spring Security)
- Role-Based Access Control (RBAC)
- Two roles: ADMIN (full access) and USER (read-only)
- Secure login/logout
- Password encryption with BCrypt
- Method-level security with @PreAuthorize

### ✅ Task 2: Docker Containerization
- Multi-stage Dockerfile for optimized builds
- Docker Compose with PostgreSQL database
- Health checks and automatic startup
- Persistent data volumes

### ✅ Task 3: Database Migration (Liquibase)
- Version-controlled database schema
- Automatic migrations on startup
- Sample data pre-loaded
- Separate changesets for tables and data

## Testing the Application

### As ADMIN:
1. Login with `admin/admin123`
2. View all students
3. Click "Add New Student" - ✅ Accessible
4. Add a student and save
5. Delete a student - ✅ Accessible

### As USER:
1. Logout and login with `user/user123`
2. View all students
3. Try to access "Add New Student" - ❌ Access Denied
4. Cannot see delete buttons - ❌ Hidden

## Architecture

```
Browser
   ↓
Spring Security (Authentication & Authorization)
   ↓
Controller Layer (@PreAuthorize)
   ↓
Service Layer (Business Logic)
   ↓
Repository Layer (JPA)
   ↓
PostgreSQL Database (via Liquibase migrations)
```

## File Structure Highlights

- **Security**: `SecurityConfig.java`, `CustomUserDetailsService.java`
- **Entities**: `User.java`, `Student.java`
- **Controllers**: `StudentController.java`, `HomeController.java`
- **Templates**: `login.html`, `students.html`, `student-form.html`
- **Docker**: `Dockerfile`, `docker-compose.yaml`
- **Liquibase**: `db/changelog/*.xml`

## Troubleshooting

### Port 8080 already in use:
```powershell
# Change port in application.properties
server.port=8081
```

### Cannot connect to database:
```powershell
# Check PostgreSQL container
docker ps
docker logs studentdb
```

### Liquibase errors:
```powershell
# Clear Liquibase lock
docker exec -it studentdb psql -U postgres -d studentdb -c "DELETE FROM databasechangeloglock;"
```

## Sample Data Included

**Students:**
- John Doe (CSE-001)
- Jane Smith (CSE-002)
- Bob Johnson (CSE-003)

**Users:**
- admin/admin123 (ADMIN role)
- user/user123 (USER role)

## Next Steps

1. ✅ Run the application
2. ✅ Test login with both users
3. ✅ Verify ADMIN can add/delete students
4. ✅ Verify USER can only view students
5. ✅ Check Docker containers are running
6. ✅ Verify Liquibase migrations applied successfully

Enjoy your secured Spring Boot application! 🎉
