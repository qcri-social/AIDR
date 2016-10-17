Name: aidr-manager

Code: https://github.com/Qatar-Computing-Research-Institute/CrisisComputing/tree/master/aidr-manager

# Overview

The aidr-manager module provides an interface for interacting with AIDR services. It performs tasks including user authorization, and keeping track of data about the different collections that have been created.

Login to the aidr-manager is done using Spring Social against Twitter.

## Front-end

The aidr-manager module contains the front-end for the application, which is written in ExtJS. It interacts with the back-end via AJAX calls.

## Back-end

The aidr-manager module has a back-end that interacts with [aidr-db-manager](DB Manager) to maintain data about each of the collections. The back-end also issues calls to all the other modules of AIDR.

# Technologies

The front-end is implemented using ExtJS.

The back-end is implemented as a Java EE application, using the Spring framework and the following libraries:

* Spring Security, Spring Social Web, Spring Social Security, Spring ORM, Spring MVC 3.2
* Hibernate 3.6.9 (to access a MySQL DB) -- (why? isn't this done through aidr-db-manager? --ChaTo)
* JPA 2.1, MySQL
* Twitter4j 4+ (why? --ChaTo)
* Jackson 1.9.13 (JSON processor)
* Jersey 2+

# Module Dependencies

* [aidr-common](Common)
* [aidr-db-manager](DB Manager)
