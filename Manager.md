Name: aidr-manager

Code: https://github.com/qcri-social/AIDR/tree/master/aidr-manager

# Overview

The aidr-manager module provides an interface for interacting with AIDR services. It performs tasks including user authorization, and keeping track of data about the different collections that have been created.

## Front-end

The aidr-manager module contains the front-end for the application, which is written in ExtJS.

## Back-end

The aidr-manager module has a back-end that interacts with [aidr-db-manager](DB Manager) to maintain data about each of the collections.

# Technologies

* ExtJS
* Spring Security, Spring Social Web, Spring Social Security, Spring ORM, Spring MVC 3.2
* Hibernate 3.6.9 (Persistence to aidr_fetch_manager MySQL DB)
* JPA 2.1, MySQL
* Twitter4j 4+ (why?)
* Jackson 1.9.13 (JSON processor)
* Jersey 2+

# Module Dependencies

* [aidr-common](Common)
* [aidr-db-manager](DB Manager)
