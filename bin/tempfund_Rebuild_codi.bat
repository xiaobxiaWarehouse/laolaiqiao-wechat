@echo off
::-----------------------------
::repackage tempfund-server
::-----------------------------
E:
cd \
cd "E:\project\temp_fund\trunk\tempfund-server\"
rd /s /q target
md target
cd ..
call mvn clean package -DskipTests=true

::-----------------------------
::clean tempfund-server in tomcat
::-----------------------------
D:
cd \
cd "D:\TechProgram\apache-tomcat-8.5.5\webapps\"
rd /s /q  ROOT
del /s /q  ROOT.war

::-----------------------------
::copy war to tomcat
::-----------------------------
copy "E:\project\temp_fund\trunk\tempfund-server\target\tempfund-server.war" "D:\TechProgram\apache-tomcat-8.5.5\webapps\"

::-----------------------------
::rename war
::-----------------------------
cd \
cd "D:\TechProgram\apache-tomcat-8.5.5\webapps\"
ren tempfund-server.war ROOT.war

::-----------------------------
::start tomcat
::-----------------------------
cd \
cd "D:\TechProgram\apache-tomcat-8.5.5\bin"
catalina.bat jpda start