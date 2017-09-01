@echo off

cd ..
call mvn clean package -DskipTests=true

@pause