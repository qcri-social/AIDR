To start/stop/re-start AIDR and other system administration tasks, see [System administrator manual](https://github.com/Qatar-Computing-Research-Institute/CrisisComputing/wiki/System-administrator-manual)

# 1. Requirements

AIDR developers use an Ubuntu 14 server for developing and testing.

## 1.1. Required software

Before installing AIDR, you must have the following **installed** in your system:

1. **Java** (we have tested with Java 7 (JDK 1.7.0_80), and we know **AIDR does NOT work with Java 8**) -- http://www.oracle.com/technetwork/java/javaee/downloads/
1. **Maven** -- http://maven.apache.org/

## 1.2. Required services

Before installing AIDR, you must have the following **installed and running** in your system:

1. **Glassfish** server (we have tested with v4.1) -- https://glassfish.java.net/
1. **MySQL** server (we have tested with v5.5) -- https://dev.mysql.com/downloads/mysql/
1. **Redis** server -- http://redis.io/
1. **Postgres** server (we have tested with 9.4)-- https://www.postgresql.org/download/
1. **Pybossa** server (Not for AIDR) -- http://docs.pybossa.com/en/latest/index.html

To set up MySql with Glassfish, check these useful links: [Using Connector/J with Glassfish](http://dev.mysql.com/doc/connector-j/en/connector-j-usagenotes-glassfish-config.html) and [How to setup a JDBC connection in Glassfish](http://computingat40s.wordpress.com/how-to-setup-a-jdbc-connection-in-glassfish/).

## 1.3. Required configuration of services

Before installing AIDR, **configure these services** as follows:

1. **MySQL configuration**: modify `/etc/mysql/my.cnf`:
 * In the `[client]` section, add
    * `default-character-set = utf8mb4`
 * In the `[mysqld]` section, add:
    * `character-set-client-handshake = FALSE`
    * `character-set-server = utf8mb4`
    * `collation-server = utf8mb4_unicode_ci`
	* `sql-mode = ""` (Only if using v5.6 and above)
	
 * In the [mysql] section, add:
    * `default-character-set = utf8mb4`
  * For performance optimization:
    (Reference: https://www.percona.com/blog/2014/01/28/10-mysql-settings-to-tune-after-installation/)
    * max_connections = 250
    * Set buffer pool size to 50-80% of computer memory
        * innodb_buffer_pool_size=4G
        * innodb_buffer_pool_instances=4
        * innodb_file_per_table =1
    * Set the log file size to about 25% of the buffer pool size
        * innodb_log_file_size=500M
        * innodb_log_buffer_size=8M

1. **Redis configuration**: modify `redis.conf`:
   * Increase the maximum number of clients to avoid having an error "max number of clients reached":
     * `maxclients 10000`
   * Set the timeout to zero to allow very slow running collections:
     * `timeout 0`
   * For performance optimization(recommended):
     1. Cap the maxmemory limit to around 2gb or more. By default it is infinite.
       * `maxmemory 2gb`
     1. Set the eviction policy to allkeys-lru. By default it is noeviction.
       * `maxmemory-policy allkeys-lru`

Remember to re-start these services to make sure the configuration options have been applied.

# 2. Building the complete AIDR suite

If you want to build all the modules which are a part of the AIDR application in one go, you can do so using the instructions provided below:

**PreRequisite:**

* You have to get your **Twitter** Consumer Key and Consumer Key Secret. You can get them by creating a sample twitter app (Read permission recommended). link: https://apps.twitter.com

* You have to get your **Facebook** Application Id and App secret. You can get them by creating a sample facebook app (Read permission recommended). link: https://developers.facebook.com/

	In Facebook App
	
	1.	Go to Settings-Basic
	
		Add new Platform
		
			Website-URL: http://localhost:8080/AIDRFetchManager/index.jsp
			App-Domains: http://localhost:8080/
	1.	Go to App Review
	
		Make {app-name} public --> YES
		
* The directory $AIDR_HOME/profiles/{selected-profile}/config.properties contains the minimum required configurations for AIDR. Go through every configuration and change as per your system config. Use default values wherever you can, and be careful while changing these to ensure that there is no mismatch between the remote JNDI resources. Create the directories and give appropriate permissions for 'Default Persister File Path' , 'Model', 'Log Location'.

* Go to AIDR's root directory:

    `cd $AIDR_HOME`

* To build AIDR maven requires you to provide a profile explicitly through the `-P` option. You cannot build and install without specifying this option (we don't use a default profile.). Currently there are two kind of profile options available `dev` and `prod`. To install using the `dev` profile configurations, use the following:

    `mvn -Pdev install`

This will build all the modules keeping the dependency order intact. 

# 3. Pre-deployment MySQL & Postgres commands (mandatory)
**MySql**

* Create `aidr_predict` schema if not present already in the MySQL database, as shown below. This assumes database=`aidr_predict`, username=`aidr_admin`, password=`aidr_admin`, which has also been taken as the default setting in the central config.properties file:

        % mysql -u root -p
        Enter password: [your mysql root user password]
        mysql> CREATE DATABASE aidr_predict;
        mysql> GRANT ALL PRIVILEGES ON aidr_predict.* TO aidr_admin@localhost IDENTIFIED BY 'aidr_admin';
        mysql> GRANT TRIGGER ON aidr_predict.* TO aidr_admin@localhost IDENTIFIED BY 'aidr_admin'; 
        mysql> QUIT;
	* Populate table in mysql database

			% mysql aidr_predict -u aidr_admin -p < aidr_predict_schema.sql

	* Populate seed data for tables in mysql database

			% mysql aidr_predict -u aidr_admin -p < aidr_predict_seed_data.sql
	
**Postgres**

* Create `aidr` database if not present already in the Postgres, as shown below. This assumes database=`aidr`, username=`aidr_admin`, password=`aidr_admin`, which has also been taken as the default setting in the central config.properties file:

		%sudo su - postgres
		%psql
		psql> CREATE DATABASE aidr;
		psql> CREATE USER aidr_admin WITH password 'aidr_admin';
		psql> GRANT ALL privileges on DATABASE aidr to aidr_admin;

# 4. Deploying the complete AIDR suite

If you want to deploy (or undeploy) all the modules which are a part of the AIDR application in one go, you can do so using the following instructions:

* Go to AIDR's root directory:

    `cd $AIDR_HOME`

* Review the `deploy.sh` script:
 * Set the environment variables correctly to match your installation.
 * Set the correct application names and JDBC resource names.
 * Set the db username and password wherever necessary. Default username: aidr_admin and password: aidr_admin
* Run the `deploy.sh` script:

    `sh deploy.sh deploy`

**NOTE 1:** The argument to the `deploy.sh` script can be `deploy` (starts a glassfish domain, created JDBC resources and deploys the various modules on glassfish), `undeploy` (undeploys all the modules from the glassfish server, removes the JDBC resources and shuts down the glassfish domain), or `undeploy-deploy` (undeploys the app first then deploys it).

**NOTE 2:** The `deploy.sh` command also starts a java process for the Tagger module.

Also, if you are using a glassfish user with an enabled password. Please use the following command in the `$GLASSFISH_HOME` directory before running the deployment script:

    bin/asadmin login

This will help you login once and would execute all the commands in the script in a non-obtrusive manner. If you don't do this you will be asked to enter the asadmin username and password multiple times during the deployment process.

**NOTE 3:** The default minimum http-thread-pool size for Glassfish is 5, and this may not be sufficient for the application. This value can be increased via the admin console: Configurations -> server-config -> Thread Pools


# Known Issues / Troubleshooting

* aidr-manager application, when deployed on Glassfish 4 application server, does not load its default homepage (index.jsp page) on startup. In this case, complete path (e.g., http://localhost:8080/AIDRFetchManager/index.jsp) must be provided to launch the default homepage.

* CDI deployment failure when attempting deployment of a module. The workaround is to toggle the `scope` of the glassfish 4.0 specific dependencies in the `pom.xml` file between `provided` and `compile`. 

* Tagger-API throws remote EJB exception `java.lang.NoClassDefFoundError` (org.omg.CORBA.MARSHAL: WARNING: IOP00810010: Error from readValue on ValueHandler in CDRInputStream vmcid: OMG minor code: 10 completed) for `AIDRTaskManger` remote EJB method calls. `Solution`: upgrade to java version `1.7.0_80` link: [https://java.net/jira/browse/GLASSFISH-21047](https://java.net/jira/browse/GLASSFISH-21047).

* aidr-persister throws java.lang.NoClassDefFoundError:   com/fasterxml/jackson/module/jaxb/JaxbAnnotationIntrospector
at com.fasterxml.jackson.jaxrs.json.JsonMapperConfigurator._resolveIntrospector(JsonMapperConfigurator.java:109) 
at org.glassfish.jersey.message.internal.WriterInterceptorExecutor$TerminalWriterInterceptor.invokeWriteTo(WriterInterceptorExecutor.java:263)
`Solution` by the hack given on link: https://java.net/jira/browse/GLASSFISH-21141?focusedCommentId=380449&page=com.atlassian.jira.plugin.system.issuetabpanels:comment-tabpanel#comment-380449

***

# Per-module deployment

The preferred method for deployment is the one presented above, deploying all the modules. The instructions below explain how to deploy specific modules one by one, but it is not recommended unless you are already familiar with AIDR.

# 0. Building and deploying (general)

AIDR is a set of maven-based projects. Inside each project, a POM file specifies all the dependencies that are required for the building process. To build, go to the root directory of the project where pom.xml is located and run `mvn install`

This process initiates downloading process of required packages/libraries from the maven central repository, and may take a few minutes. A successful build will create a target folder on the project root directory and a couple of other files too.

If the maven process creates a .war file, that file must be deployed to Glassfish. Go to the admin page of your Glassfish server, usually it can be accessed through http://hostname:4848/. Login and go to the applications tab, choose the _deploy_ option and upload the .war file.

Compilation and deployment of the modules must follow the below-specified sequence i.e., #1 (aidr-common) should be complied/deployed first before #2 (aidr-db-manager).

**NOTE:** The configuration property files of specific modules may refer to the central configuration file present in $AIDR_HOME/profiles/${PROFILE}/config.properties. You might have to edit some properties there.

# 1. Common (aidr-common)

`aidr-common` contains code that is reused across multiple modules: e.g., logging and REDIS load shedding. This module **must** be built before any other module. 

* Build using maven following the instructions above; this should generate a file `aidr-common-X.jar`.
* For most users, the maven build will also install the generated jar in maven's local repository so that
other modules can thereafter automatically find the dependency.

This module does not need a deployment in the Glassfish server.

# 2. DB-manager (aidr-db-manager)
* Create `aidr_predict` schema if not present already in the MySQL database, as shown below. This assumes database=aidr_predict, username=aidr_admin, password=aidr_admin, which has also been taken as the default setting in the central config.properties file:

        % mysql -u root -p
        Enter password: [your mysql root user password]
        mysql> CREATE DATABASE aidr_predict;
        mysql> GRANT ALL PRIVILEGES ON aidr_predict.* TO aidr_admin@localhost IDENTIFIED BY 'aidr_admin';
        mysql> GRANT TRIGGER ON aidr_predict.* TO aidr_admin@localhost IDENTIFIED BY 'aidr_admin'; 
        mysql> QUIT;

* Edit $AIDR_HOME/profiles/${PROFILE}/config.properties:
  1. Set the TAGGER_DB_HBM2DDL property to "create". This means upon deployment aidr-predict schema will be created from scratch. If there already exist a populated schema, it will be re-written and all data will be lost. Make sure to set the value of this property to "update" for the subsequent deployments of this module to prevent schema refresh each time. 
  2. Set jdbc resource in the glassfish server for aidr_predict database (connection pool URL jdbc:mysql://localhost:3306/aidr_predict) and specify its name at `jta-data-source`
* Build using maven following the instructions above; this should generate a file `db-manager-ear-X.ear`
* Deploy the `db-manager-ear-X.ear` to Glassfish
* Populate table in mysql database

        % mysql aidr_predict -u aidr_admin -p < aidr_predict_schema.sql

* Populate seed data for tables in mysql database

        % mysql aidr_predict -u aidr_admin -p < aidr_predict_seed_data.sql

# 3. Persister (aidr-persister)
* Create `aidr` schema if not present already in the Postgres database

* Edit  $AIDR_HOME/profiles/${PROFILE}/config.properties:
  1. DEFAULT_PERSISTER_FILE_PATH : This is where the persister will store all tweets on the file system. This path should be accessible from the server, so a link to the location must be created on the web server.
  2. Also, ensure that the PERSISTER_HOST, PERSISTER_PORT and PERSISTER_WEB_APP_CONTEXT properties are correctly set.

* Build using maven following the instructions above; this should generate a file `aidr-persister-X.war`
* Deploy `aidr-persister-X.war` to Glassfish using the instructions above.

* If you are using Apache web server, edit the appropriate file in `/etc/apache2/sites-available` directory as follows: set `AllowOverride All` under the appropriate `<Directory>` section. Restart Apache web service. 
* Create a file `.htaccess` in the `.../aidr/data/persister` directory with the following two lines:

        Options -Indexes 
        AddType application/octet-stream .zip .json .csv

# 4. Collector (aidr-collector)

* In $AIDR_HOME/profiles/${PROFILE}/config.properties, appropriately set the configuration parameters. So that collector's property file has the following set appropriately:
  1. FETCHER_REST_URI: Rest URI of the collector. For example, if the collector is deployed as AIDRCollector on the server, then the REST URI would be: 'http://localhost:8080/AIDRCollector/webresources/'. This is made out of COLLECTOR_HOST, COLLECTOR_PORT and COLLECTOR_WEB_APP_CONTEXT in the central configuration property file.
  2. PERSISTER_REST_URI: Rest URI of the persiser module, e.g. 'http://localhost:8080/AIDRPersister/webresources/'.This is made out of PERSISTER_HOST, PERSISTER_PORT and PERSISTER_WEB_APP_CONTEXT in the central configuration property file.

* Build using maven following the instructions above; this should generate a file `aidr-collector-X.war`
* Deploy `aidr-collector-X.war` to Glassfish following the instructions above.
* Test the deployment (optional). You can ping the collector service using the following command:
```
$ curl http://localhost:8080/aidr-collector/webresources/manage/ping

Response: 
{"startDate":"2014/12/14 16:22:12","currentStatus":"RUNNING"}
```
# 5. Tagger (aidr-tagger)

* Edit the central configuration properties file $AIDR_HOME/profiles/${PROFILE}/config.properties, to match the appropriate database login info. The properties which need to be verified are TAGGER_DB_NAME, TAGGER_DB_USERNAME and TAGGER_DB_PASSWORD.

* Compile the application to a jar file.

 `% mvn -Pdev install` (or right-click on `pom.xml`, Run as ... Maven build ... 'install')
     
* Start/run the application

  1. Run the following command: java -Xmx4048m -cp  $GLASSFISH_HOME/glassfish/lib/gf-client.jar:aidr-tagger-1.0-jar-with-dependencies.jar:libs/* qa.qcri.aidr.predict.Controller
  2. Make sure you are in the root directory (otherwise trained models will not be saved in the right part)
  3. The main method in the jar is in qa.qcri.aidr.predict.Controller
  4. You will see some incomprehensible debug output. If the numbers are not 0, input data is being processed.

This module does not need a deployment in the Glassfish server. Also, create the model folder in target (with appropriate permissions).
      
# 6. Tagger-API (aidr-tagger-api)

* Create JDBC resources in server (e.g., Glassfish) to match the JNDI names (JNDI/aidr_predict and JNDI/aidr_fetch_manager) used in [central configurations file $AIDR_HOME/profiles/${PROFILE}/config.properties to match the properties TAGGER_JNDI and MANAGER_JNDI)
* Build using maven following the instructions above; this should generate a file `aidr-tagger-api-X.war`
* Deploy `aidr-tagger-api-X.war` to Glassfish using the instructions above.
* Test the deployment (optional). You can check if `aidr-tagger-api` was installed correctly:
```
$ curl http://localhost:8080/AIDRTaggerAPI/rest/misc/ping

Response:
{"application":"AIDRTaggerAPI", "status":"RUNNING"}
```

# 7. Output (aidr-output)

* Set the Redis host and port number appropriately in the central configurations property file ($AIDR_HOME/profiles/${PROFILE}/config.properties). 
* Build using maven following the instructions above; this should generate a file `aidr-output-X.war`.
* Deploy `aidr-output-X.war` to Glassfish using the instructions above.


# 8. Trainer API (aidr-trainer-api)

* Pre-requisite: `aidr_predict` database(mysql) and aidr database(in postgres) should be created. `aidr_predict' database is created by aidr-db-manager module. 
* Create a new database called `aidr`. Grant appropriate table permissions (similar to the instructions for setting up the `aidr_predict` database) 

* Appropriately set the properties TAGGER_DB_USERNAME and TAGGER_DB_PASSWORD in the central configurations property file ($AIDR_HOME/profiles/${PROFILE}/config.properties).
* Build using maven following the instructions above; this should generate a file `aidr-trainer-api-X.war`
* Deploy `aidr-trainer-api.war` to Glassfish using the instructions above. 

# 9. Trainer Pybossa (aidr-trainer-pybossa)

* Pre-requisite: `aidr_predict` database and `aidr` database
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
* Build using maven following the instructions above; this should generate a file `aidr-trainer-pybossa.war`
* Deploy `aidr-trainer-pybossa.war` to Glassfish using the instructions above. 


# 10. Manager (aidr-manager)

Prior to start the project building process make sure you have completed the following steps (a-d):

(a) Apply the following changes to the central configurations property file($AIDR_HOME/profiles/${PROFILE}/config.properties):

   * In case of first time deployment, make sure you set the MANAGER_HBM2DDL property to 'create'.

**NOTE**:- If the application has previously been deployed and you have the MySql schema with tables in place then just remove the above line. The above line is used to create MySQL schema during the first deployment. After the first deployment, you can instead use 'update'.

   * You should also enter valid values for MANAGER_DB_USERNAME and MANAGER_DB_PASSWORD
   * twitter.consumerKey=<put here your Twitter's application consumer key>
   * twitter.consumerSecret=<put here your Twitter's application consumer key secret>
   * facebook.consumerKey=<put here your Facebook's application consumer key>
   * facebook.consumerSecret=<put here your Facebook's application consumer key secret>
   
(e) Apply the following changes to [system.properties](../tree/master/aidr-manager/src/main/resources/system.properties)
   * twitter.callBackURL=<here goes the URL where the aidr-manager application is accessible>. e.g., http://localhost:8080/AIDRFetchManager
   * application.secureUrl=<here goes the URL where the application is accessible>. e.g., http://localhost:8080/AIDRFetchManager
   * fetchMainUrl=Put here aidr-collector webresources path (e.g., http://localhost:8080/aidr-collector/webresources)
   * taggerMainUrl=Put here aidr-tagger-api webresources path (e.g., http://localhost:8080/aidr-tagger-api/rest
   * persisterMainUrl=Put here aidr-persister webresources path (e.g., http://localhost:8080/aidr-persister/webresources)
   * crowdsourcingAPIMainUrl=Put here aidr-trainer-api webresources path (e.g., http://localhost:8080/aidr-trainer-api/rest)
   * outputAPIMainUrl=Put here aidr-output webresources path (e.g., http://localhost:8080/aidr-output/rest)
   * serverUrl=Put here the server name on which AIDR is hosted

After the above steps have been executed, you can build the project:

* Build using maven following the instructions above; this should generate a file `aidr-manager.war`
* Deploy `aidr-manager.war` to Glassfish using the instructions above.