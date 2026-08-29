@echo off
title coderzhang-blog backend (dev)
echo IMPORTANT: open tunnel.bat FIRST !
echo.

rem Change to your own IDEA JBR path
set "JAVA_HOME=C:\Users\admin\AppData\Local\Programs\IntelliJ IDEA\jbr"

rem Dev database password: MYSQL_PASSWORD in /root/.db_credentials on server
set "DEV_DB_PASSWORD=PUT_YOUR_PASSWORD_HERE"

cd /d "D:\Desktop\agent_util_scripts\coderzhang-blog\backend"
mvn -s ..\mvn-settings.xml spring-boot:run -Dspring-boot.run.profiles=dev
pause >nul
