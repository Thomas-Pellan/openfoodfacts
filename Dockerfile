FROM openjdk:11-jdk

ARG PCK_VERSION

COPY target/openfoodfacts-${PCK_VERSION}.jar docker-openfoodfacts-api.jar
ENTRYPOINT ["java","-jar","/docker-openfoodfacts-api.jar"]

HEALTHCHECK --interval=10m --timeout=10s --retries=5 CMD wget http://localhost:8080/actuator/health | grep UP

