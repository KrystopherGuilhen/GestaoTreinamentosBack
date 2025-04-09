FROM openjdk:21

# Copia o JAR da aplicação para o container
COPY target/treinamento-1.0.0.jar /app/app.jar

# Define o diretório de trabalho
WORKDIR /app

# Comando para rodar a aplicação com depuração habilitada
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "app.jar", "--spring.profiles.active=prod"]