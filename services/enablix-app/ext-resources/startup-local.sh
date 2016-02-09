#!/bin/bash
clear
echo "Launching Enablix app."
java -Xms64m -Xmx128m -DbaseDir=./../../ext-resources -Dui.assets.external.paths=./../../../../web/studio/ -DdataDir=. -Dlog4j.configuration=file:"./config/properties/log4j.properties" -jar enablix-app-1.0-SNAPSHOT.jar --server.port=8080