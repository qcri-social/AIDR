AIDR Collector Installation Instructions
----------------------------------------

Required technologies
---------------------
* Apache Maven (Get the latest version from http://maven.apache.org/)
* Apache Tomcat (Get the latest version from http://tomcat.apache.org/) OR GlassFish (Get the latest version from https://glassfish.java.net/)
* Redis (Get the latest version from http://redis.io/)

Building 
--------
Pre-requisite: Maven should be installed.

AIDR-Collector is a maven based project. Project POM file specifies all the dependencies that are required for the building process. To build, go to the root directory of the project where POM.xml is located and run "mvn install" command. This process initiates downloading process of required packages/libraries from the maven central repository, and may take a few minutes. A successful build will create a target folder on the project root directory and a couple of other files too.  You must find a file "aidr-collector-1.0.war" if the building process successfully completed.

Deployment
----------
Pre-requisite: Tomcat/Glassfish server must be installed and running.

Deployment on Tomcat:
Simply copy and paste the aidr-collector-1.0.WAR file to the <tomcat-root>/webapps directory of the tomcat server. It will automatically deploy the application.

Deployment on Glassfish:
Go to the admin page of your Glassfish server, usually it can be accessed through http://localhost:4848/. Login and go to the applications tab, choose deploy option and browse to the "aidr-collector-1.0.war" file and click deploy.


Running
-------
The AIDR Collector has a RESTFul API, that means all the operations have their corresponding REST services. For more details regarding the API, please refer to this google doc (https://docs.google.com/document/d/1mOlhFDoVCAGOE7gvKQLKwH04P4_b0jyBF7orTQ7ca28/pub). AIDR Collector publishes collected tweets to Redis channels which named after collection codes, like, "FetcherChannel.Sandy2013". You can explore the code for more details. 
