# BUILD STAGE
FROM gradle:7.6.4-jdk17-alpine AS build

WORKDIR /app

COPY . .

RUN gradle build --no-daemon -x test

# RUN STAGE
FROM openjdk:17-alpine

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
