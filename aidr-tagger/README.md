
Installation
============

1. Requirements
---------------

MySQL Ver 14.14 Distr 5.5.29 for debian-linux-gnu (x86_64) [ubuntu: mysql-server-5.5]
PHP 5.3.10 [ubuntu: php5-cli]
Redis 2.6.13 [ubuntu: redis-server]
Java SE 1.7.0_17

Tested under Windows 7/Ubuntu 12.04.2

2. Database configuration
-------------------------

1) Create a database. This assumes database=aidr_predict, username=aidr_user, password=pass123 :

   % mysql -u root -p
   Enter password: [your mysql root user password]
   mysql> CREATE DATABASE aidr_predict;
   mysql> GRANT ALL PRIVILEGES ON aidr_predict.* TO aidr_user@localhost IDENTIFIED BY 'pass123';
   mysql> GRANT TRIGGER ON aidr_predict.* TO aidr_user@localhost IDENTIFIED BY 'pass123';
   mysql> QUIT;

2) Create the database schema running the src/main/scripts/create_db.sql script (this will DELETE your old data, if any):

   % mysql aidr_predict -u aidr_user -p < create_db.sql
   Enter password: pass123
   
3) Populate the database:

   % mysql aidr_predict -u aidr_user -p < populate_db_crisistype.sql
   Enter password: pass123
   
   % mysql aidr_predict -u aidr_user -p < populate_db_attributes.sql
   Enter password: pass123

4) Edit src/main/java/qa/qcri/aidr/predict/common/Config.java to match the database login info.

5) Compile the application to a jar file.

   % mvn install (or right-click on pom.xml, Run as ... Maven build ... 'install')
     
6) Start the application

   a) Make sure you are in the root directory (otherwise trained models will not be saved in the right part)
   b) The main method in the jar is in qa.qcri.aidr.predict.Controller
      You will see some incomprehensible debug output. If the numbers are not 0, input data is being processed.
      
