package com.example.feedbacksync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FeedbackSyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeedbackSyncApplication.class, args);
    }

}


# Database configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/feedbacksync_db
spring.datasource.username=admin
spring.datasource.password=admin
spring.datasource.driver-class-name=org.postgresql.Driver

# Hibernate/JPA
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Spring Security Configuration
spring.security.user.name=admin
spring.security.user.password=admin

# JWT Configuration
spring.app.jwt-secret=mySecretKeyhjgrhjeFGHJsfKDFVKJDGFDFDSVFDSGFDVFvjvsdfv
spring.app.jwt-expiration-ms=300000
spring.app.jwt-refresh-expiration-ms=86400000

# Logging Configuration
logging.level.org.springframework=INFO
logging.level.com.feedback=DEBUG




<!--JWT Dependencies-->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>0.12.6</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>0.12.6</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId> <!-- or jjwt-gson if Gson is preferred -->
            <version>0.12.6</version>
            <scope>runtime</scope>
        </dependency>



<!--Spring Security 6-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>

        <!--PostgreSQL -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>
        <!-- Redis-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>