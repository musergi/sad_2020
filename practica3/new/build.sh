#!/bin/bash

javac src/Main.java \
    src/server/*.java \
    src/client/*.java \
    src/client/ui/*.java \
    -d bin