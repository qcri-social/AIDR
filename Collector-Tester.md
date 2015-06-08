The collector tester is a program that can be used after deploying the collector, to test the collector independently of the other modules.

It only requires the aidr-collector module to be deployed.

## Command line

The collector tester is run through the following command:

```
mvn test -DcollectorTesterTest PARAMETERS
```

This parameter is mandatory:

```
-Dcollection-task=FILE
```

These parameters are optional:

```
-Dconfig=FILE
-Dtime=SECONDS (default 60)
-Dquiet=TRUE/FALSE (default false)
```

The _config_ is the name of the collector configuration (or of a centralized configuration) to read properties that the tester needs to know to perform the testing: the URL of the collector's API, the name of the REDIS queue where the collector will write.

The _collection-task_ is a file containing a CollectionTask object serialized in .properties format. A simple collection-task.properties file is included which describes the following test (listen for tweets in English containing the keyword "uk", which are many):

```
collectionCode = collector_tester
keywords = uk
languageFilter = en
persist = false
```

The _time_ is the number of seconds during which the test has to be done.

The _quiet_ option suppresses the print of the tweets. All other messages are printed even in _quiet_ mode.

## Execution

The collector tester should perform the following steps:

1. Read the _collection-task_
1. Subscribe to the REDIS queue where the collector writes data. This thread will print to the screen the received tweets (just the timestamp and first 40 characters of the text), unless the _quiet_ option is specified. If subscription to REDIS fails, FAIL.
1. Call the collector's `/start` method to start a collection; if the return code is not OK, FAIL.
1. Wait for 1/2 of the _time_
1. Call the collector's  `/status` method to get the status of the collection.
1. If the status returned is not OK, FAIL.
1. Wait for 1/2 of the _time_
1. Call the collector's `/stop` method; if the return code is not OK, FAIL.
1. Call the collector's  `/status` method, if the status returned is different from "not running", FAIL.
1. Print the number of tweets received so far, if the number is zero, FAIL.
1. Exit with exit code 0 (success) otherwise.

FAIL means printing a clear and informative message describing the condition and exiting with code 1 (non success).

On interrupt by the user, the collector tester should attempt to call the collector's `/stop` method and exit with non-success code.

