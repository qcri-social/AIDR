#/bin/bash

#copy all files to be archived to a folder - this folder will be synced with the data repository server

find /data/log -mtime +12 -exec mv "{}" /data/archive_log/ \;

