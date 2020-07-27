FROM ubuntu:20.04

WORKDIR /work

ENV DEBIAN_FRONTEND noninteractive

RUN apt-get update
RUN apt-get install -y \
      build-essential \
      default-jdk \
      maven \
      texlive-latex-base

COPY . /work

RUN mvn install

ENTRYPOINT ["java", "-jar", "target/Server-0.0.0.jar"]
