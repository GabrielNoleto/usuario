
FROM gradle:9-jdk17-alpine AS BUILD
WORKDIR /app
COPY . .
RUN gradle build --no-daemon


FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=BUILD /app/build/libs/*.jar  /app/usuario.jar
EXPOSE 8080

CMD ["java", "-jar", "usuario.jar"]