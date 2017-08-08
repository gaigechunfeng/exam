@cd /d %~dp0
@set EXAM_HOME=%cd%\..
@set JAVA_HOME=%EXAM_HOME%\jdk1.8.0_121
@set CLASS_PATH=%JAVA_HOME%\lib
@set MAVEN_HOME=%EXAM_HOME%\apache-maven-3.3.9
@if exist lib rd /s /q lib

call %MAVEN_HOME%\bin\mvn.cmd dependency:copy-dependencies -DoutputDirectory=%EXAM_HOME%\lib -DincludeScope=compile
call %MAVEN_HOME%\bin\mvn.cmd package -Dmaven.test.skip=true
%JAVA_HOME%\bin\java -cp "../resources;../lib/ant-1.10.1.jar;../lib/ant-launcher-1.10.1.jar;../lib/aspectjrt-1.8.10.jar;../lib/aspectjweaver-1.8.10.jar;../lib/commons-fileupload-1.3.2.jar;../lib/commons-io-2.2.jar;../lib/commons-logging-1.2.jar;../lib/druid-1.0.29.jar;../lib/exam-core-1.0.1.jar;../lib/exam-www-1.0.1.jar;../lib/guava-21.0.jar;../lib/jackson-annotations-2.8.0.jar;../lib/jackson-core-2.8.7.jar;../lib/jackson-databind-2.8.7.jar;../lib/javax.servlet-api-3.1.0.jar;../lib/jconsole-1.8.0.jar;../lib/jetty-http-9.4.2.v20170220.jar;../lib/jetty-io-9.4.2.v20170220.jar;../lib/jetty-security-9.4.2.v20170220.jar;../lib/jetty-server-9.4.2.v20170220.jar;../lib/jetty-servlet-9.4.2.v20170220.jar;../lib/jetty-util-9.4.2.v20170220.jar;../lib/jetty-webapp-9.4.2.v20170220.jar;../lib/jetty-xml-9.4.2.v20170220.jar;../lib/logback-classic-1.1.7.jar;../lib/logback-core-1.1.7.jar;../lib/mysql-connector-java-5.1.30.jar;../lib/slf4j-api-1.7.8.jar;../lib/spring-aop-4.3.7.RELEASE.jar;../lib/spring-beans-4.3.7.RELEASE.jar;../lib/spring-context-4.3.7.RELEASE.jar;../lib/spring-core-4.3.7.RELEASE.jar;../lib/spring-expression-4.3.7.RELEASE.jar;../lib/spring-jdbc-4.3.7.RELEASE.jar;../lib/spring-tx-4.3.7.RELEASE.jar;../lib/spring-web-4.3.7.RELEASE.jar;../lib/spring-webmvc-4.3.7.RELEASE.jar;../lib/tools-1.8.0.jar" cn.gov.baiyin.court.www.Pack
