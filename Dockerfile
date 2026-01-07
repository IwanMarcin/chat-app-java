FROM maven:3.9.12-eclipse-temurin-25-jammy as build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:25-jdk
COPY --from=build /target/demo-0.0.1-SNAPSHOT.jar demo.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "demo.jar"]