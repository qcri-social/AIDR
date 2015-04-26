Name: aidr-analysis

Code: https://github.com/qcri-social/AIDR/tree/master/aidr-analysis

# Overview

The aidr-analysis-module 


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