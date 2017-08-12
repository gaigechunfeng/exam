@cd /d %~dp0..
@set exam_home=%cd%
@set jre_home=%exam_home%\jdk1.8.0_121\jre
@if exist set-java.bat call set-java.bat

@set maven_home=%exam_home%\apache-maven-3.3.9
@if exist set-maven.bat call set-maven.bat

@if exist lib rd /s /q lib

call %maven_home%\bin\mvn dependency:copy-dependencies -DoutputDirectory=%exam_home%\lib -DincludeScope=compile
call %maven_home%\bin\mvn package -Dmaven.test.skip=true

@set exam_classpath=./lib/ant-1.10.1.jar;./lib/ant-launcher-1.10.1.jar;./lib/aspectjrt-1.8.10.jar;./lib/aspectjweaver-1.8.10.jar;./lib/commons-codec-1.10.jar;./lib/commons-collections-3.2.1.jar;./lib/commons-fileupload-1.3.2.jar;./lib/commons-io-2.2.jar;./lib/commons-lang-2.4.jar;./lib/commons-logging-1.2.jar;./lib/druid-1.0.29.jar;./lib/exam-core-1.0.1.jar;./lib/exam-www-1.0.1.jar;./lib/guava-21.0.jar;./lib/jackson-annotations-2.8.0.jar;./lib/jackson-core-2.8.7.jar;./lib/jackson-databind-2.8.7.jar;./lib/javax.servlet-api-3.1.0.jar;./lib/jconsole-1.8.0.jar;./lib/jetty-http-9.4.2.v20170220.jar;./lib/jetty-io-9.4.2.v20170220.jar;./lib/jetty-security-9.4.2.v20170220.jar;./lib/jetty-server-9.4.2.v20170220.jar;./lib/jetty-servlet-9.4.2.v20170220.jar;./lib/jetty-util-9.4.2.v20170220.jar;./lib/jetty-webapp-9.4.2.v20170220.jar;./lib/jetty-xml-9.4.2.v20170220.jar;./lib/jxl-2.6.12.jar;./lib/log4j-1.2.14.jar;./lib/logback-classic-1.1.7.jar;./lib/logback-core-1.1.7.jar;./lib/mysql-connector-java-5.1.30.jar;./lib/slf4j-api-1.7.8.jar;./lib/spring-aop-4.3.7.RELEASE.jar;./lib/spring-beans-4.3.7.RELEASE.jar;./lib/spring-context-4.3.7.RELEASE.jar;./lib/spring-core-4.3.7.RELEASE.jar;./lib/spring-expression-4.3.7.RELEASE.jar;./lib/spring-jdbc-4.3.7.RELEASE.jar;./lib/spring-tx-4.3.7.RELEASE.jar;./lib/spring-web-4.3.7.RELEASE.jar;./lib/spring-webmvc-4.3.7.RELEASE.jar;./lib/tools-1.8.0.jar;./lib/velocity-1.7.jar
%jre_home%\bin\java -cp %exam_classpath% cn.gov.baiyin.court.www.Pack
