FROM ubuntu:20.04

ENV DEBIAN_FRONTEND noninteractive

RUN apt-get update
RUN apt-get install -y \
      build-essential \
      default-jdk \
      maven \
      texlive-latex-base

WORKDIR /app

COPY . /app

RUN mvn install

RUN useradd -m app
USER app

CMD java -jar target/Server-0.0.0.jar
