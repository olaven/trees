FROM ubuntu:latest

RUN apt-get update -y
RUN apt-get install -y default-jre
RUN apt-get install -y default-jdk
RUN apt-get install -y openjdk-8-jre
#RUN apt-get install -y nodejs

COPY ./target/trees-1.0-SNAPSHOT.jar .
ENTRYPOINT ["java","-jar","trees-1.0-SNAPSHOT.jar"]
