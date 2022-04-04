#!/bin/bash

#package the image into a usable jar file
mvn clean package

#Getting maven package version of the jar generated
version=$(mvn -q -Dexec.executable="echo" -Dexec.args='${project.version}' --non-recursive exec:exec)

#Build docker image with the extracted mvn version
docker build --build-arg PCK_VERSION=$version --tag=docker-openfoodfacts-api:latest .

#Start the image on port 8080 and bind it with inside the container
docker run \
  --network="host" \
  -p8080:8080 docker-openfoodfacts-api:latest