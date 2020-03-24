#!/bin/bash

JAVA_HOME="/usr/lib/jvm/java-1.8.0-oenjdk-amd64/"

gcc -c -fPIC -I/usr/lib/jvm/java-1.8.0-openjdk-amd64/include -I/usr/lib/jvm/java-1.8.0-openjdk-amd64/include/linux src/client/view/utils/client_view_utils_ConsoleUtils.c -o ConsoleUtils.o
gcc  ConsoleUtils.o -shared -o lib/libconsole_utils.so
rm ConsoleUtils.o

javac src/Main.java \
    src/server/Server.java \
    src/server/Chat.java \
    src/client/Client.java \
    src/client/ChatSocket.java \
    src/client/view/Console.java \
    src/client/view/View.java \
    src/client/view/MenuView.java \
    src/client/view/utils/ConsoleUtils.java \
    src/client/input/SequenceParser.java \
    -d bin