# Step : Test and package
FROM maven:3.5.3-jdk-8-alpine as target
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src/ /build/src/
RUN mvn package -Dexec.skip=true -Djacoco.skip

# Step : Package image
FROM openjdk:8-jre-alpine
RUN adduser -D nosy
USER nosy
WORKDIR /app
EXPOSE 8081
COPY --from=target /build/target/*.jar /app/nosy-email.jar
CMD java -Djava.security.egd=file:/dev/./urandom -jar nosy-email.jar
