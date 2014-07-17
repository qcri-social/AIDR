For uniform logging messages across AIDR modules we will be using the Apache log4j logger:

## To enable logging for your module

Please note each module has its own log. In this case, we assume the module wants to write to aidr-output.log:

* Create a file called `log4j.properties` in `/src/main/resources` with the following content:

`    

     # Root logger option
     log=/var/log/aidr
     log4j.rootLogger=INFO, file
 
     # Direct log messages to a log file
     log4j.appender.file=org.apache.log4j.RollingFileAppender
 
     # Replace aidr-output.log with the name of your log file
     log4j.appender.file.File=${log}/aidr-output.log
     log4j.appender.file.MaxFileSize=30MB
     log4j.appender.file.MaxBackupIndex=100
     log4j.appender.file.layout=org.apache.log4j.PatternLayout
     log4j.appender.file.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:[%t][%C][%M:%L] - %m%n
    
`
* Create a directory `/var/log/aidr` and appropriately set the r/w permissions for it. 
* In your `pom.xml`, include log4j dependency: 

`

       <dependency>
	          <groupId>log4j</groupId>
	          <artifactId>log4j</artifactId>
	          <version>1.2.17</version>
       </dependency>
`

* In your class that will use logging, add the following:
                  `private static Logger logger = Logger.getLogger(Foo.class.getName());` where `Foo` is your class name. 
* You can log messages in your class using the above logger.

## Log message standards

Your log message should include:

* Class name
* Method name
* Collection code (if available), or collection-id (if available)
* Classifier code/id, Document id, or whatever information in case the collection code is not available.

Before throwing an exception, always create a log entry.