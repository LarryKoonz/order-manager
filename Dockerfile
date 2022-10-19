FROM adoptopenjdk:12.0.1_12-jdk-openj9-0.14.1

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve


COPY src ./src

EXPOSE 8080

CMD ["./mvnw", "spring-boot:run"]
