FROM openjdk:21-jdk

COPY build/libs/userserver-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.profiles.active=local"]

ENV TZ=Asia/Seoul
