In case of a disconnection, the collector follows a strategy derived from the one Twitter recommends (https://dev.twitter.com/streaming/overview/connecting) but with a longer waiting time and randomization.

# TCP/IP level errors

If the error has `statusCode=-1`, i.e., it is a TCP/IP level error:

1. Put the collection in the `WARNING` state.
2. Wait for `RECONNECT_NET_FAILURE_WAIT_SECONDS * RANDOM(1..3) * ATTEMPT_NUMBER` seconds. `RANDOM(1...3)` is a floating point number between 1 and 3 (not an integer!). `RECONNECT_NET_FAILURE_WAIT_SECONDS` is by default 60 seconds, the configuration file must say linear back-off will be used.
3. Try to re-connect.
4. If `ATTEMPT_NUMBER++ > RECONNECT_NET_FAILURE_RETRY_ATTEMPTS` (configurable, default 5) times in a row, without receiving any tweet in between attempts, set the collection to the `ERROR` state.

# Rate limit errors

If the error has `statusCode == 420`, i.e. rate limit,

1. Put the collection in the `WARNING` state.
2. Wait for `RECONNECT_RATE_LIMIT_WAIT_SECONDS * RANDOM(1..3) * (2^ATTEMPT_NUMBER)`, where `ATTEMPT_NUMBER` is 0 initially. This generates exponentially increasing times. `RECONNECT_RATE_LIMIT_WAIT_SECONDS` is 60 by default; the config file must say this is the number recommended by Twitter and that exponential back-off will be used.
3. Try to re-connect
4. IF `ATTEMPT_NUMBER++ > RECONNECT_RATE_LIMIT_RETRY_ATTEMPTS`, set the collection to the ERROR state.

# Service unavailable errors

If the error has `statusCode == 503`, i.e. service unavailable, the same as 420 above (linear back-off) but use config variables `RECONNECT_SERVICE_UNAVAILABLE_WAIT_SECONDS` and `RECONNECT_SERVICE_UNAVAILABLE_RETRY_ATTEMPTS`.

# Other errors

IF the error has statusCode` == 401, 403, 404, 406, 413, 416`

1. Put the collection in the `ERROR` state.

