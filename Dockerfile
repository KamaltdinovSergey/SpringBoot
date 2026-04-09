FROM maven:4.0.0-rc-5-amazoncorretto-25 AS build
COPY pom.xml /build/
WORKDIR /build/
RUN mvn dependency:go-offline
COPY src /build/src/
RUN mvn package -DskipTests

#Run stage
FROM openjdk:27-ea-jdk-oracle
ARG JAR_FILE=/build/target/*.jar
COPY --from=build $JAR_FILE /opt/SpringBoot/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/opt/SpringBoot/app.jar"]