#!/bin/bash
#commands to run this script as a cron job in every 3 hours
#sudo crontab -e
#Add below line in the file opened by above command
#0 */3 * * * root sh /home/hadoop/AIDR/aidr_testers.sh

export MAVEN_OPTS="-Xms256m -Xmx2048m -XX:PermSize=256m"
PROFILE=dev
AIDR_HOME=E:/AIDR/AIDR
COLLECTION_TASK_FILE=E:/collectionTask.properties
HOST=http://localhost:8084
SEND_MAIL_API=$HOST/AIDRTaggerAPI/rest/misc/sendErrorEmail
ALL_RUNNING_COLLECTIONS_API=$HOST/AIDRCollector/webresources/twitter/status/all 

#Collector Tester
cd $AIDR_HOME
cd aidr-collector
mvn -P$PROFILE test -Dtest=CollectorTesterTest -DcollectionTask=$COLLECTION_TASK_FILE -Dtime=10
result=$?
#result=0(success), 1(fail)
if [[ $result != 0 ]] ; then
	#Sending mail
	echo CollectorTester Failed
	echo Sending Mail
	runningCollections="Running Collections"$'\n'$(curl -get $ALL_RUNNING_COLLECTIONS_API | jq '.[] |"CollectionName: "+ .collectionName' | sort)
	freeMemory="Memory Statistics"$'\n'$(egrep --color 'Mem|Cache|Swap|Buffer' /proc/meminfo)
	description="Tester for AIDRCollector failed on $PROFILE"$'\n'$runningCollections$'\n'$freeMemory
	curl --data "module=AIDRCollector&description=$description" $SEND_MAIL_API
fi

#Persister Tester
cd $AIDR_HOME
cd aidr-persister
mvn -P$PROFILE test -Dtest=PersisterTesterTest
result=$?
#result=0(success), 1(fail)
if [[ $result != 0 ]] ; then
	#Sending mail
	echo PersisterTester Failed
	echo Sending Mail
	runningCollections="Running Collections"$'\n'$(curl -get $ALL_RUNNING_COLLECTIONS_API | jq '.[] |"CollectionName: "+ .collectionName' | sort)
	freeMemory="Memory Statistics"$'\n'$(egrep --color 'Mem|Cache|Swap|Buffer' /proc/meminfo)
	description="Tester for AIDRPersister failed on $PROFILE"$'\n'$runningCollections$'\n'$freeMemory
	curl --data "module=AIDRPersister&description=$description" $SEND_MAIL_API
fi

#Tagger Tester
cd $AIDR_HOME
cd aidr-tagger
mvn -P$PROFILE test -Dtest=TaggerTesterTest -DskipTests=false -Dnitems-test=100 
result=$?
#result=0(success), 1(fail)
if [[ $result != 0 ]] ; then
	#Sending mail
	echo TaggerTester Failed
	echo Sending Mail
	runningCollections="Running Collections"$'\n'$(curl -get $ALL_RUNNING_COLLECTIONS_API | jq '.[] |"CollectionName: "+ .collectionName' | sort)
	freeMemory="Memory Statistics"$'\n'$(egrep --color 'Mem|Cache|Swap|Buffer' /proc/meminfo)
	description="Tester for AIDRTagger failed on $PROFILE"$'\n'$runningCollections$'\n'$freeMemory
	curl --data "module=AIDRTagger&description=$description" $SEND_MAIL_API
fi

#Output Tester
cd $AIDR_HOME
cd aidr-output
mvn -P$PROFILE test -Dtest=OutputTesterTest
result=$?
#result=0(success), 1(fail)
if [[ $result != 0 ]] ; then
	#Sending mail
	echo OutputTester Failed
	echo Sending Mail
	runningCollections="Running Collections"$'\n'$(curl -get $ALL_RUNNING_COLLECTIONS_API | jq '.[] |"CollectionName: "+ .collectionName' | sort)
	freeMemory="Memory Statistics"$'\n'$(egrep --color 'Mem|Cache|Swap|Buffer' /proc/meminfo)
	description="Tester for AIDROutput failed on $PROFILE"$'\n'$runningCollections$'\n'$freeMemory
	curl --data "module=AIDROutput&description=$description" $SEND_MAIL_API
fi
