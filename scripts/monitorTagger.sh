#!/bin/bash

GLASSFISH_HOME=/home/aidr.dev/glassfish4
AIDR_HOME=/home/aidr.dev/AIDR
filePath=/home/aidr.dev/taggerMonitor
log=/home/aidr.dev/taggerMonitor/tagger.log
HOST=http://localhost:8084
SEND_MAIL_API=$HOST/AIDRTaggerAPI/rest/misc/sendErrorEmail
tagger_stop_description="Stopping Tagger."$'\n'
tagger_start_description="Tagger Started."$'\n'
glassfish_start_description="Glassfish Started."$'\n'

glassfishPid=$(ps -eo pid,cmd | grep [g]lassfish/modules/glassfish.jar | awk '{print $1}')

screenpid=$(ps -eo pid,cmd | grep [q]cri.aidr.predict.Controller | awk '{print $1}')

date >> $log
if [ -z "$glassfishPid" ]
then
        if [ -n "$screenpid" ]
        then

                pids=($(echo ${screenpid}))
                pid=$(ps -el | grep ${pids[0]} | grep java | awk '{print $4}')
                echo "tagger Process ID" $pid >> $log
                kill -9 $pid
                echo "Tagger stopped" >> $log
        fi
        $GLASSFISH_HOME/bin/asadmin start-domain
        echo "Glassfish started" >> $log
        curl --data "module=AIDRTaggerAPI&description=$glassfish_start_description" $SEND_MAIL_API

        cd $AIDR_HOME/aidr-tagger
        curl --data "module=AIDRTaggerAPI&description=$tagger_stop_description" $SEND_MAIL_API

        screen -d -m java -Xmx4096m -cp $GLASSFISH_HOME/glassfish/lib/gf-client.jar:target/aidr-tagger-1.0-jar-with-dependencies.jar:lib-non-maven/* qa.qcri.aidr.predict.Controller
        echo "Tagger started" >> $log

elif [ -z "$screenpid" ]
then
        glassfishpid=$( ps aux | grep glassfish/domains | awk '{print $2}')
        glassfishpids=($(echo ${glassfishpid}))
        glassfishpid=$(ps -el | grep ${glassfishpids[0]} | grep java | awk '{print $4}')

        if [ -n "$glassfishpid" ]
        then
                cd $AIDR_HOME/aidr-tagger
                curl --data "module=AIDRTaggerAPI&description=$tagger_stop_description" $SEND_MAIL_API
                screen -d -m java -Xmx4096m -cp $GLASSFISH_HOME/glassfish/lib/gf-client.jar:target/aidr-tagger-1.0-jar-with-dependencies.jar:lib-non-maven/* qa.qcri.aidr.predict.Controller
                curl --data "module=AIDRTaggerAPI&description=$tagger_start_description" $SEND_MAIL_API
                echo "Tagger started" >> $log
        fi
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
                        $((killedThreads++))
                        echo "thread not found" ${array[i]} $pid >> $log
                fi
        done

        if [ ${killedThreads} -gt 0 ]
 	then
                kill -9 $pid
                glassfishpid=$( ps aux | grep glassfish/domains | awk '{print $2}')
                glassfishpids=($(echo ${glassfishpid}))
                glassfishpid=$(ps -el | grep ${glassfishpids[0]} | grep java | awk '{print $4}')

                if [ -z "$glassfishpid" ]
                then
                   echo " "
                else
                        cd $AIDR_HOME/aidr-tagger
                        curl --data "module=AIDRTaggerAPI&description=$tagger_stop_description" $SEND_MAIL_API
                        screen -d -m java -Xmx4096m -cp $GLASSFISH_HOME/glassfish/lib/gf-client.jar:target/aidr-tagger-1.0-jar-with-dependencies.jar:lib-non-maven/* qa.qcri.aidr.predict.Controller
                        curl --data "module=AIDRTaggerAPI&description=$tagger_start_description" $SEND_MAIL_API
                fi
        fi

        echo "threads killed" ${killedThreads} >> $log
fi
                        

