Name: aidr-output

Code: https://github.com/Qatar-Computing-Research-Institute/CrisisComputing/tree/master/aidr-output

# Overview

The aidr-output module provides query and subscription APIs that allow users to see the items collected by aidr-collector and tagged by aidr-tagger.

In both cases, aidr-output reads the items from a Redis interface.

The aidr-output module has [no explicit per-collection start/stop](Per collection start or stop).

## Query interface

The aidr-output module maintains an in-memory buffer of the latest 1,000 items of each collection.

Then, it provides an API to query this buffer, e.g. to see the latest items, including filtering them according to various criteria.

## Subscription interface

The aidr-output module also allow users to subscribe to an HTTP interface for receiving a live stream of items for a collection.

# Packages

* qa.qcri.aidr.output.getdata implements the querying interface.
* qa.qcri.aidr.output.stream implements the subscription interface.
* qa.qcri.aidr.output.filter contains code to build and apply different kinds of filtering over the data.
* qa.qcri.aidr.output.util contains utility functions.
 

# Technologies

* JAVAX Servlet API 3.1.0 (Asynchronous servlets)
* JEDIS 2.7.0 (REDIS API library)
* Jersey 2+ (for JAX-RX 2.0)
* Google GSON 2.2.4 (JSON processor)
* Jackson 1.9.13 (JSON processor) (redudant? --ChaTo)

# Module Dependencies

* [aidr-common](Common)
* [aidr-collector](Collector)
* [aidr-tagger](Tagger)
