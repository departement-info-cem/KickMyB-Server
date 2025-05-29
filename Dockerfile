FROM maven:3.9.9-amazoncorretto-21-alpine AS build
WORKDIR /workspace/app
COPY src /workspace/app/src
COPY pom.xml /workspace/app
RUN mvn -f /workspace/app/pom.xml  install

FROM --platform=linux/amd64 eclipse-temurin:21-jdk-alpine
COPY --from=build /workspace/app/target/kickmybserver-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]