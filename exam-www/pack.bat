@cd /d %~dp0..
@set exam_home=%cd%
@set jre_home=%exam_home%\jdk1.8.0_121\jre
@if exist set-java.bat call set-java.bat

@set maven_home=%exam_home%\apache-maven-3.3.9
@if exist set-maven.bat call set-maven.bat

@if exist lib rd /s /q lib

call %maven_home%\bin\mvn dependency:copy-dependencies -DoutputDirectory=%exam_home%\lib -DincludeScope=compile
call %maven_home%\bin\mvn package -Dmaven.test.skip=true
%jre_home%\bin\java -Djava.ext.dirs=%jre_home%\ext\lib;lib cn.gov.baiyin.court.www.Pack
