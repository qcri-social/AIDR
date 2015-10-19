#/bin/bash

#cronjob is run to delete all logs older than 12 days (this is done after the files are copied to repository server)
find /data/archive_log -mtime +12 -exec rm {} \;


