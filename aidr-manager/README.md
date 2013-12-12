AIDR Manager Installation Instructions
----------------------------------------

Required technologies
---------------------
1- Apache Maven (Get the latest version from: http://maven.apache.org/)
2- Apache Tomcat (Get the latest version from: http://tomcat.apache.org/) OR GlassFish (Get the latest version from: https://glassfish.java.net/)
3- MySql (Get the latest version from: http://www.mysql.com/)

Pre-building
------------
Prior to start the project building process make sure you have completed the following steps:

1- Install the MySql database server on your machine and create a database schema, name it for example "aidr_fetch_manager". The schema name can also be changed that requires to change application configurations which specify schema details as described in the next 2 steps.
2- In case of first time deployment, make sure you add the following line in the "spring-servlet.xml" file located under "<project-root>/src/main/webapp/WEB-INF/".
<prop key="hibernate.hbm2ddl.auto">create</prop> 
NOTE:- If the application has previously been deployed and you have MySql schema with tables in place then just remove the above line.
3- In the same file "spring-servlet.xml" database credentials can be specified/changed according to your installation.
4- Apply the following changes to "system.properties" file located under "<app-root>/src/main/resources/"
    (a):    twitter.consumerKey=<put here your Twitter's application consumer key>
            twitter.consumerSecret=<put here your Twitter's application consumer key secret>
    (b):    twitter.callBackURL=<here goes the URL where the applciation is accessible>. e.g., http://localhost:8080/AIDRFetchManager        
    (c):    application.secureUrl=<here goes the URL where the applciation is accessible>. e.g., http://localhost:8080/AIDRFetchManager
    (d):    fetchMainUrl=<here specify the URL of the Collector/Fetcher application>. e.g., http://localhost:8080/AIDRFetcher/webresources/fetcher

Building 
--------
Pre-requisite: Maven should be installed and the pre-building requirements must be completed.

AIDR Manager is a maven based project. Its POM file specifies all the requires dependencies. To build, go to the root directory of the project where POM.xml is located and run "mvn install" command. This process will start downloading required packages/libraries from the maven central repository and may take a few minutes. A successful build creates a directory name "target". You must find a file "aidr-manager-1.0.war" under the target directory.

Deployment
----------
Pre-requisite: Tomcat/Glassfish server must be installed and running.

In case of Tomcat:
Simply copy and paste the aidr-manager-1.0.war file to the <tomcat-root>/webapps directory of the tomcat server. It will automatically deploy the application.

In case of Glassfish:
Go to the admin page of your Glassfish server, usually it can be accessed through http://localhost:4848/. Login and go to the applications tab, choose deploy option and browse to the "aidr-manager-1.0.war" file to deploy it.

Running
-------
If the application has been deployed following the above instructions the the application should be accessible at http://localhost:8080/AIDRFetchManager.
