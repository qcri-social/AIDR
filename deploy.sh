#!/bin/bash
# AIDR Deployment Script

echo "Starting deployment of AIDR."
MODE=$1
RUN_DDL=$2
if [ "$MODE" == "" ]
then
echo "ERROR: Missing argument MODE. Please provide a value {deploy, undeploy, undeploy-deploy}."
exit
elif [ "$MODE" != "undeploy" ] && [ "$MODE" != "deploy" ] && [ "$MODE" != "undeploy-deploy" ] && [ "$MODE" != "redeploy" ]
then
echo "ERROR: Incorrect first argument provided. Please use 'deploy', 'undeploy' or 'undeploy-deploy'."
exit
fi

if [ "$RUN_DDL" != "" ] && [ "$RUN_DDL" != "deploy_db" ]
then
echo "ERROR: Argument RUN_DDL provided is incorrect. It can only take the value deploy_db."
exit
fi

echo "Step 1: Setting up environment variables."
# Setting environment variables.
GLASSFISH_HOME=/opt/glassfish4
AIDR_HOME=/home/meghna/Documents/GitHub/DEV
AIDR_GLASSFISH_DOMAIN=domain1
MY_SQL_USERNAME=root
AIDR_ANALYSIS_CONNECTION_POOL=AIDR_ANALYSIS_CONNECTION_POOL
AIDR_ANALYSIS_JNDI=JNDI/aidr_analysis
AIDR_PREDICT_CONNECTION_POOL=AIDR_PREDICT_CONNECTION_POOL
AIDR_PREDICT_JNDI=JNDI/aidr_predict
AIDR_DB_MANAGER_JNDI=JNDI/aidr_db_manager
AIDR_TASK_MANAGER_JNDI=JNDI/aidr_task_manager
AIDR_MANAGER_CONNECTION_POOL=AIDR_MANAGER_CONNECTION_POOL
AIDR_FETCH_MANAGER_JNDI=JNDI/aidr_fetch_manager
echo "Done."

# Go to $GLASSFISH_HOME
cd $GLASSFISH_HOME

if [ "$MODE" == "undeploy" ] || [ "$MODE" == "undeploy-deploy" ]
then
#Removing JDBC Resources.
echo "Removing JDBC Resources..."
bin/asadmin delete-jdbc-resource "$AIDR_ANALYSIS_JNDI"
bin/asadmin delete-jdbc-connection-pool "$AIDR_ANALYSIS_CONNECTION_POOL"
bin/asadmin delete-jdbc-resource "$AIDR_PREDICT_JNDI"
bin/asadmin delete-jdbc-resource "$AIDR_DB_MANAGER_JNDI"
bin/asadmin delete-jdbc-resource "$AIDR_TASK_MANAGER_JNDI"
bin/asadmin delete-jdbc-connection-pool "$AIDR_PREDICT_CONNECTION_POOL"
bin/asadmin delete-jdbc-resource "$AIDR_FETCH_MANAGER_JNDI"
bin/asadmin delete-jdbc-connection-pool "$AIDR_MANAGER_CONNECTION_POOL"
echo "Done."

echo "Undeploying Glassfish Applications."
bin/asadmin undeploy AIDRDBManager
bin/asadmin undeploy AIDRTaskManager
bin/asadmin undeploy AIDRPersister
bin/asadmin undeploy AIDRCollector
bin/asadmin undeploy AIDRTaggerAPI
bin/asadmin undeploy AIDROutput
bin/asadmin undeploy AIDRAnalytics
bin/asadmin undeploy AIDRTrainerAPI
bin/asadmin undeploy AIDRFetchManager

echo "Stopping Application AIDRTagger."
PID=$(ps -eo pid,cmd | grep [q]a.qcri.aidr.predict.Controller | awk '{print $1}')
kill -9 $PID

echo "Done."

# stop the glass fish domain.
bin/asadmin stop-domain $AIDR_GLASSFISH_DOMAIN
fi

