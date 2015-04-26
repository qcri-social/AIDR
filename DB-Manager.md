Name: aidr-db-manager

Code: https://github.com/qcri-social/AIDR/tree/master/aidr-db-manager

# Overview

The aidr-db-manager module provides a layer of abstraction for all the data that is stored in a database. The database is accessed using Hibernate. 

# Technologies

* EJB 3.2 (JPA 2.1, business logic encapsulation and inter-module communication)
* MySQL, Hibernate 4.3.5 (to access the MySQL DB)
* Jersey 2+ (for JAX-RX 2.0)
* Google GSON 2.2.4 (JSON processor)
* FasterXML (JSON processor) -- (redundant? --ChaTo)

# Module Dependencies

* [aidr-common](Common)

