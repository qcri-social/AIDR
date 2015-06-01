To start/stop/re-start AIDR and other system administration tasks, see [System administrator manual](https://github.com/qcri-social/AIDR/wiki/System-administrator-manual)

# Requirements

Before installing AIDR, you must have the following **installed** in your system:

* Java (we have tested with v1.7) -- http://www.oracle.com/technetwork/java/javaee/downloads/
* Apache Maven -- http://maven.apache.org/

Before installing AIDR, you must have the following **installed and running** in your system:

* Redis server running -- http://redis.io/ (important configurations are listed below)
 * maxclients: once the limit is reached Redis will close all the new connections sending an error 'max number of clients reached'.
 * timeout: for very slow running collections, setting timeout=0 will prevent server to timeout.
* Glassfish server running (we have tested with v4.0) -- https://glassfish.java.net/
* MySQL server running (we have tested with v14.14) -- https://dev.mysql.com/downloads/mysql/
 * Useful links for setting up MySql with Glassfish: [Using Connector/J with Glassfish](http://dev.mysql.com/doc/connector-j/en/connector-j-usagenotes-glassfish-config.html) and [How to setup a JDBC connection in Glassfish](http://computingat40s.wordpress.com/how-to-setup-a-jdbc-connection-in-glassfish/) 
* Pybossa server running -- http://docs.pybossa.com/en/latest/index.html

AIDR developers work use an Ubuntu 12 server for developing and testing.

# 0. Building and deploying (general)

AIDR is a set of maven-based projects. Inside each project, a POM file specifies all the dependencies that are required for the building process. To build, go to the root directory of the project where pom.xml is located and run `mvn install`

This process initiates downloading process of required packages/libraries from the maven central repository, and may take a few minutes. A successful build will create a target folder on the project root directory and a couple of other files too.

If the maven process creates a .war file, that file must be deployed to Glassfish. Go to the admin page of your Glassfish server, usually it can be accessed through http://hostname:4848/. Login and go to the applications tab, choose the _deploy_ option and upload the .war file.

Compilation and deployment of the modules must follow the below-specified sequence i.e., #1 (aidr-common) should be complied/deployed first before #2 (aidr-db-manager).

# 1. Common (aidr-common)

`aidr-common` contains code that is reused across multiple modules: e.g., logging and REDIS load shedding. This module **must** be built before any other module. 

* Build using maven following the instructions above; this should generate a file `aidr-common-X.jar`.
* For most users, the maven build will also install the generated jar in maven's local repository so that
other modules can thereafter automatically find the dependency.

This module does not need a deployment in the Glassfish server.

# 2. DB-manager (aidr-db-manager)
* Create `aidr_predict` schema in the MySQL database, as shown below. This assumes database=aidr_predict, username=aidr_admin, password=aidr_admin:

        % mysql -u root -p
        Enter password: [your mysql root user password]
        mysql> CREATE DATABASE aidr_predict;
        mysql> GRANT ALL PRIVILEGES ON aidr_predict.* TO aidr_admin@localhost IDENTIFIED BY 'aidr_admin';
        mysql> GRANT TRIGGER ON aidr_predict.* TO aidr_admin@localhost IDENTIFIED BY 'aidr_admin'; 
        mysql> QUIT;

* Edit [persistence.xml](../tree/master/aidr-db-manager/src/main/resources/META-INF/persistence.xml):
  1. Set hibernate.hbm2ddl.auto property to "create". This means upon deployment aidr-predict schema will be created from scratch. If there exist already a populated schema, it will be re-written and all data will be lost. Make sure to set the value of this property to "update" for the subsequent deployments of this module to prevent schema refresh each time. 
  2. Set jdbc resource in the glassfish server for aidr_predict database (connection pool URL jdbc:mysql://localhost:3306/aidr_predict) and specify its name at `jta-data-source`
* Build using maven following the instructions above; this should generate a file `db-managerEAR-X.ear`
* Deploy the `db-managerEAR-X.ear` to Glassfish
* Populate `crisis_types` table in the database

        % mysql aidr_predict -u aidr_admin -p < populate_db_crisistype.sql

* Populate `nominal_attributes` and `nominal_label` tables in the database

        % mysql aidr_predict -u aidr_admin -p < populate_db_attributes.sql

# 3. Task Manager (aidr-task-manager)

**Modules dependent**: `aidr-tagger`, `aidr-tagger-api`

The `aidr-task-manager` module is meant to provide a unified view of the `aidr_predict` database tables that are related to 'aidr tasks' - namely, `document`, `task_assignment`, `document_nominal_labels` and `crisis` tables. The various modules of AIDR such as `aidr-tagger-api`, `aidr-tagger` and `aidr-trainer-api` that access these tables will use the aidr-task-manager as the single access point (in phases). To enable this, `aidr-task-manager` uses remote EJBs. The instructions for enabling access through `aidr-task-manager` are outlined below:
* Build using maven following the instructions above; this should generate a file `aidr-task-managerEAR.ear` file.
* Deploy the `aidr-task-managerEAR.ear` file to Glassfish.

# 4. Persister (aidr-persister)

* Edit [config.properties](../tree/master/aidr-persister/src/main/resources/config.properties):
  1. DEFAULT_PERSISTER_FILE_PATH : This is where the persister will store all tweets on the file system. This path should be accessible from the server, so a link to the location must be created on the web server.

* Build using maven following the instructions above; this should generate a file `aidr-persister-X.war`
* Deploy `aidr-persister-X.war` to Glassfish using the instructions above.

* If you are using Apache web server, edit the appropriate file in `/etc/apache2/sites-available` directory as follows: set `AllowOverride All` under the appropriate `<Directory>` section. Restart Apache web service. 
* Create a file `.htaccess` in the `.../aidr/data/persister` directory with the following two lines:

        Options -Indexes 
        AddType application/octet-stream .zip .json .csv

# 5. Collector (aidr-collector)

* In [config.properties](../tree/master/aidr-collector/src/main/resources/config.properties), appropriately set the configuration parameters:
  1. FETCHER_REST_URI: Rest URI of the collector. For example, if the collector is deployed as AIDRCollector on the server, then the REST URI would be: http://localhost:8080/AIDRCollector/webresources/
  2. PERSISTER_REST_URI: Rest URI of the persiser module, e.g. http://localhost:8080/AIDRPersister/webresources/

* Build using maven following the instructions above; this should generate a file `aidr-collector-X.war`
* Deploy `aidr-collector-X.war` to Glassfish following the instructions above.
* Test the deployment (optional). You can ping the collector service using the following command:
```
$ curl http://localhost:8080/aidr-collector/webresources/manage/ping

Response: 
{"startDate":"2014/12/14 16:22:12","currentStatus":"RUNNING"}
```
# 6. Tagger (aidr-tagger)

**NOTE**: A re-deployment of the `aidr-task-manager` module may require a re-compilation and restart of the `aidr-tagger` module.

* Edit [config.properties](../tree/master/aidr-tagger/src/main/resources/config.properties), to match the database login info.

* Compile the application to a jar file.

 `% mvn -Pdev install` (or right-click on `pom.xml`, Run as ... Maven build ... 'install')
     
* Start/run the application

  1. Run the following command: java -Xmx4048m -cp  $GLASSFISH_HOME/glassfish/lib/gf-client.jar:aidr-tagger-1.0-jar-with-dependencies.jar:libs/* qa.qcri.aidr.predict.Controller
  2. Make sure you are in the root directory (otherwise trained models will not be saved in the right part)
  3. The main method in the jar is in qa.qcri.aidr.predict.Controller
  4. You will see some incomprehensible debug output. If the numbers are not 0, input data is being processed.

This module does not need a deployment in the Glassfish server. Also, create the model folder in target (with appropriate permissions).
      
# 7. Tagger-API (aidr-tagger-api)

**NOTE**: A re-deployment of the `aidr-task-manager` module may require a re-deployment of the `aidr-tagger-api` module.

* Create JDBC resources in server (e.g., Glassfish) to match the JNDI names (JNDI/aidr_predict and JNDI/aidr_fetch_manager) used in [persistence.xml](../tree/master/aidr-tagger-api/src/main/resources/META-INF/persistence.xml)
* Build using maven following the instructions above; this should generate a file `aidr-tagger-api-X.war`
* Deploy `aidr-tagger-api-X.war` to Glassfish using the instructions above.
* Test the deployment (optional). You can check if `aidr-tagger-api` was installed correctly:
```
$ curl http://localhost:8080/AIDRTaggerAPI/rest/misc/ping

Response:
{"application":"AIDRTaggerAPI", "status":"RUNNING"}
```

# 8. Output (aidr-output)

**Modules dependent**: `aidr-analysis`

* Set the Redis host and port number appropriately in [config.properties](../tree/master/aidr-output/src/main/resources/config.properties) file. 
* Build using maven following the instructions above; this should generate a file `aidr-output-X.war`.
* Deploy `aidr-output-X.war` to Glassfish using the instructions above.


# 9. AIDR Analytics (aidr-analytics)

The `aidr-analytics` module is meant to provide data for various analytics and visualization of categorized tweet data. 

* Create a new database called `aidr_analysis`. Grant appropriate table and trigger permissions (similar to the instructions for setting up the `aidr_predict` database). 
* For first time build, set the property `hibernate.hbm2ddl.auto` to `create` in [persistence.xml](../tree/master/aidr-analytics/src/main/resources/META-INF/persistence.xml). For subsequent deployments, change the value to `update`.

**WARNING**: Setting "hibernate.hbm2ddl.auto" to `create` drops and creates the aidr_analysis database!

* Create a new JDBC resource in server (e.g., Glassfish) 'aidr_analysis` database. Attach it with connection pool set to that of the `aidr_analysis` database.
* Specify the JDBC resource name at `jta-data-source` in [persistence.xml](../tree/master/aidr-analytics/src/main/resources/META-INF/persistence.xml)

* Appropriately set the parameters in [granularity.properties](../tree/master/aidr-analytics/src/main/resources/granularity.properties) file. Use the suffix `s`, `m`, `h` and `d` to indicate seconds, minutes, hours and days respectively. 
* Build using maven and deploy the WAR file. 

# 10. Trainer API (aidr-trainer-api)

* Pre-requisite: `aidr_predict` database and `aidr_scheduler` database should be created. `aidr_predict' database is created by aidr-db-manager module. 
* Create a new database called `aidr_scheduler`. Grant appropriate table and trigger permissions (similar to the instructions for setting up the `aidr_predict` database) 
* Run the script (aidr_scheduler.sql)[] to create the tables in the database:

        % mysql aidr_scheduler -u aidr_admin -p < aidr_scheduler.sql

* Appropriately set the properties in the [database.properties](../tree/master/aidr-trainer-api/src/main/resources/database.properties) and [databaseTemp.properties](../tree/master/aidr-trainer-api/src/main/resources/databaseTemp.properties) files under src/main/resources.
* Build using maven following the instructions above; this should generate a file `aidr-trainer-api-X.war`
* Deploy `aidr-trainer-api-X.war` to Glassfish using the instructions above. 

# 11. Trainer Pybossa (aidr-trainer-pybossa)

* Pre-requisite: `aidr_predict` database and `aidr_scheduler` database
* Pybossa Server or user should have pybossa account(s) with clickers.micromappers.org 
* Appropriately set the properties in [database.properties](../tree/master/aidr-trainer-pybossa/src/main/resources/database.properties) under src/main/resources
* User should configure client table.
    * name: client name
    * hostURL: Pybossa api host url
    * hostAPIKey: Pybossa account api key
    * description: client description
    * queueSize: task pending size
    * aidrHostURL : train API rest service url
    * defaultTaskRunsPerTask : the numbers of user vote for a task
* Build using maven following the instructions above; this should generate a file `aidr-trainer-pybossa-X.war`
* Deploy `aidr-trainer-pybossa-X.war` to Glassfish using the instructions above. 


# 12. Manager (aidr-manager)

Prior to start the project building process make sure you have completed the following steps (a-d):

(a) Create a database schema `aidr_fetch_manager`.
(b) In case of first time deployment, make sure you add the following line in [spring-servlet.xml](../tree/master/aidr-manager/src/main/webapp/WEB-INF/spring-servlet-xml)

`<prop key="hibernate.hbm2ddl.auto">create</prop>`

**NOTE**:- If the application has previously been deployed and you have the MySql schema with tables in place then just remove the above line. The above line is used to create MySQL schema during the first deployment. After the first deployment, you can instead use: 

`<prop key="hibernate.hbm2ddl.auto">update</prop>`

(c) In the same file `spring-servlet.xml` the database credentials can be specified/changed according to your installation.

(d) Apply the following changes to [system.properties](../tree/master/aidr-manager/src/main/resources/system.properties)
   * twitter.consumerKey=<put here your Twitter's application consumer key>
   * twitter.consumerSecret=<put here your Twitter's application consumer key secret>
   * twitter.callBackURL=<here goes the URL where the aidr-manager application is accessible>. e.g., http://localhost:8080/AIDRFetchManager
   * application.secureUrl=<here goes the URL where the application is accessible>. e.g., http://localhost:8080/AIDRFetchManager
   * fetchMainUrl=Put here aidr-collector webresources path (e.g., http://localhost:8084/aidr-collector/webresources)
   * taggerMainUrl=Put here aidr-tagger-api webresources path (e.g., http://localhost:8084/aidr-tagger-api/rest
   * persisterMainUrl=Put here aidr-persister webresources path (e.g., http://localhost:8084/aidr-persister/webresources)
   * crowdsourcingAPIMainUrl=Put here aidr-trainer-api webresources path (e.g., http://localhost:8084/aidr-trainer-api/rest)
   * outputAPIMainUrl=Put here aidr-output webresources path (e.g., http://localhost:8084/aidr-output/rest)
   * serverUrl=Put here the server name on which AIDR is hosted

After the above steps have been executed, you can build the project:

* Build using maven following the instructions above; this should generate a file `aidr-manager-X.war`
* Deploy `aidr-manager-X.war` to Glassfish using the instructions above.

# Building the complete AIDR suite

If you want to build all the modules which are a part of the AIDR application in one go, you can do so using the instructions provided below:

* Go to AIDR's root directory:

    `cd $AIDR_HOME`

* To build AIDR maven requires you to provide a profile explicitly through the -P option. You cannot build and install without specifying this option (we don't use a default profile.). Currently there are two kind of profile options available 'dev' and 'prod'. To install using the 'dev' profile configurations use the following:

    `mvn -Pdev install`

This will build all the modules keeping the dependency order intact. 

**NOTE:** the directory $AIDR_HOME/profiles/<selected-profile>/configuraiton.properties contains the minimum required configurations for AIDR. Use default values wherever you can, and be careful while changing these to ensure that there is no mismatch between the remote JNDI resources. 

# Deploying the complete AIDR suite

If you want to deploy (or undeploy) all the modules which are a part of the AIDR application in one go, you can do so using the following instructions:

* Go to AIDR's root directory:

    `cd $AIDR_HOME`

* Review the `deploy.sh` script:
 * Set the environment variables correctly to match your installations.
 * Set the correct application names and JDBC resource names.

* Run the `deploy.sh` script:

    `sh deploy.sh deploy`

**NOTE 1:** The argument to the `deploy.sh` script can be `deploy` (starts a glassfish domain, created JDBC resources and deploys the various modules on glassfish), `undeploy` (undeploys all the modules from the glassfish server, removes the JDBC resources and shuts down the glassfish domain), or `undeploy-deploy` (undeploys the app first then deploys it).

**NOTE 2:** The `deploy.sh` command also starts a java process for the Tagger module. However, undeploy does not kill this specific process. If you want you can use `jps` to locate the tagger process and kill it using `kill -9`.

Also, if you are using a glassfish user with an enabled password. Please use the following command in the `$GLASSFISH_HOME` directory before running the deployment script:

    bin/asadmin login

This will help you login once and would execute all the commands in the script in a non-obtrusive manner. If you don't do this you will be asked to enter the asadmin username and password multiple times during the deployment process.

# Miscellaneous

**Please execute the following in MySQL, once the databases are created**

First, check the character set currently being used in MySQL:

1. SHOW VARIABLES WHERE Variable_name LIKE 'character\_set\_%' OR Variable_name LIKE 'collation%';

2. ALTER DATABASE aidr_fetch_manager CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

3. ALTER TABLE aidr_fetch_manager.AIDR_COLLECTION CHANGE last_document last_document LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

4. ALTER DATABASE aidr_predict CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

5. ALTER TABLE aidr_predict.document CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

6. ALTER TABLE aidr_predict.document CHANGE data data TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

Next, modify `/etc/mysql/my.cnf` file with the following:

Under `[client]` section add:
default-character-set = utf8mb4

Under `[mysqld]` section add:
character-set-client-handshake = FALSE
character-set-server = utf8mb4
collation-server = utf8mb4_unicode_ci

Under `[mysql]` section add:
default-character-set = utf8mb4

Finally, do a sanity check: 

* SHOW VARIABLES WHERE Variable_name LIKE 'character\_set\_%' OR Variable_name LIKE 'collation%';



# Known Issues

* aidr-manager application, when deployed on Glassfish 4 application server, does not load its default homepage (index.jsp page) on startup. In this case, complete path (e.g., http://abc.org/aidr/index.jsp) must be provided to launch the default homepage.

* CDI deployment failure when attempting deployment of a module. The workaround is to toggle the `scope` of the glassfish 4.0 specific dependencies in the `pom.xml` file between `provided` and `compile`. 

* Tagger-API throws remote EJB exception `java.lang.NoClassDefFoundError` (org.omg.CORBA.MARSHAL: WARNING: IOP00810010: Error from readValue on ValueHandler in CDRInputStream vmcid: OMG minor code: 10 completed) for `AIDRTaskManger` remote EJB method calls. `Solution`: downgrade to java version `1.7.0_51` or upgrade to `1.8.0_*` link: [https://java.net/jira/browse/GLASSFISH-21047](https://java.net/jira/browse/GLASSFISH-21047).