if [ "$MODE" == "deploy" ] || [ "$MODE" == "undeploy-deploy" ]
then

echo "Starting Glassfish Domain: " $AIDR_GLASSFISH_DOMAIN
# start glass fish domain
bin/asadmin start-domain $AIDR_GLASSFISH_DOMAIN
echo "Starting AIDR Applications."
# Set the default mode of Glassfish to use Implicit CDI
bin/asadmin set configs.config.server-config.cdi-service.enable-implicit-cdi=true

# Creating JDBC Resources
bin/asadmin create-jdbc-connection-pool --driverclassname=com.mysql.jdbc.Driver --restype=java.sql.Driver --steadypoolsize=8 --maxpoolsize=32 --maxwait=60000 --isisolationguaranteed=true --ping=true --property user=aidr_admin:password=aidr_admin:url="jdbc\\:mysql\\://localhost\\:3306/aidr_analysis" "$AIDR_ANALYSIS_CONNECTION_POOL"
bin/asadmin create-jdbc-resource --connectionpoolid="$AIDR_ANALYSIS_CONNECTION_POOL" "$AIDR_ANALYSIS_JNDI"
bin/asadmin create-jdbc-connection-pool --driverclassname=com.mysql.jdbc.Driver --restype=java.sql.Driver --steadypoolsize=8 --maxpoolsize=32 --maxwait=60000 --isisolationguaranteed=true --ping=true --property user=aidr_admin:password=aidr_admin:url="jdbc\\:mysql\\://localhost\\:3306/aidr_predict" $AIDR_PREDICT_CONNECTION_POOL
bin/asadmin create-jdbc-resource --connectionpoolid=AIDR_PREDICT_CONNECTION_POOL $AIDR_PREDICT_JNDI
bin/asadmin create-jdbc-resource --connectionpoolid=AIDR_PREDICT_CONNECTION_POOL $AIDR_DB_MANAGER_JNDI
bin/asadmin create-jdbc-resource --connectionpoolid=AIDR_PREDICT_CONNECTION_POOL $AIDR_TASK_MANAGER_JNDI
bin/asadmin create-jdbc-connection-pool --driverclassname=com.mysql.jdbc.Driver --restype=java.sql.Driver --steadypoolsize=8 --maxpoolsize=32 --maxwait=60000 --isisolationguaranteed=true --ping=true --property user=aidr_admin:password=aidr_admin:url="jdbc\\:mysql\\://localhost\\:3306/aidr_fetch_manager" $AIDR_MANAGER_CONNECTION_POOL
bin/asadmin create-jdbc-resource --connectionpoolid=AIDR_MANAGER_CONNECTION_POOL $AIDR_FETCH_MANAGER_JNDI

# Deploying separate modules. First undeploying if already deployed and deploying again.
bin/asadmin deploy --contextroot=AIDRDBManager --name=AIDRDBManager $AIDR_HOME/aidr-db-manager/target/aidr-db-manager-ear-1.0.ear
bin/asadmin deploy --contextroot=AIDRTaskManager --name=AIDRTaskManager $AIDR_HOME/aidr-task-manager/target/aidr-task-manager-ear-1.0.ear
bin/asadmin deploy --contextroot=AIDRPersister --name=AIDRPersister $AIDR_HOME/aidr-persister/target/aidr-persister-1.0.war
bin/asadmin deploy --contextroot=AIDRCollector --name=AIDRCollector $AIDR_HOME/aidr-collector/target/aidr-collector-1.0.war
bin/asadmin deploy --contextroot=AIDRTaggerAPI --name=AIDRTaggerAPI $AIDR_HOME/aidr-tagger-api/target/aidr-tagger-api-1.0.war
bin/asadmin deploy --contextroot=AIDROutput --name=AIDROutput $AIDR_HOME/aidr-output/target/aidr-output-1.0.war
bin/asadmin deploy --contextroot=AIDRAnalytics --name=AIDRAnalytics $AIDR_HOME/aidr-analytics/target/aidr-analytics-1.0.war
bin/asadmin deploy --contextroot=AIDRTrainerAPI --name=AIDRTrainerAPI $AIDR_HOME/aidr-trainer-api/target/aidr-trainer-api.war

