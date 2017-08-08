@cd /d %~dp0
@set EXAM_HOME=%cd%\..\
@set MAVEN_HOME=%EXAM_HOME%\apache-maven-3.3.9
@set JAVA_HOME=%EXAM_HOME%\jdk1.8.0_121
@set CLASS_PATH=%JAVA_HOME%\lib
call %MAVEN_HOME%\bin\mvn.cmd install -Dmaven.test.skip=true