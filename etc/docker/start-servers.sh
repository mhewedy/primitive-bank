#!/usr/bin/env bash

java -jar target/primitive-bank-*.jar --spring.profiles.active=demo & \
    npm start --prefix src/main/resources/static/web -- --host 0.0.0.0
