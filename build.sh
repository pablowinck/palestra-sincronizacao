#!/bin/zsh

quarkus build --native -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=docker && docker build -t pablowinck/palestra-sincronizacao:1.0 -f src/main/docker/Dockerfile.native-micro . && docker push pablowinck/palestra-sincronizacao:1.0

