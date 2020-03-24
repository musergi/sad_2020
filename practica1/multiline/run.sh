#!/bin/bash

if javac -d bin src/*.java; then
    java -classpath bin Main $1
fi