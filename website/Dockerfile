FROM openjdk:11.0.9.1-jre-buster

ENV APPLICATION_NAME website
ENV APPLICATION_USER ktor

RUN adduser -D -g '' $APPLICATION_USER

RUN mkdir /app
RUN chown -R $APPLICATION_USER /app

USER $APPLICATION_USER

COPY ./build/libs/$APPLICATION_NAME.jar /app/$APPLICATION_NAME.jar
WORKDIR /app

CMD $APPLICATION_USER
CMD ["java", "-server", "-jar", ".jar"]
