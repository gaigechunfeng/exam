@echo off
@cd /D %~dp0..
@set mysql_home=%cd%\mysql
"%mysql_home%\bin\mysql" -uroot -proot