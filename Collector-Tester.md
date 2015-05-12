# Collector Tester

## Command line

The collector tester has the following command-line options:

```
--config=FILE
--collection-task=FILE
--time=SECONDS (default 60)
--quiet
```

The _config_ is the name of the collector configuration (or of a centralized configuration) to read properties such as the URL of the collector's API, and the name of the REDIS queue where the collector will write.

The _collection-task_ is a file containing a CollectionTask object serialized in .properties, XML, or JSON format. A simple collection-task.properties file is included which describes the following test (search for tweets in English containing the keyword "uk", which are many):

```
collectionCode = testcollection
keywords = uk
languageFilter = en
persist = false
```

The _time_ is the number of seconds during which the test has to be done.

The _quiet_ option suppresses the print of the tweets. All other messages are printed even in _quiet_ mode.

## Execution

The collector tester should perform the following steps:

1. Read the _collection-task_
1. Subscribe to the REDIS queue where the collector writes data. This thread will print to the screen the received tweets (just the timestamp and first 40 characters of the text), unless the _quiet_ option is specified.
1. Call the collector's `/start` method to start a collection; if the return code is not OK, exit with an error code.
1. Wait for 1/2 of the _time_
1. Call the collector's  `/status` method to get the status of the collection.
1. If the status returned is not OK, exit with an error code.
1. Wait for 1/2 of the _time_
1. Call the collector's `/stop` method; if the return code is not OK, exit with an error code.
1. Call the collector's  `/status` method, if the status returned is different from "not running", exit with an error code.
1. Print the number of tweets received so far, and exit with an error code if this number is zero.
1. Exit with normal exit code otherwise.

## Signal handling

On interrupt by the user, the collector tester should attempt to call the collector's `/stop` method and exit.