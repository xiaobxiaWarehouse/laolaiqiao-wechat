@echo off
cd ..
mvn clean package -DskipTests=true assembly:assembly

@pause