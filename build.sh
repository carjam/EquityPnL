#!/bin/sh
mvn clean
mvn package
mkdir -p target/dependency
cd ./target/dependency
jar -xf ../*.jar
cd ../..
