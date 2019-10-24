# Trees 

## Running app 
`docker run -p 8080:8080 olaven/org.olaven.enterprise.trees`
__note__: using http, not https. For now :) 
## Local setup:
* add `/frontend/.env` with the `MAPBOX_KEY=your_mapbox_key`
## Running the application 
`mvn package && docker-compose up`

## About GraphQL 
* Main endpoint: http://localhost:8080/graphql
* UI accessible at http://localhost:8080/graphiql
* UI graph representation at http://localhost:8080/voyager

TODO: 
- [X] Finish wrapped responses on _all endpoints_ 
- [X] noarg heller enn default-verdier paa entities 
- [X] Implement keyset pagination 
- [X] Some expand filtering 
- [X] Override spring sin haandtering av exceptions 
- [X] Legge inn en validation-annotation
- [X] Ikke sende stacktrace  
- [X] Cut "/org.olaven.enterprise.trees/" from path
- [X] Redirection 
- [X] Conditional requests
- [X] Circuit breaker 
- [X] Test mocking 
- [X] Caching 
- [ ] constructor validation
- [ ] Add code coverage check 
- [X] Trello Integration
- [X] Dockerize app
- [X] GraphQL -> samme funksjonalitet som REST 
- [ ] Microservice 
    - [ ] Docker compose 
    - [ ] Gateway 
    - [ ] Service Discovery 
    - [ ] Load Balancer 
    - [ ] RabbitMQ
- [ ] After microservice refactor 
    - [ ] Make sure cache works 
    - [ ] Retry terraform/travis deploy 
    - [ ] Re-implement metrics 
    - [ ] Hystrix (circuit breaker) should be going to _outgoing_ requests
- [ ] Postgres database 
- [ ] Flyway 
- [ ] Mock Eureka away locally (look at mock exam)
