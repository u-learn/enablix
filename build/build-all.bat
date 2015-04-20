echo -----------------Building commons-parent ------------------------
call mvn -f ../commons/commons-parent/pom.xml clean install -DskipTests
echo Exit Code = %ERRORLEVEL%
if not "%ERRORLEVEL%" == "0" pause /b ELSE echo Dependencies in commons-parent installed.

echo -----------------Building services-parent ------------------------
call mvn -f ../services/services-parent/pom.xml clean install -DskipTests
echo Exit Code = %ERRORLEVEL%
if not "%ERRORLEVEL%" == "0" pause /b ELSE echo Dependencies in services-parent installed.


echo -----------------Building enablix------------------------
call mvn clean install -DskipTests
echo Exit Code = %ERRORLEVEL%
if not "%ERRORLEVEL%" == "0" pause /b ELSE echo Dependencies in enablix installed.


echo -----------------Deploying enablix------------------------
call mvn -f ../services/enablix-app/pom.xml clean install -DskipTests -P deployment
echo Exit Code = %ERRORLEVEL%
if not "%ERRORLEVEL%" == "0" pause /b ELSE echo Dependencies in enablix installed.
 