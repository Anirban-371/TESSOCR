FROM sgrio/java-oracle
MAINTAINER Anirban Dey
RUN apt-get update
RUN apt-get install -y maven
COPY pom.xml /usr/local/services/pom.xml
COPY src /usr/local/services/src
WORKDIR /usr/local/services
RUN mvn package
CMD ["java","-cp","target/ReadEL-0.0.1-SNAPSHOT.jar","com.readEl.OCR.service.App"]