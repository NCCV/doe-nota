FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD doenota-api-0.4.1.RELEASE.jar app.jar
RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/urandom", "-jar", "/app.jar"]