#!/bin/bash
clear
echo "Launching Enablix Analytics app."
export PATH=$PATH:./lib/lib_linux64/
java -Xms128m -Xmx512m -DrunAsJob=true -DbaseDir=. -DdataDir=. -Dlog4j.configuration=file:"./config/properties/log4j.properties" -Djava.library.path=./lib/lib_linux64/ -Dui.cache.resources.flag=false -jar analytics-app-1.0-SNAPSHOT.jar --server.port=7070