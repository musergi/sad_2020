#!/bin/bash

if javac -d bin src/*.java; then
    java -classpath bin Main
fi