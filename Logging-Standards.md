## Log message standards

## What information to include

With the above configuration, log4j will automatically include the thread-id, class, and method name in the log file. Additionally, your log message must specify:

* **Collection code** (if available), or collection-id (if available)
* Classifier code/id, Document id, or whatever information in case the collection code is not available.

## When to log

### Panic messages: panic() = error() + event in DB + e-mail

A panic message signals any anomalous condition in the system that _interrupt this and future operations_, i.e. that will prevent this user and future users from doing what they wanted to do. If disk is full, and you need to write to disk, you need a panic message, because it means the next users won't be able to do their operations if this continue like this. 

A panic message does three actions:

* Log an error
* Place an event in the database
* Send an e-mail

### Severe errors: severe_error() = error() + event in DB

If you have an error that absolutely needs to be seen by a system administrator, mark it as a severe_error(). This should create both an error and an entry in the database.

### Errors and severe errors: error()

Use error() for any anomalous condition in the system that _interrupts the current operation_, i.e. that prevents users from doing what they wanted to do. For instance, if you are writing to a file, but couldn't open the file, log an error. If you had any problem that means you couldn't do what the user asked for, log an error.

Please note that errors arising from incorrect user input (e.g. the user tried to create a collection with a name that already exists) do not constitute a system error and don't need to be logged as such, instead can be optionally logged as warning if needed.

Before throwing an exception, always create a log entry.

### Warnings: warn()

Use warning for any anomalous condition that _does not interrupt the current operation_, i.e. that still allow users to do what they wanted to do. For instance, if you are writing lines to a file, but had to skip one line because of some formatting error, log a warning. If there was a small problem but you were able to easily deal with it, and will continue with the current operation, throw a warning. If you noticed anything strange, but still can proceed, log a warning.

### Normal actions: info()

Use info() for everything else that is part of the normal operation, e.g. startup messages, but remember to log _only once per operation_. Don't say info("Opening file ..."), info("Writing to file ..."), info("Closed file ..."). Instead, do info("Wrote to file ...")

# Using Apache Log4J in AIDR

For uniform logging of messages in AIDR modules, we will be using the `Apache log4j` logger:

## To enable logging for your module

Please note each module has its own log. In this case, we assume the module wants to write to `aidr-output.log`:

* Create a file called `log4j.properties` in `/src/main/resources` with the following content:
```
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
log4j.appender.file.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p [%t][%C][%M] - %m%n
```

* Create a directory `/var/log/aidr` and appropriately set the r/w permissions for it. 
* In your `pom.xml`, include `log4j` dependency: 

```
 <dependency>
  <groupId>log4j</groupId>
  <artifactId>log4j</artifactId>
  <version>1.2.17</version>
 </dependency>
```
* In your class that will use logging, add the following:
```
private static Logger logger = Logger.getLogger(Foo.class.getName());
```
where `Foo` is your class name. 
* You can log messages in your class using the above logger.

