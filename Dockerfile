# BUILD STAGE
FROM gradle:7.6.4-jdk17-alpine AS build

WORKDIR ./src

COPY . .

RUN gradle build --no-daemon

# RUN STAGE
FROM openjdk:17-alpine

WORKDIR /app

COPY --from=build build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
