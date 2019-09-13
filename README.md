# Trees 

![Front page image](https://images.unsplash.com/photo-1552645883-c036db742442?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=2850&q=80)

## About 
First and foremost, this is a project to aid exercises in two courses at Kristiania 
Universitly College (namely "DevOps in the Cloud" and "Enterprise Programming 2"). 
The idea in both of these subjects is to have a "pet project" and apply the concepts 
learned in lectures on it. Although not intended by the lecturers, I have combined 
two subjects, as the seem to go quite nicely together. 

## Topic 
The of my choice is plants. The idea is to store a lot of plants in a database and 
then displaying them on a map. I have not come up with many practical use cases for 
this yet. However, the topic somehow intrigues me. As this project is just used 
as a playground for new knowledge, I have concluded the topic does not really need 
to be interesting to other people. In other words, the topic intriguing me is more 
than good enough a reason to work on the project.

## Local Development:
* add `.env` with the `MAPBOX_KEY=your_mapbox_key`  

## Technology 
The project is split between a backend, written in [Kotlin](http://kotlinlang.org) 
using the [Spring Boot](https://spring.io/projects/spring-boot) framework. There is 
also a minimal frontend written in [React](https://reactjs.org/) and 
[Typescript](https://www.typescriptlang.org/).

The backend provides a REST-API. The backend is expected to expand a lot after each 
lecture. As neither the DevOps- or Enterprise courses have strong requirements on 
frontend-code, that part is likely to be given little attention compared to the rest 
of the application.    



TODO: 
- [X] Finish wrapped responses on _all endpoints_ 
- [X] noarg heller enn default-verdier paa entities 
- [ ] Implement keyset pagination 
- [ ] Some expand filtering 
- [ ] Override spring sin haandtering av exceptions 
- [ ] Legge inn en validation-annotation
- [ ] Ikke sende stacktrace  

