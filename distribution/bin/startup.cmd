@echo off

rem Copyright 2020

if not exist "%JAVA_HOME%\bin\java.exe" echo Please set the JAVA_HOME variable in your environment, We need java(x64)! jdk8 or later is better! & EXIT /B 1
set "JAVA=%JAVA_HOME%\bin\java.exe"

setlocal enabledelayedexpansion

set BASE_DIR=%~dp0
rem added double quotation marks to avoid the issue caused by the folder names containing spaces.
rem removed the last 5 chars(which means \bin\) to get the base DIR.
set BASE_DIR="%BASE_DIR:~0,-5%"

set DEFAULT_SEARCH_LOCATIONS="classpath:/,classpath:/config/,file:./,file:./config/"
set CUSTOM_SEARCH_LOCATIONS=%DEFAULT_SEARCH_LOCATIONS%,file:%BASE_DIR%/conf/

set MODE="standalone"
set FUNCTION_MODE="all"
set SERVER=app-server
set MODE_INDEX=-1
set FUNCTION_MODE_INDEX=-1
set SERVER_INDEX=-1


set i=0
for %%a in (%*) do (
   if "%%a" == "-m" ( set /a MODE_INDEX=!i!+1 )
   if "%%a" == "-f" ( set /a FUNCTION_MODE_INDEX=!i!+1 )
   if "%%a" == "-s" ( set /a SERVER_INDEX=!i!+1 )
   set /a i+=1
)

set i=0
for %%a in (%*) do (
   if %MODE_INDEX% == !i! ( set MODE="%%a" )
   if %FUNCTION_MODE_INDEX% == !i! ( set FUNCTION_MODE="%%a" )
   if %SERVER_INDEX% == !i! (set SERVER="%%a")
   set /a i+=1
)

if %MODE% == "standalone" (
    set "JAVA_OPT=%JAVA_OPT% -Xms512m -Xmx512m -Xmn256m"
    set "JAVA_OPT=%JAVA_OPT% -Ddeep.standalone=true"
) else (
    set "JAVA_OPT=%JAVA_OPT% -server -Xms2g -Xmx2g -Xmn1g -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=320m"
    set "JAVA_OPT=%JAVA_OPT% -XX:-OmitStackTraceInFastThrow XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=%BASE_DIR%\logs\java_heapdump.hprof"
    set "JAVA_OPT=%JAVA_OPT% -XX:-UseLargePages"
)

if %FUNCTION_MODE% == "config" (
  set "JAVA_OPT=%JAVA_OPT% -Ddeep.functionMode=config"
)
if %FUNCTION_MODE% == "naming" (
  set "JAVA_OPT=%JAVA_OPT% -Ddeep.functionMode=naming"
)

set "JAVA_OPT=%JAVA_OPT% -Dloader.path=%BASE_DIR%/plugins/health,%BASE_DIR%/plugins/cmdb,%BASE_DIR%/plugins/mysql"

set "JAVA_OPT=%JAVA_OPT% -Ddeep.home=%BASE_DIR%"
set "JAVA_OPT=%JAVA_OPT% -jar %BASE_DIR%\target\%SERVER%.jar"
set "JAVA_OPT=%JAVA_OPT% --spring.config.location=%CUSTOM_SEARCH_LOCATIONS%"
set "JAVA_OPT=%JAVA_OPT% --logging.config=%BASE_DIR%/conf/app-logback.xml"

call "%JAVA%" %JAVA_OPT% app.app %*
