#!/bin/bash
echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
docker build . --tag trees
docker tag trees olaven/trees:latest
docker push olaven/trees