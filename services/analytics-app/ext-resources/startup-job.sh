#!/bin/bash
clear
echo "Launching Enablix Analytics app."

java -Xms128m -Xmx512m -cp "./lib/lib_linux64/NeticaJ.jar" -DrunAsJob=true -Dtask.scheduler.enabled=false -DbaseDir=. -DdataDir=. -Dlog4j.configuration=file:"./config/properties/log4j.properties" -Djava.library.path=./lib/lib_linux64 -Dui.cache.resources.flag=false -Dtask.scheduler.enabled=false -jar analytics-app-1.0-SNAPSHOT.jar --server.port=7070
