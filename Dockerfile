# First stage: build using Maven Wrapper
FROM eclipse-temurin:17 AS build
WORKDIR /app

# Copy the Maven wrapper and build files
COPY pom.xml ./
COPY .mvn .mvn
COPY mvnw ./

# Load dependencies (cache this layer)
RUN ./mvnw dependency:go-offline

# Copy the source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Second stage: create the runtime image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*-jar-with-dependencies.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application with wait-for-it.sh, extending the timeout
CMD ["java", "-jar", "app.jar"]
