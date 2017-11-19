FROM openjdk:8-jdk-slim
VOLUME /tmp
ADD *.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8081
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]