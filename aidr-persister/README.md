AIDR-Persister Installation Instructions
----------------------------------------

Required technologies
---------------------
* Apache Maven (Get the latest version from http://maven.apache.org/)
* GlassFish (Get the latest version from https://glassfish.java.net/)
* Redis (Get the latest version from http://redis.io/)

Building 
--------
Pre-requisite: Maven should be installed.

AIDR-Collector is a maven based project. Project POM file specifies all required dependencies required for the building process. To build, go to the root directory of the project where POM.xml is located and run "mvn install" command. This process initiates downloading process of required packages/libraries from the maven central repository, and may take a few minutes. A successful build will create a target folder on the project root directory and a couple of other files too.  You must find a file "aidr-persister-1.0.war" if the building process successfully completed.

Deployment
----------
Pre-requisite: Glassfish server must be installed and running.

Deployment on Glassfish:
Go to the admin page of your Glassfish server, usually it can be accessed through http://localhost:4848/. Login and go to the applications tab, choose deploy option and browse to the "aidr-persister-1.0.war" file and click deploy.


Running
-------
The AIDR Persister has a RESTFul API, that means all the operations have their corresponding REST services. For more details check the API package inside the project source code.
