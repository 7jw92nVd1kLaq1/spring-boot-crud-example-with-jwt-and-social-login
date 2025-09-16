# spring-boot-crud-example-with-jwt-and-social-login

## Overview
This project is a simple Spring Boot backend application that demonstrates how to implement CRUD operations with JWT authentication and social login (Kakao). The application uses Spring Security for authentication and authorization, and it provides RESTful APIs for managing users.

## Features
- User registration and login with JWT authentication
- Social login with Kakao
- CRUD operations for user management
- Secure endpoints with role-based access control
- Use of Spring Data JPA for database interactions
- PostgreSQL as the database

## Setup (VS Code)
1. Clone the repository:
    ```bash
    git clone https://github.com/7jw92nVd1kLaq1/spring-boot-crud-example-with-jwt-and-social-login.git
    ```
2. Navigate to the project directory:
    ```bash
    cd spring-boot-crud-example-with-jwt-and-social-login
    ```
3. Configure the database connection in `src/main/resources/application.properties`.
```
spring.application.name=backend
# PostgreSQL Configuration
spring.datasource.url=jdbc:postgresql://localhost:5433/basicrud
spring.datasource.username=postgres
spring.datasource.password=password
spring.datasource.driver-class-name=org.postgresql.Driver

# Hibernate Configuration
spring.jpa.hibernate.ddl-auto=create 
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

logging.level.org.springframework.security=DEBUG

# Fix the secret below for your application
# You can generate a new secret key at https://jwtsecrets.com/#generator
# Make sure to set the secret length to 256 bits when generating a key
jwt.secret=[your_secret_key_here]
# Fix the expiration times below for your application
jwt.accessExpirationInMinutes=15
jwt.refreshExpirationInDays=7
```

4. Download the extension "Extension Pack for Java" in VS Code.
5. Open the project in VS Code.
6. Run the application using the Spring Boot extension or by executing:
    ```bash
    ./gradlew bootRun
    ```
7. The application will be accessible at `http://localhost:8080`.