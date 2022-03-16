FROM  openjdk:13

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

EXPOSE 9040/tcp

ENTRYPOINT ["java", "-jar", "/app.jar", "-Xmx:256m"]