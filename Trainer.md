Name: aidr-trainer

Code: https://github.com/qcri-social/AIDR/tree/master/aidr-trainer-api

# Overview

The aidr-trainer module manages the [[task buffer]].

Items enter the task buffer if they are selected by the [tagger](Tagger). Items can exit the task buffer if a long time (e.g. 12 hours) pass and they have neither been assigned nor been labelled.

Items can be labelled in the task buffer by an AIDR operator, or via crowdsourcing. In the first case, we say the items are labelled through the _internal tagging interface_, which is a part of AIDR's front-end that is accessible to AIDR operators. In the second case, we say the items are labelled through _PyBossa_, which happens our own [PyBossa](http://pybossa.com/) installation, named [MicroMappers](http://clickers.micromappers.org/).

In both cases, an item passes through the following steps:

1. The item is waiting to be assigned.
1. The item is assigned to an operator, or to MicroMappers.
1. The item is labelled by the person/group it was assigned.

The aidr-trainer module also takes care of expiring old items that wait for too long for a label, in order to keep the _task buffer_ relatively small.

# Technologies

The aidr-trainer is a Java EE application using Spring and the following libraries:

* Spring ORM, Spring JDBC, Spring MVC 3.2
* JEDIS 2.1.0 (REDIS API library)
* Hibernate 4.1.5 (persistence) -- (why? --ChaTo)
* MySQL -- (why? --ChaTo)
* Jersey 1.18.1
* JSON-Simple 1.1 (JSON Processor) -- (redundant? --ChaTo)
* Jackson 1.9.13 (JSON processor)
* Jersey 2+
* HttpClient 4.2.5

# Module Dependencies

* [aidr-common](Common)
* [aidr-task-manager](Task Manager)
* [aidr-db-manager](DB Manager)
