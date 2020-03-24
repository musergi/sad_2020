#!/bin/bash

javac -d build src/*.java
java -classpath build TestReadLine