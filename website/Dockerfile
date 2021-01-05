FROM openjdk:11-jre-slim

ARG APPLICATION_USER=ktor

RUN useradd $APPLICATION_USER

RUN mkdir /app
RUN chown -R $APPLICATION_USER /app

USER $APPLICATION_USER

COPY ./build/libs/website.jar /app/website.jar
WORKDIR /app

CMD ["java", "-server", "-jar", "website.jar"]
