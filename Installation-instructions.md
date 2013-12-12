# 0. Requirements

Before installing AIDR, you must have the following:

* Apache Maven installed -- http://maven.apache.org/
* Redis server running -- http://redis.io/
* Glassfish server running -- https://glassfish.java.net/
* MySQL server running -- https://dev.mysql.com/downloads/mysql/

We have tested AIDR on Ubuntu 12, using Glassfish v3.2 and MySQL v14.14.

# 1. Building and deploying (general)

AIDR is a set of maven-based projects. Inside each project, a POM file specifies all the dependencies that are required for the building process. To build, go to the root directory of the project where pom.xml is located and run `mvn install`

This process initiates downloading process of required packages/libraries from the maven central repository, and may take a few minutes. A successful build will create a target folder on the project root directory and a couple of other files too.

If the maven process creates a .war file, that file must be deployed to Glassfish. Go to the admin page of your Glassfish server, usually it can be accessed through http://hostname:4848/. Login and go to the applications tab, choose the _deploy_ option and upload the .war file.

# 2. Collector (aidr-collector)

## Building

Build using maven following the instruction above; this should generate a file "aidr-collector-X.war".

## Deploying

Deploy "aidr-collector-1.0.war" to Glassfish following the instructions above.

## Running

The AIDR Collector has a RESTFul API, that means all the operations have their corresponding REST services. For more details regarding the API, please refer to this google doc (https://docs.google.com/document/d/1mOlhFDoVCAGOE7gvKQLKwH04P4_b0jyBF7orTQ7ca28/pub). AIDR Collector publishes collected tweets to Redis channels which named after collection codes, like, "FetcherChannel.Sandy2013". You can explore the code for more details. 

# 3. Persister (aidr-persister)

## Building

Build using maven following the instruction above; this should generate a file "aidr-persister-X.war".

## Deploying

Deploy "aidr-persister-X.war" to Glassfish using the instructions above.


