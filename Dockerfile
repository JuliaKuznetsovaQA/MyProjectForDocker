FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY /build/libs/MyProjectForDocker-1.0-SNAPSHOT.jar /app/file.jar
ENTRYPOINT ["java", "-jar", "file.jar"]