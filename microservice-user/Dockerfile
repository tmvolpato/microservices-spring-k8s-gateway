FROM adoptopenjdk/openjdk11:alpine-jre as builder

MAINTAINER Thiago M. Volpato

ENV JAR_FILE=target/*.jar
ADD ${JAR_FILE} application.jar

RUN java -Djarmode=layertools -jar application.jar extract

FROM adoptopenjdk/openjdk11:alpine-jre

COPY --from=builder dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder internal-dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder application/ ./

ARG PORT_APP=8001
ENV ENV_MS_USER_SERVER_PORT $PORT_APP
EXPOSE $ENV_MS_USER_SERVER_PORT

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]