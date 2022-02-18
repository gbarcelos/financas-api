FROM openjdk:8-jre-alpine

WORKDIR /financas-api

COPY target/*.jar /financas-api/app.jar

EXPOSE 8080

CMD java -XX:+UseContainerSupport -Xmx512m -Dspring.profiles.active=stage -jar app.jar --server.port=$PORT