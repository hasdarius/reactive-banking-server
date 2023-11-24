FROM amazoncorretto:17-alpine-jdk
MAINTAINER HasDarius "has.darius17@yaho.com"
COPY build/libs/*.jar /app/app.jar
ENTRYPOINT ["java"]
CMD ["-jar", "/app/app.jar"]
EXPOSE 8080