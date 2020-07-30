FROM ubuntu:20.04

ENV DEBIAN_FRONTEND noninteractive

RUN apt-get update && apt-get install -y \
    default-jdk \
    maven \
    postgresql-client \
    texlive-latex-base \
 && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY src /app/src
COPY vendor /app/vendor
COPY pom.xml /app/pom.xml

RUN mvn install
RUN mvn compile

RUN useradd -m app
USER app

CMD java -jar target/Server-0.0.0.jar
