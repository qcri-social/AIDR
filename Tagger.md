Name: aidr-tagger (stand-alone Java application) and aidr-tagger-api (Java EE application)

Code: https://github.com/qcri-social/AIDR/tree/master/aidr-tagger and https://github.com/qcri-social/AIDR/tree/master/aidr-tagger-api

# Overview


# Technologies

aidr-tagger (stand-alone Java application):

* WEKA 3.7.6 (Machine Learning library)
* Google GSON 2.2.4 (JSON processor)
* Jackson 1.9.13 (JSON processor) -- (redundant? --ChaTo)
* EJB 3.2 remote client (access to aidr-task-manager and aidr-db-manager remote EJB services)
* MySQL
* JEDIS 2.1.0 (connecting to REDIS)

aidr-tagger-api (Java EE application):

* EJB 3.2
* Jersey 2+
* Jackson 1.9.13 (JSON processor)
* FasterXML (JSON processor) -- (redundant? --ChaTo)

# Module Dependencies

* [aidr-common](Common)
* [aidr-task-manager](Task Manager)
* [aidr-db-manager](DB Manager)
