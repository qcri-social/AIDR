Name: aidr-trainer-pybossa

Code: https://github.com/qcri-social/AIDR/tree/master/aidr-trainer-pybossa

# Overview

The aidr-trainer-pybossa, also known as the **PyBossa Scheduler** is responsible for exchanging items from the [[task buffer]] to and from our [PyBossa](http://pybossa.com/) installation, named [MicroMappers](http://clickers.micromappers.org/).

It operates periodically by:

1. Checking if there are items waiting to be labelled in the task buffer.
1. Marking those items as assigned to MicroMappers
1. Send those items from AIDR to MicroMappers
1. Checking periodically in MicroMappers if there are new labelled items
1. Send those items from MicroMappers to AIDR

# Technologies

The aidr-trainer-pybossa is a Java EE application using Spring and the following libraries:

* Spring ORM, Spring JDBC, Spring MVC 3.2, JEDIS 2.1.0 (REDIS API library)
* Hibernate 4.1.5 (for MySQL) -- (why? --ChaTo)
* Jersey 1.18.1
* OpenCSV 2.3
* JSON-Simple 1.1 (JSON Processor)
* Jersey 2+

# Module Dependencies

* [aidr-common](Common)
* [aidr-task-manager](Task Manager)