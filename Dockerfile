# Stage 1: Build ứng dụng bằng Maven
FROM maven:3.9.9-eclipse-temurin-21 AS build

# Đặt thư mục làm việc trong container
WORKDIR /app

# Copy file pom.xml trước để cache dependency
COPY pom.xml .

# Download dependencies (chỉ chạy lại khi pom.xml thay đổi)
RUN mvn dependency:go-offline -B

# Copy toàn bộ source code
COPY src ./src

# Build ra file jar
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy file JAR đã build (shaded) từ stage 1
COPY --from=build /app/target/*-SNAPSHOT-shaded.jar app.jar

# Expose port (cổng server app lắng nghe)
EXPOSE 1234

# Chạy ứng dụng Java (fat jar tự có Main-Class)
CMD ["java", "-jar", "app.jar"]
