FROM maven:3.9.11-eclipse-temurin-24 AS build

WORKDIR /workspace
COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:24-jre

WORKDIR /app
ENV PORT=8080 \
    PORTFOLIO_STORAGE_PATH=/var/lib/portafolio/evidencias \
    PORTFOLIO_USERS_PATH=/var/lib/portafolio/usuarios.txt

COPY --from=build /workspace/target/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]