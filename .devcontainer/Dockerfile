FROM mcr.microsoft.com/devcontainers/java:21

RUN apt-get update && apt-get install -y maven

ENV MAVEN_HOME=/usr/share/maven
ENV PATH="$MAVEN_HOME/bin:${PATH}"

WORKDIR /workspace
