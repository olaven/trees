# Trees 

## Running app 
`docker run -p 8080:8080 olaven/trees`
__note__: using http, not https. For now :) 
## Local setup:
* add `.env` with the `MAPBOX_KEY=your_mapbox_key`


TODO: 
- [X] Finish wrapped responses on _all endpoints_ 
- [X] noarg heller enn default-verdier paa entities 
- [X] Implement keyset pagination 
- [X] Some expand filtering 
- [X] Override spring sin haandtering av exceptions 
- [X] Legge inn en validation-annotation
- [X] Ikke sende stacktrace  
- [X] Cut "/trees/" from path
- [X] Redirection 
- [X] Conditional requests
- [X] Circuit breaker 
- [X] Test mocking 
- [X] Caching 
- [ ] constructor validation
- [ ] Add code coverage test 
- [X] Trello Integration
- [X] Dockerize app
- [ ] Postgres database 
