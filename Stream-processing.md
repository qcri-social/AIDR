AIDR is a stream processing application, and operates under the same assumptions as a [https://en.wikipedia.org/wiki/Streaming_algorithm](streaming algorithm):

# Data is received as an unbounded stream of items.
# Data is read once and only once.
# Data cannot be kept in main memory.
# Data cannot be kept on disk.

We relax the last assumption but only in the [[persister]], where we dump the contents of the items into files. However, the operation of AIDR does not rely on these items, and even when the persister is off, the application can continue working.