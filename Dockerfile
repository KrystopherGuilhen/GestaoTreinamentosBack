FROM openjdk:21

COPY target/treinamento-0.0.28.jar /app/app.jar

CMD ["java", "-jar", "-Dspring.profiles.active=prod", "/app/app.jar"]