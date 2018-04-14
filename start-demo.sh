#!/usr/bin/env bash

mvn clean package

java $JAVA_OPTS -jar target/primitive-bank-*.jar --spring.profiles.active=demo
