Name: aidr-task-manager

Code: https://github.com/qcri-social/AIDR/tree/master/aidr-task-manager

# Overview

The aidr-task-manager module provides a layer of abstraction for handling item labelling.

Items that are labelled, or waiting to be labelled, from the [[task buffer]].

It interfaces with the [aidr-db-manager](DB Manager) to centralize access to the tables _document_ (representing a data item), _task answer_ (representing a label of a data item), and _task assignment_ (representing a data item that is waiting to be labelled).

# Technologies

This is implemented as a Java EE application, with methods accessed using EJB. It uses the following libraries:

* EJB  3.2 (business logic encapsulation and inter-module communication)
* FasterXML (JSON processor)

# Module Dependencies

* [aidr-common](Common)
* [aidr-db-manager](DB Manager)
