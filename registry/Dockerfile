FROM openjdk:8-alpine

ADD target/registry.jar .

CMD java -jar registry.jar --spring.profiles.active=docker