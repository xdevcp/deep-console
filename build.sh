#!/bin/bash

mvn -Prelease-app -Dmaven.test.skip=true clean install -U

java -jar console/target/app.jar --server.port=8000 --spring.profiles.active=dev
