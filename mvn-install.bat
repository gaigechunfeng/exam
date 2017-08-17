@cd /d %~dp0
@set EXAM_HOME=%cd%
@set MAVEN_HOME=%EXAM_HOME%\apache-maven-3.3.9
@if exist set-maven.bat call set-maven.bat

@set JAVA_HOME=%EXAM_HOME%\jdk1.8.0_121
@if exist set-java.bat call set-java.bat

@set CLASS_PATH=%JAVA_HOME%\lib

call %MAVEN_HOME%\bin\mvn install -Dmaven.test.skip=true
call %EXAM_HOME%\exam-core\mvn-install.bat