FROM  openjdk:13

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8081/tcp

ENTRYPOINT ["java", "-jar", "/app.jar", "-Xmx:256m"]