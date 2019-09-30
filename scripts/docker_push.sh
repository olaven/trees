#!/bin/bash
echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
docker build . --tag trees-image
docker tag trees-image $(DOCKER_USERNAME)/trees-image:latest
docker push $(DOCKER_USERNAME)/trees