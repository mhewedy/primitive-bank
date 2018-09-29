FROM maven:3.5.4-jdk-8-alpine

MAINTAINER Muhammad Hewedy <mhewedy@gmail.com>

#install npm
RUN apk add --update nodejs nodejs-npm

#config and copy
RUN mkdir -p /app
WORKDIR /app
COPY .  /app

# run package managers
RUN mvn clean package -DskipTests
RUN cd src/main/resources/static/web; npm install; cd -

EXPOSE 4200

CMD ["etc/docker/start-servers.sh"]
