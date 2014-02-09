To start/stop/re-start AIDR and other system administration tasks, see [System administrator manual](https://github.com/qcri-social/AIDR/wiki/System-administrator-manual)

# 0. Requirements

Before installing AIDR, you must have the following:

* Apache Maven installed -- http://maven.apache.org/
* Redis server running -- http://redis.io/
* Glassfish server running -- https://glassfish.java.net/
* MySQL server running -- https://dev.mysql.com/downloads/mysql/
* Pybossa server running -- http://docs.pybossa.com/en/latest/index.html

We have tested AIDR on Ubuntu 12, using Glassfish v3.2 and MySQL v14.14.

# 1. Building and deploying (general)

AIDR is a set of maven-based projects. Inside each project, a POM file specifies all the dependencies that are required for the building process. To build, go to the root directory of the project where pom.xml is located and run `mvn install`

This process initiates downloading process of required packages/libraries from the maven central repository, and may take a few minutes. A successful build will create a target folder on the project root directory and a couple of other files too.

If the maven process creates a .war file, that file must be deployed to Glassfish. Go to the admin page of your Glassfish server, usually it can be accessed through http://hostname:4848/. Login and go to the applications tab, choose the _deploy_ option and upload the .war file.

# 2. Collector (aidr-collector)

* Build using maven following the instructions above; this should generate a file `aidr-collector-X.war`
* Deploy `aidr-collector-X.war` to Glassfish following the instructions above.

## Running

The AIDR Collector has a RESTFul API, that means all the operations have their corresponding REST services. For more details regarding the API, please refer to [API page](https://github.com/qcri-social/AIDR/wiki/API-documentation). The output of the aidr-collector pubished to Redis through channels. Every collection starts its dedicated sub-channel under the aidr-collector channel, which is `FetcherChannel`.

# 3. Persister (aidr-persister)

* Build using maven following the instructions above; this should generate a file `aidr-persister-X.war`
* Deploy `aidr-persister-X.war` to Glassfish using the instructions above.

# 4. Manager (aidr-manager)

Prior to start the project building process make sure you have completed the following steps (a-d):

(a) Install the MySql database server on your machine and create a database schema, name it for example "aidr_fetch_manager". 

Useful links for setting up MySql with Glassfish: [Using Connector/J with Glassfish](http://dev.mysql.com/doc/connector-j/en/connector-j-usagenotes-glassfish-config.html) and [How to setup a JDBC connection in Glassfish](http://computingat40s.wordpress.com/how-to-setup-a-jdbc-connection-in-glassfish/) 

The schema name can also be changed that requires to change application configurations which specify schema details as described in the next 2 steps.

(b) In case of first time deployment, make sure you add the following line in the `spring-servlet.xml` file located under `<project-root>/src/main/webapp/WEB-INF/`:

`<prop key="hibernate.hbm2ddl.auto">create</prop>`

NOTE:- If the application has previously been deployed and you have MySql schema with tables in place then just remove the above line. The above line is used to create MySQL schema during the first deployment.

(c) In the same file `spring-servlet.xml` the database credentials can be specified/changed according to your installation.

(d) Apply the following changes to "system.properties" file located under `<app-root>/src/main/resources/`

* twitter.consumerKey=<put here your Twitter's application consumer key>
* twitter.consumerSecret=<put here your Twitter's application consumer key secret>
* twitter.callBackURL=<here goes the URL where the application is accessible>. e.g., http://localhost:8080/AIDRFetchManager   
* application.secureUrl=<here goes the URL where the application is accessible>. e.g., http://localhost:8080/AIDRFetchManager
* fetchMainUrl=<here specify the URL of the Collector/Fetcher application>. e.g., http://localhost:8080/AIDRFetcher/webresources/fetcher

After the above steps have been executed, you can build the project:

* Build using maven following the instructions above; this should generate a file `aidr-manager-X.war`
* Deploy `aidr-manager-X.war` to Glassfish using the instructions above.

# 5. Tagger (aidr-tagger)

1) Create a database. This assumes database=aidr_predict, username=aidr_user, password=pass123 :

   `% mysql -u root -p`
   Enter password: [your mysql root user password]
   `mysql> CREATE DATABASE aidr_predict;`
   `mysql> GRANT ALL PRIVILEGES ON aidr_predict.* TO aidr_user@localhost IDENTIFIED BY 'pass123';`
   `mysql> GRANT TRIGGER ON aidr_predict.* TO aidr_user@localhost IDENTIFIED BY 'pass123';`
   `mysql> QUIT;`

2) Create the database schema running the src/main/scripts/create_db.sql script (this will DELETE your old data, if any):

   `% mysql aidr_predict -u aidr_user -p < create_db.sql`
   Enter password: `pass123`
   
3) Populate the database:

   `% mysql aidr_predict -u aidr_user -p < populate_db_crisistype.sql`
   Enter password: `pass123`
   
   `% mysql aidr_predict -u aidr_user -p < populate_db_attributes.sql`
   Enter password: `pass123`

