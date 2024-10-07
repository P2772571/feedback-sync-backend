# Feedback Sync

## Description
Feedback Sync is a Spring Boot application designed to manage user authentication, registration, and feedback synchronization. It leverages JWT for secure token-based authentication and provides endpoints for user registration, login, password management, and logout.

## Project's Packages
- `com.example.feedbacksync.controller`: Contains the REST controllers for handling HTTP requests.
- `com.example.feedbacksync.config`: Contains the configuration classes for Spring Security.
- `com.example.feedbacksync.service`: Contains the service layer for business logic.
- `com.example.feedbacksync.entity`: Contains the data models/entities.
- `com.example.feedbacksync.repository`: Contains the repository interfaces for database operations.
- `com.example.feedbacksync.exception`: Contains custom exceptions and exception handlers.
- `com.example.feedbacksync.payloads`: Contains data transfer objects for request and response payloads.
- `com.example.feedbacksync.jwt`: Contains classes for JWT authentication and authorization.
- `com.example.feedbacksync.seeders`: Contains classes for seeding the database with initial Users data.

## Prerequisites
- Java 17 or higher
- Maven 3.9.8 or higher
- Jwt configuration
- PostgreSQL@16 installed and running
- IntelliJ IDEA or any other Java IDE
- Docker

## Installation
Before running the application, ensure that you have the prerequisites installed.

### Using Docker

#### Database Setup
1. Pull the postgres image:
    ```sh
    docker pull postgres
    ```
2. Run the postgres container:
    ```sh
    docker run --name feedback-sync-db -e POSTGRES_PASSWORD=your_password -d -p 5432:5432 postgres
    ```
    Replace `your_password` with your desired password.
3. Create the database:
    ```sh
    docker exec -it feedback-sync-db psql -U postgres
    ```
4. Create the database:
    ```sql
    CREATE DATABASE feedbacksync_db;
    ```
5. Create the user:
    ```sql
    CREATE USER feedback_sync_user WITH ENCRYPTED PASSWORD 'your_password';
    ```
    Replace `your_password` with your desired password.
6. Grant privileges to the user:
    ```sql
    GRANT ALL PRIVILEGES ON DATABASE feedback_sync TO feedback_sync_user;
    ```
7. Exit the psql shell:
    ```sql

#### Application Setup
1. Clone the repository:
    ```sh
    git clone https://github.com/P2772571/feedback-sync-backend.git
    cd feedback-sync-backend
    ```
2. Build the Docker image of Feedback Sync:
    ```sh
    docker build -t feedback-sync .
    ```
3. Run the Docker container:
    ```sh
    docker run -p 8080:8080 feedback-sync
    ```
4. The application will be accessible at `http://localhost:8080`.
5. To stop the container, run:
    ```sh
    docker stop <container_id>
    ```
    Replace `<container_id>` with the container ID.
6. To remove the container, run:
    ```sh
    docker rm <container_id>
    ```
7. To remove the image, run:
    ```sh
    docker rmi feedback-sync
    ```

### Using Docker Compose (Recommended)
1. Clone the repository:
    ```sh
    git clone https://github.com/P2772571/feedback-sync-backend.git
    cd feedback-sync-backend
    ```
2. Build the Docker image of Feedback Sync:
    ```sh
    docker build -t feedback-sync .
    ```
3. Run the Docker container using Docker Compose:
    ```sh
    docker-compose up 
    ```
4. Run Docker container and build the image in one command
   ```sh
       docker-compose up --build
   ```
5. The application will be accessible at `http://localhost:8080`.
6. To stop the container, run:
    ```sh
    docker-compose down
    ```
7. To remove the image, run:
    ```sh
    docker rmi feedback-sync
    ```
8. To remove the database volume, run:
    ```sh
    docker volume rm feedback-sync_db
    ```
9. To remove the network, run:
    ```sh
    docker network rm feedback-sync_default
    ```

### Without Docker

1. Clone the repository:
    ```sh
    git clone https://github.com/P2772571/feedback-sync-backend.git
    cd feedback-sync-backend
    ```
2. Install postgresql:
 - Download installer from https://www.postgresql.org/download/
 - Install postgresql
 - Create a database named `feedback_sync`.
 - Update the database configuration in `src/main/resources/application.properties`:
        ```properties
        spring.datasource.url=jdbc:postgresql://localhost:5432/feedback_sync
        spring.datasource.username=your_db_username
        spring.datasource.password=your_db_password
        ```
 - Start the postgresql service
3. Build the project using Maven:
    ```sh
    mvn clean install
    ```

## Run
1. Start the application:
    ```sh
    mvn spring-boot:run
    ```
2. The application will be accessible at `http://localhost:8080`.

## Others
- Ensure to configure your JWT secret and expiration times in `application.properties`:
    ```properties
    jwt.secret=your_jwt_secret
    jwt.expirationMs=3600000
    jwt.refreshExpirationMs=86400000
    ```

- For CORS configuration, update the `@CrossOrigin` annotation in the controller methods as needed.

## License
This project is licensed under the MIT License.