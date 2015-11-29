@echo off
cd target
dir /b vermilingua*.jar > tmpFile
set /p Jar= < tmpFile
java -jar %Jar% %*
cd ..
