#!/bin/bash

GLASSFISH_HOME=/home/aidr.dev/glassfish4
AIDR_HOME=/home/aidr.dev/AIDR
filePath=/home/aidr.dev/taggerMonitor
log=/home/aidr.dev/taggerMonitor/tagger.log

screenpid=$(ps -eo pid,cmd | grep [q]cri.aidr.predict.Controller | awk '{print $1}')

date >> $log

if [ -z "$screenpid" ]
then
	cd $AIDR_HOME/aidr-tagger
	screen -d -m java -Xmx2048m -cp $GLASSFISH_HOME/glassfish/lib/gf-client.jar:target/aidr-tagger-1.0-jar-with-dependencies.jar:lib-non-maven/* qa.qcri.aidr.predict.Controller
	echo "Tagger started" >> $log
else

	pids=($(echo ${screenpid}))
	pid=$(ps -el | grep ${pids[0]} | grep java | awk '{print $4}')
	echo "tagger Process ID" $pid >> $log

	status=0
	filename=$filePath"/jstack."$pid
	killedThreads=0

	#list of threads to look for in stack trace
	array=()
	readarray array < $filePath/taggerThreads.txt

	cnt=${#array[@]}

	#get thread dump for tagger
	jstack $pid > $filename

	for ((i=0; i<${cnt}; i++))
	do
	        status=$(grep ${array[i]} ${filename} | wc -l)
		if [ ${status} -eq 0 ]
		then
                	#killed thread is found. Restart tagger
                	killedThreads++
                	echo "thread not found" ${array[i]} $pid >> $log
		fi
	done

	if [ ${killedThreads} -gt 0 ]
	then
		kill -9 $pid
		cd $AIDR_HOME/aidr-tagger
		screen -d -m java -Xmx2048m -cp $GLASSFISH_HOME/glassfish/lib/gf-client.jar:target/aidr-tagger-1.0-jar-with-dependencies.jar:lib-non-maven/* qa.qcri.aidr.predict.Controller
	fi

	echo "threads killed" ${killedThreads} >> $log
fi

