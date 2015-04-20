#!/bin/bash
clear

echo "-----------------Building commons-parent ------------------------"
mvn -f ../commons/commons-parent/pom.xml clean install -DskipTests

echo "-----------------Building services-parent ------------------------"
mvn -f ../services/services-parent/pom.xml clean install -DskipTests


echo "-----------------Building enablix------------------------"
mvn clean install -DskipTests


echo "-----------------Deploying enablix------------------------"
mvn -f ../services/enablix-app/pom.xml clean install -DskipTests -P deployment
 