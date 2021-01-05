FROM openjdk:11-jre-slim

ARG APPLICATION_USER=spamgourmet

RUN useradd $APPLICATION_USER

RUN mkdir /app
RUN chown -R $APPLICATION_USER /app

USER $APPLICATION_USER

COPY ./build/libs/mailserver.jar /app/mailserver.jar
WORKDIR /app

CMD ["java", "-server", "-jar", "mailserver.jar"]
