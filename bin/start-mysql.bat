@cd /D %~dp0
@set mysql_home=%cd%\..\mysql
call "%mysql_home%\bin\mysqld.exe" --defaults-file="%mysql_home%\my.ini"