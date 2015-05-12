_(In development as of May 2015)_

The output tester is a program that can be used after deploying aidr-output, to test this module independently of the other modules.

It only requires the aidr-output module to be deployed.

## Command line

The output tester has the following command-line options:

```
--config=FILE
--nitems=NUMBER (default 2000)
```

The _config_ is the name of the output configuration (or of a centralized configuration) to read properties that the tester needs to know to perform the testing: the URL of the output's API, and the name of the REDIS queue from which output reads.

The _nitems_ is the number of items that should be written to the persister. It should be strictly greater than `GetBufferedAIDRData.MAX_MESSAGES_COUNT` so that we can appreciate the effects of the buffer.

## Execution

The output test should generate a collection name as `YYYYMMDDhhssmm-output-test`. It should also generate synthetic Twitter messages with fixed texts but random tweetids. It should keep those messages in memory for the duration of the text.

1. Write to REDIS _nitems_ items in the channel expected by output, if it fails, FAIL.
1. Call the `/crisis/fetch/channel/{crisisCode}` API, if it fails, FAIL.
1. Read from that API the returned tweets, if the number of messages is different from MAX_MESSAGES_COUNT (i.e. if it is greater than or less than MAX_MESSAGES_COUNT), FAIL.
1. Compare all the tweets received with the latest MAX_MESSAGES_COUNT tweets sent. If any of them is different, FAIL.
1. If this point is reached, exit with success code.

FAIL means printing a clear and informative message describing the condition and exiting with code 1 (non success).

On interrupt by the user, no action is needed.