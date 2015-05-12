_(In development as of May 2015)_

The persister tester is a program that can be used after deploying the persister, to test the persister independently of the other modules.

It only requires the aidr-persister module to be deployed.

## Command line

The persister tester has the following command-line options:

```
--config=FILE
--nitems=NUMBER (default 100)
```

The _config_ is the name of the persister configuration (or of a centralized configuration) to read properties that the tester needs to know to perform the testing: the URL of the persister's API, the name of the REDIS queues from which the persister reads, the name of the directory in which the persister writes, the base URL from which the generated CSV files need to be downloaded.

The _nitems_ is the number of items that should be written to the persister.

## Execution

The persister test should generate a collection name as `YYYYMMDDhhssmm-persister-test`. It should also generate synthetic Twitter messages with a fixed text but different tweetid.

**Phase A**: test the collector persister.

1. Call the `collectorPersister/start` API, if it doesn't return OK, FAIL.
1. Write to REDIS _nitems_/2 items, if writing is not possible, FAIL.
1. Call the `collectorPersister/status` API, if it doesn't return OK, FAIL.
1. Write to REDIS _nitems_/2 items, if writing is not possible, FAIL.
1. Call the `collectorPersister/stop` API, if it doesn't return OK, FAIL.
1. Call the `collectorPersister/status` API, if it doesn't return that the collection is not running, FAIL.
1. Read the directory where the persister file should be located, if the directory does not exist, FAIL.
1. Count the number of items in the file, if it doesn't match _nitems_, FAIL.
1. If this point is reached, exit with success code.

**Phase B**: test the taggerPersister, doing the same operations as above but against the `taggerPersister/` APIs.

**Phase C**: test a simple CSV generation routine.

1. After phase B is completed, call `taggerPersister/genCSV`, if it doesn't return OK, FAIL.
1. Download the corresponding CSV file, if it is not downloadable, FAIL.
1. Count the number of rows in this CSV file, if it doesn't match _nitems_, FAIL.
1. If this point is reached, exit with success code.

FAIL means printing a clear and informative message describing the condition and exiting with code 1 (non success).

On interrupt by the user, the persister tester should attempt to call the persister's `collectorPersister/stop` and `taggerPersister/stop` methods and exit with non-success code.