bin/asadmin set configs.config.server-config.cdi-service.enable-implicit-cdi=false
bin/asadmin deploy --properties implicitCdiEnabled=false --contextroot=AIDRFetchManager --name=AIDRFetchManager $AIDR_HOME/aidr-manager/target/aidr-manager.war
bin/asadmin set configs.config.server-config.cdi-service.enable-implicit-cdi=true

echo "Starting Application AIDRTagger."
cd $AIDR_HOME/aidr-tagger
nohup java -Xmx2048m -cp $GLASSFISH_HOME/glassfish/lib/gf-client.jar:aidr-tagger-1.0-jar-with-dependencies.jar:libs/* qa.qcri.aidr.predict.Controller &
fi

if [ "$MODE" == "redeploy" ]
then

# Deploying separate modules. First undeploying if already deployed and deploying again.
bin/asadmin redeploy --keepstate=true --contextroot=AIDRDBManager --name=AIDRDBManager $AIDR_HOME/aidr-db-manager/target/aidr-db-manager-ear-1.0.ear
bin/asadmin redeploy --keepstate=true --contextroot=AIDRTaskManager --name=AIDRTaskManager $AIDR_HOME/aidr-task-manager/target/aidr-task-manager-ear-1.0.ear
bin/asadmin redeploy --keepstate=true --contextroot=AIDRPersister --name=AIDRPersister $AIDR_HOME/aidr-persister/target/aidr-persister-1.0.war
bin/asadmin redeploy --keepstate=true --contextroot=AIDRCollector --name=AIDRCollector $AIDR_HOME/aidr-collector/target/aidr-collector-1.0.war
bin/asadmin redeploy --keepstate=true --contextroot=AIDRTaggerAPI --name=AIDRTaggerAPI $AIDR_HOME/aidr-tagger-api/target/aidr-tagger-api-1.0.war
bin/asadmin redeploy --keepstate=true --contextroot=AIDROutput --name=AIDROutput $AIDR_HOME/aidr-output/target/aidr-output-1.0.war
bin/asadmin redeploy --keepstate=true --contextroot=AIDRAnalytics --name=AIDRAnalytics $AIDR_HOME/aidr-analytics/target/aidr-analytics-1.0.war
bin/asadmin redeploy --keepstate=true --contextroot=AIDRTrainerAPI --name=AIDRTrainerAPI $AIDR_HOME/aidr-trainer-api/target/aidr-trainer-api.war
bin/asadmin set configs.config.server-config.cdi-service.enable-implicit-cdi=false
bin/asadmin redeploy --properties implicitCdiEnabled=false --contextroot=AIDRFetchManager --name=AIDRFetchManager $AIDR_HOME/aidr-manager/target/aidr-manager.war
bin/asadmin set configs.config.server-config.cdi-service.enable-implicit-cdi=true

echo "Stopping Application AIDRTagger."
PID=$(ps -eo pid,cmd | grep [q]cri.aidr.predict.Controller | awk '{print $1}')
kill -9 $PID

echo "Starting Application AIDRTagger."
cd $AIDR_HOME/aidr-tagger
nohup java -Xmx2048m -cp $GLASSFISH_HOME/glassfish/lib/gf-client.jar:target/aidr-tagger-1.0-jar-with-dependencies.jar:lib-non-maven/* qa.qcri.aidr.predict.Controller &
fi

if [ "$RUN_DDL" == "deploy_db" ]
then
echo "Deploying the database script."
mysql -u "$MY_SQL_USERNAME" -p < $AIDR_HOME/postInstall.sql
fi

echo "End of deployment script..."
