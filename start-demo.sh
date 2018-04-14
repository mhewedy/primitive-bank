#!/usr/bin/env bash

mvn clean package -DskipTests

java $JAVA_OPTS -jar target/primitive-bank-*.jar --spring.profiles.active=demo &

cd src/main/resources/static/web; ng serve; cd - &
