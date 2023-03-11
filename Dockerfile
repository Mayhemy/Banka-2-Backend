##
## Build stage
##
#FROM docker.io/maven:3.8.5-openjdk-17@sha256:e299c7dd06091386e49ce6658b30d61bbf62b4287608e156c3468b52c0c78305 AS build
#COPY src /home/app/src
#COPY pom.xml /home/app
#RUN --mount=type=cache,target=/root/.m2 mvn -Dmaven.test.skip -f /home/app/pom.xml clean package
#
##
## Package stage
##
#FROM docker.io/openjdk:17-alpine@sha256:a996cdcc040704ec6badaf5fecf1e144c096e00231a29188596c784bcf858d05
#COPY --from=build /home/app/target/Banka2Backend-0.0.1-SNAPSHOT.jar /app.jar
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","/app.jar"]
FROM maven:3.8.5-openjdk-17 as maven

WORKDIR /home/app/
COPY src /home/app/src
COPY pom.xml /home/app

RUN target=/root/.m2 mvn -f /home/app/pom.xml clean package -Dmaven.test.skip=true #posle 10 dugih godina ovaj skip test sluzi da skipujem testove
#kada se pokrene run jer baza na localhostu ne postoji a kontejner koji pokrecemo propertije u buildu ne uzima iz docker composa nego tek u runu

FROM tomcat:10-jdk17-openjdk
#ARG TOMCAT_FILE_PATH=/docker

#Data & Config - Persistent Mount Point
ENV APP_DATA_FOLDER=/var/lib/banka2

ENV CATALINA_OPTS="-Xms1024m -Xmx4096m -XX:MetaspaceSize=512m -XX:MaxMetaspaceSize=512m -Xss512k"

#Move over the War file from previous build step
WORKDIR /usr/local/tomcat/webapps/
COPY --from=maven /home/app/target/Banka2Backend-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/

WORKDIR $APP_DATA_FOLDER

EXPOSE 8080
ENTRYPOINT ["catalina.sh", "run"]
