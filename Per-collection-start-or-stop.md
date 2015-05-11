# Explicit per-collection start/stop

The aidr-collector requires explicit per-collection start/stop to operate. This start/stop command is issued aidr-manager through the aidr-collector API.

The aidr-persister also requires explicit per-collection start/stop to operate. This start/stop command is issued by aidr-collector through the aidr-persister API. This is legacy of an old design, ideally the persister should not require explicit start/stop.

# No explicit per-collection start/stop

The aidr-output and aidr-analytics module do not require explicit per-collection start/stop.

Instead, each of these modules independently does the following:

1. listen to the Redis queue to which aidr-tagger writes
1. every time it detects a new collection, create a new in-memory data structure for that collections
1. in-memory data structures are destroyed if inactive for a long time