#FROM openjdk:8-jdk-alpine
#RUN addgroup -S spring && adduser -S spring -G spring
#USER spring:spring
#ARG DEPENDENCY=target/dependency
#COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
#COPY ${DEPENDENCY}/META-INF /app/META-INF
#COPY ${DEPENDENCY}/BOOT-INF/classes /app
#EXPOSE 8084
#ENTRYPOINT ["java","-cp","app:app/lib/*","com.companyx.equity.MainApplicationClass"]

FROM maven:3.6.3-jdk-11-slim AS builder

RUN apt-get update \
    && apt-get -y install make git python python-dev tzdata python-pip \
    && curl -Ls \
        https://codeclimate.com/downloads/test-reporter/test-reporter-latest-linux-amd64 \
        -o /usr/local/bin/cc-test-reporter && \
        chmod +x /usr/local/bin/cc-test-reporter

WORKDIR /app

COPY ./pom.xml /app/pom.xml
#RUN mvn --no-transfer-progress dependency:go-offline

#COPY . /app
#WORKDIR /app

#RUN mvn --no-transfer-progress -DskipTets=true package

#FROM openjdk:11.0.10-jdk-slim
#RUN apt-get update \
#    && apt-get install -y awscli \
#    && rm -rf /var/lib/apt/lists/*
#COPY --from=builder /app/target/equity-1.0-SNAPSHOT.jar /app/

