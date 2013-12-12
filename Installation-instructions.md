# Requirements

* Apache Maven -- http://maven.apache.org/
* Glassfish -- https://glassfish.java.net/
* Redis -- http://redis.io/

# Building (general)

AIDR is a set of maven-based projects. Inside each project, a POM file specifies all the dependencies that are required for the building process. To build, go to the root directory of the project where pom.xml is located and run `mvn install`

This process initiates downloading process of required packages/libraries from the maven central repository, and may take a few minutes. A successful build will create a target folder on the project root directory and a couple of other files too.

# Collector

## Building

After building you must find a file "aidr-collector-1.0.war" if the building process successfully completed.

## Deploying

Pre-requisite: a Glassfish server must be installed and running.

Go to the admin page of your Glassfish server, usually it can be accessed through http://hostname:4848/. Login and go to the applications tab, choose deploy option and browse to the "aidr-collector-1.0.war" file and click deploy.

## Running

The AIDR Collector has a RESTFul API, that means all the operations have their corresponding REST services. For more details regarding the API, please refer to this google doc (https://docs.google.com/document/d/1mOlhFDoVCAGOE7gvKQLKwH04P4_b0jyBF7orTQ7ca28/pub). AIDR Collector publishes collected tweets to Redis channels which named after collection codes, like, "FetcherChannel.Sandy2013". You can explore the code for more details. 


