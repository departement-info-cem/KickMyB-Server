FROM maven:3.6.0-jdk-11-slim AS build
WORKDIR /workspace/app
COPY src /workspace/app/src
COPY pom.xml /workspace/app
RUN mvn -f /workspace/app/pom.xml  install

FROM --platform=linux/amd64 eclipse-temurin:17-jdk-alpine
COPY --from=build /workspace/app/target/Server-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]