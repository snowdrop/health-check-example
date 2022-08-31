FROM openjdk:11
COPY target/*.jar health-check.jar
CMD java ${JAVA_OPTS} -jar health-check.jar
