@echo off

rem Copyright 2020

if not exist "%JAVA_HOME%\bin\jps.exe" echo Please set the JAVA_HOME variable in your environment, We need java(x64)! jdk8 or later is better! & EXIT /B 1

setlocal

set "PATH=%JAVA_HOME%\bin;%PATH%"

echo killing app server

for /f "tokens=1" %%i in ('jps -m ^| find "app.app"') do ( taskkill /F /PID %%i )

echo Done!
