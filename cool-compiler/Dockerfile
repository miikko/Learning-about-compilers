FROM ubuntu:18.04

RUN apt-get update && apt-get -y install flex bison build-essential csh \
    openjdk-8-jdk libxaw7-dev libc6-i386 lib32stdc++6

ADD student-dist.tar.gz /usr/class/

RUN chown root /usr/class

ENV PATH "$PATH:/usr/class/bin"

WORKDIR /cool
