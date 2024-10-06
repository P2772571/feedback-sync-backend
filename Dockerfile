# Stage 1: Build the application with Amazon Corretto 21
FROM amazoncorretto:21 AS builder

# Install tar, gzip, wget, and the required version of Maven (3.6.3 or later)
RUN yum update -y && \
    yum install -y wget tar gzip && \
    wget https://archive.apache.org/dist/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz && \
    tar xzvf apache-maven-3.6.3-bin.tar.gz && \
    mv apache-maven-3.6.3 /opt/maven && \
    ln -s /opt/maven/bin/mvn /usr/bin/mvn

# Set the working directory inside the container
WORKDIR /build

# Copy the project files to the container
COPY ./pom.xml ./pom.xml
COPY ./src ./src

# Build the application (without running tests)
RUN mvn clean package -DskipTests

# Debug: List files in /build/target to verify the JAR file
RUN ls -al /build/target

# Stage 2: Create the final Docker image with Amazon Corretto JDK 21
FROM amazoncorretto:21 AS runtime

# Correct the JAR file name
COPY --from=builder /build/target/feedbacksync-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the port the application runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
