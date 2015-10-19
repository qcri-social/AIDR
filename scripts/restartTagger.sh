#!/bin/bash

#script used in glassfish upstart script to start tagger on glassfish start

GLASSFISH_HOME=/opt/glassfish4
AIDR_HOME=/data/AIDR
log=/data/scripts/tagger.log

date >> $log

pid=$(ps -eo pid,cmd | grep [q]cri.aidr.predict.Controller | awk '{print $1}')
kill -9 $pid

cd $AIDR_HOME/aidr-tagger
screen -d -m java -Xmx2048m -cp $GLASSFISH_HOME/glassfish/lib/gf-client.jar:target/aidr-tagger-1.0-jar-with-dependencies.jar:lib-non-maven/* qa.qcri.aidr.predict.Controller
echo "Tagger re-started" >> $log

