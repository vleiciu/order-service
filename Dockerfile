FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
EXPOSE 8081
ARG LOCATION
ENV LOCATION ${LOCATION}
ARG JAR_FILE
ENV JAR_FILE ${JAR_FILE}
ADD target/${JAR_FILE} ${JAR_FILE}
CMD java -jar /${JAR_FILE} --spring.config.location=classpath:/${LOCATION}