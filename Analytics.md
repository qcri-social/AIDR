Name: aidr-analytics

Code: https://github.com/qcri-social/AIDR/tree/master/aidr-analytics

# Overview

The aidr-analytics module maintains various statistics of the tagged items. It is responsible for keeping information about how many items on a collection have been tagged into various categories, at different time granularities (5 minutes, 1 hour, and 1 day). 

# Technologies

The aidr-analytics module is a Java EE application using Spring and the following libraries:

* JEDIS 2.2.1 (connecting to REDIS)
* FasterXML (JSON processor)
* Google GSON 2.2.4 (JSON processor) -- (redundant? --ChaTo)
* Hibernate 4.3.5 (for MySQL) -- (why? --ChaTo)
* JPA 2.1
* Jersey 2+

# Module Dependencies

* [aidr-common](Common)
* [aidr-output](Output)