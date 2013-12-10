AIDR Trainer Pybossa Installation Instructions
----------------------------------------

Required technologies
---------------------
1- Apache Maven (Get the latest version from http://maven.apache.org/)
2- Apache Tomcat (Get the latest version from http://tomcat.apache.org/) OR GlassFish ( Get the latest version from https://glassfish.java.net/)


Building
--------
Pre-requisite: Maven should be installed.
pre-requisite: AIDR predict database and AIDR scheduler database. predcit database script should be located in AIDR-Predict installtion details.
               AIDR Scheduler database script should be located in AIDR-Trainer-API.

AIDR-Trainer-Pybossa is a maven based project. Project POM file specifies all the dependencies that are required for the building process. To build, go to the root directory of the project where POM.xml is located and run "mvn install" command. This process initiates downloading process of required packages/libraries from the maven central repository, and may take a few minutes. A successful build will create a target folder on the project root directory and a couple of other files too.  You must find a file "AIDRTrainerPybossa.war" if the building process successfully completed.

Deployment
----------
Pre-requisite: Tomcat/Glassfish server must be installed and running.

Deployment on Tomcat:
Go to tomcat admin page, and, load AIDRTrainerPybossa.war. It will automatically deploy the application.

Deployment on Glassfish:
Go to the admin page of your Glassfish server, usually it can be accessed through http://localhost:4848/. Login and go to the applications tab, choose deploy option and browse to the "AIDRTrainerPybossa.war" file and click deploy.


Running
-------
The AIDRTrainerPybossa has dependency on AIDR-Traininer-API & AIDR. The below configurations should set up before/after to run AIDR Trainer Pybossa.
1. AIDR-Traininer-API should be running
2. User should have at least one active collection with tagging via aidr.qcri.org


