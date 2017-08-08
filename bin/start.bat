@cd /D %~dp0
@set EXAM_HOME=%cd%\..
@set JAVA_HOME=%EXAM_HOME%\jre
@set CLASS_PATH=%JAVA_HOME%\lib

@call "%EXAM_HOME%\bin\set-classpath.bat"
%JAVA_HOME%\bin\java -cp %exam_classpath% cn.gov.baiyin.court.www.App