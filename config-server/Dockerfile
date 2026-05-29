FROM eclipse-temurin:17-jdk
# On installe curl pour que Docker puisse vérifier si le service est prêt
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]