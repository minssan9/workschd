# docker build -f ./docker/api-Dockerfile -t voyagerss-api-dev

FROM docker.io/openjdk:17
LABEL maintainer="minssan9@gmail.com"

# Make port 8080 available to the world outside this container
EXPOSE 24000

#COPY --from=builder /gradle/api/build/libs/*.jar en9door_api.jar
COPY api-admin/build/libs/*.jar en9door_api.jar

# Run the jar file
#ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILE}", "-jar", "/en9door_api.jar"]
ENTRYPOINT ["java", "-jar", "/en9door_api.jar"]
