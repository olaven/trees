FROM openjdk:latest

<<<<<<< HEAD
WORKDIR app
COPY target/trees.jar .
=======
WORKDIR /app
COPY ./target/trees.jar /app
>>>>>>> 35713952b37ccf873613050b50dc723348c45130
CMD java -jar trees.jar