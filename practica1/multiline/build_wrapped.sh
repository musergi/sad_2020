#!/bin/bash

JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-13.0.2.jdk/Contents/Home
gcc -c -fPIC -I${JAVA_HOME}/include -I${JAVA_HOME}/include/darwin src/ConsoleUtils.c -o bin/ConsoleUtils.o
gcc bin/ConsoleUtils.o -shared -o bin/libconsole_utils.so
rm bin/ConsoleUtils.o