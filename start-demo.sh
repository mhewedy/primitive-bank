#!/usr/bin/env bash

# to kill all sub processes when this script killed
trap 'kill -HUP 0' EXIT

if [ "$1" != "-skip--build" ]
then
    mvn clean package -DskipTests
fi

java $JAVA_OPTS -jar target/primitive-bank-*.jar --spring.profiles.active=demo &

cd src/main/resources/static/web; ng serve; cd - &

# Point the browser to http://localhost:4200/
