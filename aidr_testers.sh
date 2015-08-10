#!/bin/bash
#commands to run this script as a cron job in every 3 hours
#sudo crontab -e
#Add below line in the file opened by above command
#0 */3 * * * root sh /home/hadoop/AIDR/aidr_testers.sh

PROFILE=dev
AIDR_HOME=E:/AIDR/AIDR
COLLECTION_TASK_FILE=E:/collectionTask.properties
SEND_MAIL_API=http://localhost:8084/AIDRTaggerAPI/rest/misc/sendErrorEmail

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
	curl --data "module=AIDRCollector&description=Tester for aidr-collector failed on $PROFILE" $SEND_MAIL_API
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
	curl --data "module=AIDRPersister&description=Tester for aidr-persister failed on $PROFILE" $SEND_MAIL_API
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
	curl --data "module=AIDRTagger&description=Tester for aidr-tagger failed on $PROFILE" $SEND_MAIL_API
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
	curl --data "module=AIDROutput&description=Tester for aidr-output failed on $PROFILE" $SEND_MAIL_API
fi
