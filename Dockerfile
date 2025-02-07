FROM alpine

COPY /build/libs/manual-1.0.0-SNAPSHOT.jar manual.jar
COPY /locale/ /locale/