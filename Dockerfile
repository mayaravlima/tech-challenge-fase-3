FROM eclipse-temurin:17-jdk-alpine as build

RUN mkdir app

COPY . ./app

RUN apk add --no-cache bash procps curl tar

# common for all images
ENV MAVEN_HOME /usr/share/maven

COPY --from=maven:3.9.5-eclipse-temurin-11 ${MAVEN_HOME} ${MAVEN_HOME}
COPY --from=maven:3.9.5-eclipse-temurin-11 /usr/local/bin/mvn-entrypoint.sh /usr/local/bin/mvn-entrypoint.sh
COPY --from=maven:3.9.5-eclipse-temurin-11 /usr/share/maven/ref/settings-docker.xml /usr/share/maven/ref/settings-docker.xml

RUN ln -s ${MAVEN_HOME}/bin/mvn /usr/bin/mvn

ARG MAVEN_VERSION=3.9.5
ARG USER_HOME_DIR="/root"
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

WORKDIR /app

RUN mvn clean package -DskipTests

RUN cp -p /app/target/*.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/app.jar"]