#### Build app
FROM amazoncorretto:17 as build
WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x ./mvnw
RUN ./mvnw verify --fail-never

COPY src src
RUN ./mvnw install -DskipTests

#### Provide executable jar
FROM amazoncorretto:17

RUN mkdir -p /opt/app
COPY --from=build /app/target/*.jar /opt/app/epk-account.jar

ENTRYPOINT ["java", "-jar", "/opt/app/epk-account.jar"]
