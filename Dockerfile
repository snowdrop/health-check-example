FROM openjdk:8u171-alpine3.7
RUN apk --no-cache add curl
COPY target/*.jar health-check.jar
CMD java ${JAVA_OPTS} -jar health-check.jar
