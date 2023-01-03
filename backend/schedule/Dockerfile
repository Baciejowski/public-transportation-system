#### Build app
FROM amazoncorretto:19 as build
WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x ./mvnw
RUN ./mvnw verify --fail-never

COPY src src
RUN ./mvnw install -DskipTests

#### Provide executable jar
FROM amazoncorretto:19

RUN mkdir -p /opt/app
COPY --from=build /app/target/*.jar /opt/app/epk-schedule.jar

ENTRYPOINT ["java", "-jar", "/opt/app/epk-schedule.jar"]

