To start/stop/re-start AIDR and other system administration tasks, see [System administrator manual](https://github.com/qcri-social/AIDR/wiki/System-administrator-manual)

# 0. Requirements

Before installing AIDR, you must have the following:

* Apache Maven installed -- http://maven.apache.org/
* Redis server running -- http://redis.io/
* Glassfish server running -- https://glassfish.java.net/
* MySQL server running -- https://dev.mysql.com/downloads/mysql/
* Pybossa server running -- http://docs.pybossa.com/en/latest/index.html

We have tested AIDR on Ubuntu 12, using Glassfish v4.0 and MySQL v14.14.

# 1. Building and deploying (general)

AIDR is a set of maven-based projects. Inside each project, a POM file specifies all the dependencies that are required for the building process. To build, go to the root directory of the project where pom.xml is located and run `mvn install`

This process initiates downloading process of required packages/libraries from the maven central repository, and may take a few minutes. A successful build will create a target folder on the project root directory and a couple of other files too.

If the maven process creates a .war file, that file must be deployed to Glassfish. Go to the admin page of your Glassfish server, usually it can be accessed through http://hostname:4848/. Login and go to the applications tab, choose the _deploy_ option and upload the .war file.

# 2. Collector (aidr-collector)

* Build using maven following the instructions above; this should generate a file `aidr-collector-X.war`
* In `utils/Config.java`, appropriately set the configuration parameters. Note that the FETCHER_REST_URI and the PERSISTER_REST_URI should match the actual URI's used. 
* Appropriately set the `fetchMainUrl` in the `system.properties` file under `aidr-manager`. 
* Deploy `aidr-collector-X.war` to Glassfish following the instructions above.

## Running

The AIDR Collector has a RESTFul API, that means all the operations have their corresponding REST services. For more details regarding the API, please refer to [API page](https://github.com/qcri-social/AIDR/wiki/API-documentation). The output of the aidr-collector pubished to Redis through channels. Every collection starts its dedicated sub-channel under the aidr-collector channel, which is `FetcherChannel`.

# 3. Persister (aidr-persister)

* Build using maven following the instructions above; this should generate a file `aidr-persister-X.war`
* Modify the `utils/Config.java` file appropriately. Ensure that you have read-write permissions for the DEFAULT_PERSISTER_FILE_PATH and SCD1_URL. 
* Appropriately set the `persisterMainUrl` in the `system.properties` file under `aidr-manager`.
* Deploy `aidr-persister-X.war` to Glassfish using the instructions above.
* If you are using Apache web server, edit the appropriate file in `/etc/apache2/sites-available` directory as follows: set `AllowOverride All` under the appropriate `<Directory>` section. Restart Apache web service. 
* Create a file `.htaccess` in the `.../aidr/data/persister` directory with the following two lines:

             Options -Indexes
             AddType application/octet-stream .zip .json .csv

# 4. Manager (aidr-manager)

Prior to start the project building process make sure you have completed the following steps (a-d):

(a) Install the MySql database server on your machine and create a database schema, name it for example "aidr_fetch_manager". 

Useful links for setting up MySql with Glassfish: [Using Connector/J with Glassfish](http://dev.mysql.com/doc/connector-j/en/connector-j-usagenotes-glassfish-config.html) and [How to setup a JDBC connection in Glassfish](http://computingat40s.wordpress.com/how-to-setup-a-jdbc-connection-in-glassfish/) 

The schema name can also be changed that requires to change application configurations which specify schema details as described in the next 2 steps.

(b) In case of first time deployment, make sure you add the following line in the `spring-servlet.xml` file located under `<project-root>/src/main/webapp/WEB-INF/`:

`<prop key="hibernate.hbm2ddl.auto">create</prop>`

**NOTE**:- **If the application has previously been deployed and you have the MySql schema with tables in place then just remove the above line. The above line is used to create MySQL schema during the first deployment**. After the first deployment, you can instead use: 

`<prop key="hibernate.hbm2ddl.auto">update</prop>`


(c) In the same file `spring-servlet.xml` the database credentials can be specified/changed according to your installation.

(d) Apply the following changes to "system.properties" file located under `<app-root>/src/main/resources/`

* twitter.consumerKey=<put here your Twitter's application consumer key>
* twitter.consumerSecret=<put here your Twitter's application consumer key secret>
* twitter.callBackURL=<here goes the URL where the application is accessible>. e.g., http://localhost:8080/AIDRFetchManager   
* application.secureUrl=<here goes the URL where the application is accessible>. e.g., http://localhost:8080/AIDRFetchManager
* fetchMainUrl=Put here aidr-collector webresources path (e.g., http://localhost:8084/aidr-collector/webresources)
* taggerMainUrl=Put here aidr-tagger-api webresources path (e.g., http://localhost:8084/aidr-tagger-api/rest
* persisterMainUrl=Put here aidr-persister webresources path (e.g., http://localhost:8084/aidr-persister/webresources)
* crowdsourcingAPIMainUrl=Put here aidr-trainer-api webresources path (e.g., http://localhost:8084/aidr-trainer-api/rest)
* outputAPIMainUrl=Put here aidr-output webresources path (e.g., http://localhost:8084/aidr-output/rest)

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
* Appropriately set the `taggerMainUrl` in the `system.properties` file under `aidr-manager`. 
* Copy the `aidr-task-manager.jar` file generated as per the instructions in `aidr-task-manager` to the WEB-INF/lib folder.
* Include the `aidr-task-manager.jar` file in the build path.
* Build using maven following the instructions above; this should generate a file `aidr-tagger-api-X.war`
* Deploy `aidr-tagger-api-X.war` to Glassfish using the instructions above.

# 7. Output (aidr-output)

* Set the Redis host and port number appropriately in the `resources/config.properties` file. 
* Build using maven following the instructions above; this should generate a file `aidr-output-X.war`
* Appropriately set the `outputAPIMainUrl` in the `system.properties` file under `aidr-manager`.
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
    * name: client name
    * hostURL: Pybossa api host url
    * hostAPIKey: Pybossa account api key
    * description: client description
    * queueSize: task pending size
    * aidrHostURL : train API rest service url
    * defaultTaskRunsPerTask : the numbers of user vote for a task
* Appropriately set the `crowdsourcingAPIMainUrl` in the `system.properties` file under `aidr-manager`.     
* Build using maven following the instructions above; this should generate a file `aidr-trainer-pybossa-X.war`
* Deploy `aidr-trainer-pybossa-X.war` to Glassfish using the instructions above. 


# Task Manager

The `aidr-task-manager` module is meant to provide a unified view of the `aidr_predict` database tables that are related to 'aidr tasks' - namely, `document`, `task_assignment`, `document_nominal_labels` and `crisis` tables. The various modules of AIDR such as `aidr-tagger-api`, `aidr-tagger` and `aidr-trainer-api` that access these tables will use the aidr-task-manager as the single access point (in phases). To enable this, `aidr-task-manager` uses remote EJBs. The instructions for enabling access through `aidr-task-manager` are outlined below:


* Create a new JDBC resource in the server called `JNDI/aidr_task_manager` to match the entry in the `persistence.xml` file with `connection pool` set to that of the `aidr-predict database`.
* Build using maven the `aidr-task-manager.war` file and deploy to glassfish in the usual way.
* Next build `aidr-task-manager.jar` file excluding the `ejb/bean` directory from the jar. 
* Create a new directory `WEB-INF/lib` in the module that will use aidr-task-manager.
* Copy the `aidr-task-manager.jar` file to the `WEB-INF/lib` directory. 



# Known Issues

* aidr-manager application, when deployed on Glassfish 4 application server, does not load its default homepage (index.jsp page) on startup. In this case, complete path (e.g., http://abc.org/aidr/index.jsp) must be provided to launch the default homepage.

* CDI deployment failure when attempting deployment of a module. The workaround is to toggle the `scope` of the glassfish 4.0 specific dependencies in the `pom.xml` file between `provided` and `compile`. 

* Tagger-API throws remote EJB exception `java.lang.NoClassDefFoundError` (org.omg.CORBA.MARSHAL: WARNING: IOP00810010: Error from readValue on ValueHandler in CDRInputStream vmcid: OMG minor code: 10 completed) for `AIDRTaskManger` remote EJB method calls. `Solution`: downgrade to java version `1.7.0_51` or upgrade to `1.8.0_*` link: [https://java.net/jira/browse/GLASSFISH-21047](https://java.net/jira/browse/GLASSFISH-21047).