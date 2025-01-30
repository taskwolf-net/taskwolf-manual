FROM openjdk:21

COPY /build/libs/manual-1.0.0-SNAPSHOT.jar manual.jar
COPY /locale/ /locale/