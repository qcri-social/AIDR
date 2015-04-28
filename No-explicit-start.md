The aidr-output, aidr-persister and aidr-analytics module do not require explicit per-collection start/stop (unlike aidr-collector).

Instead, each of these modules independently does the following:

1. listen to the Redis queue to which aidr-tagger writes
1. every time it detects a new collection, create a new in-memory data structure for that collections
1. in-memory data structures are destroyed if inactive for a long time