4) Edit `src/main/java/qa/qcri/aidr/predict/common/Config.java` to match the database login info.

5) Modify config/config.txt with appropriate MySQL settings

6) Compile the application to a jar file.

 `% mvn install` (or right-click on `pom.xml`, Run as ... Maven build ... 'install')
     
6) Start the application

   a) Make sure you are in the root directory (otherwise trained models will not be saved in the right part)
   b) The main method in the jar is in qa.qcri.aidr.predict.Controller
      You will see some incomprehensible debug output. If the numbers are not 0, input data is being processed.
      
# 6. Tagger-API (aidr-tagger-api)

* Create JDBC resources in server (e.g., Glassfish) to match the JNDI names (JNDI/aidr_predict and JNDI/aidr_fetch_manager) used in src/main/resource/META-INF/persistence.xml. 
* Build using maven following the instructions above; this should generate a file `aidr-tagger-api-X.war`
* Deploy `aidr-tagger-api-X.war` to Glassfish using the instructions above.

# 7. Output (aidr-output)

* Build using maven following the instructions above; this should generate a file `aidr-output-X.war`
* Deploy `aidr-output-X.war` to Glassfish using the instructions above.

# 8. Trainer API (aidr-trainer-api)

* Pre-requisite: aidr-tagger database and aidr-scheduler database should be created. Aidr-tagger database script is located in aidr-tagger installtion details. Aidr-trainer database script (aidr_scheduler.sql) can be found in the root of the aidr-trainer-api project.
* Appropriately set the properties in the database.properties and databaseTemp.properties files under src/main/resources.
* Build using maven following the instructions above; this should generate a file `aidr-trainer-api-X.war`
* Deploy `aidr-trainer-api-X.war` to Glassfish using the instructions above. 

# 9. Trainer Pybossa (aidr-trainer-pybossa)

* Pre-requisite: aidr-tagger database and aidr-scheduler database should be created. Aidr-tagger database script is located in aidr-tagger installtion details. Aidr-trainer database script (aidr_scheduler.sql) can be found in the root of the aidr-trainer-api project.
* Pybossa Server or user should have pybossa account(s) with clickers.micromappers.org 
* Appropriately set the properties in the database.properties under src/main/resources
* User should configure client table.
* Build using maven following the instructions above; this should generate a file `aidr-trainer-pybossa-X.war`
* Deploy `aidr-trainer-pybossa-X.war` to Glassfish using the instructions above. 

# 10. Context Root Settings

Default context roots for deployment:

1. `aidr-collector`: AIDRCollector
2. `aidr-persister`: AIDRPersister
3. `aidr-manager`: AIDRFetchManager
4. `aidr-tagger-api`: AIDRPredictUI-API
6. `aidr-trainer-api`: AIDRTrainerAPI
7. `aidr-output`: AIDROutput
7. `aidr-trainer-pybossa`: AIDRTrainerPybossa 

These values need to reflect the setting used in the file: aidr-manager/src/main/resources/system.properties