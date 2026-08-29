@echo off
chcp 65001 >nul
title coderzhang-blog backend (dev)
echo 先双击「数据库隧道\tunnel.bat」开隧道！
echo.

rem 改成你自己的 IDEA JBR 路径
set "JAVA_HOME=C:UsersadminAppDataLocalProgramsIntelliJ IDEAjbr"

rem 开发库密码：服务器 /root/.db_credentials 的 MYSQL_PASSWORD
set "DEV_DB_PASSWORD=在这里填密码"

cd /d "D:Desktopagent_util_scriptscoderzhang-blogackend"
mvn -s ..mvn-settings.xml spring-boot:run -Dspring-boot.run.profiles=dev
pause >nul
