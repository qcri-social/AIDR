Conceptually, AIDR is a series of **processing elements** connected by pipelines. The processing elements are Java applications, and the pipelines that connect them are Redis queues.

AIDR is a stream processing application, and operates under the same assumptions as a [streaming algorithm](https://en.wikipedia.org/wiki/Streaming_algorithm):

* Data is received as an unbounded stream of items.
* Data is read once and only once.
* Data cannot be stored in main memory.
* Data cannot be stored on disk.

We relax the last assumption only in the [[persister]], where we dump the contents of the items into files. However, the operation of AIDR does not rely on the persister, and even when the persister is off, the application can continue working.

### References

For an academic perspective on this issue, see:

Muhammad Imran, Ioanna Lykourentzou, Yannick Naudet, Carlos Castillo: "Engineering Crowdsourced Stream Processing Systems". http://arxiv.org/abs/1310.5463