FROM openjdk:11.0.9.1-jre-buster

ARG APPLICATION_NAME=website
ARG APPLICATION_USER=ktor

RUN useradd $APPLICATION_USER

RUN mkdir /app
RUN chown -R $APPLICATION_USER /app

USER $APPLICATION_USER

COPY ./build/libs/$APPLICATION_NAME.jar /app/$APPLICATION_NAME.jar
WORKDIR /app

CMD ["java", "-server", "-jar", "$APPLICATION_NAME.jar"]
