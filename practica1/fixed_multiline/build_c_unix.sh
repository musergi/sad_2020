#!/bin/bash

# Edit java home to the proper path
JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64/

gcc -c -fPIC -I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux src/ConsoleUtils.c -o bin/ConsoleUtils.o
gcc bin/ConsoleUtils.o -shared -o bin/libconsole_utils.so
rm bin/ConsoleUtils.o
