FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
COPY ${DEPENDENCY}/BOOT-INF/classes/application.properties /app/com/org/os
ENTRYPOINT ["java","-cp","app:app/lib/*","com.org.os.OrderServiceApplication"]