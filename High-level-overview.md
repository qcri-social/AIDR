AIDR is a [[stream processing]] application in Java implemented through a series of modules. The following diagram illustrates at a very high level the relationships between the different modules.

![High-level diagram](http://i.imgur.com/bEf5ErD.png)

Operators of the AIDR platform operate AIDR through the [[Manager]]. The first thing operators do is to use the [[Collector]] to start collecting items. Those items are passed to the [[Tagger]], which automatically annotates then. Then, the [[Output]] module keeps a small buffer of up to 1K items for viewing, and the [[Persister]] stores the items on disk so they can be downloaded.

The [[Tagger]] also samples a small number of items in the [[task buffer]] to get human-provided labels for them. Those labels are provided by the operator of the platform, through the [[Trainer]], or can be provided by external annotators through the [[PyBossa Trainer]].

Additionally, the [[DB Manager]] provides a layer of abstraction of the database, and the [[Task Manager]] provides a layer of abstraction over the elements belonging to the [[task buffer]].

# An illustrated guide to data flows in AIDR

See [An illustrated guide to data flows in AIDR](https://docs.google.com/presentation/d/1rdArb279kbXOBna6DRMTjJlhHyFRTi6_wu3IPh-qdRo/present#)