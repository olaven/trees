#!/bin/bash
echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
docker build . --tag trees-image
docker tag trees-image <dockerhub_username>/<image_name>:latest
docker push olaven/trees