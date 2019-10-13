#!/bin/bash
echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
docker build . --tag org.olaven.enterprise.trees
docker tag org.olaven.enterprise.trees olaven/org.olaven.enterprise.trees:latest
docker push olaven/org.olaven.enterprise.trees