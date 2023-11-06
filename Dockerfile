FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
EXPOSE 8081
ARG JAR_FILE=target/OrderService-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} OrderService-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/OrderService-0.0.1-SNAPSHOT.jar"]