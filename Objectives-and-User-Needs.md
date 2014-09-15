This document describes the strategic direction of AIDR in terms of objectives and user needs.

# Objectives

The objective of AIDR is to provide a platform to collect and classify in real-time a large amount of user-generated messages during humanitarian crises. The platform applies human intelligence (crowdsourcing) and machine intelligence (machine learning) to perform these tasks. This platform is provided as a service via a website, and as free and open source software.

The main success metrics of AIDR are:

* The number of crises that can be handled simultaneously: AIDR should support tens of crises at the same time.
* The number of messages that can be classified per unit of time: AIDR should support up to hundreds of messages per minute.
* The number of users who can volunteer to help in the classification: AIDR should allow hundreds of volunteers to work simultaneously.

# User Needs

AIDR has two types of users: **operators**, who start and oversee the process of collection and classification, and **labelers**, who contribute human intelligence through labeled examples.

## User Needs: Operators

Operators of AIDR have the following main needs:

* Maintain (Create, Read, Update or Delete) a set of crisis-related collections
 * Create a collection specifying a set of parameters
 * Monitor the state of a collection
 * Modify the parameters of a collection
 * Stop, delete and/or archive a collection
* Maintain (CRUD) for each crisis-related collection, a set of classifiers
 * Create a classifier specifying a set of tags for it, or copy an existing (suggested) classifier
 * Monitor the classification accuracy
 * Modify the tags of the classifier
 * Remove the classifier from the collection
* Download the resulting classified tweets onto a spreadsheet.
* Visualize at a glance the resulting classified tweets.
* Consume the resulting classified tweets into an application through an API.

Users of AIDR are typically members of emergency-response organizations (including both professional/formal organizations and digital volunteer communities), as well as members of the public.

**Operator personas** (fictional characters describing prototypical users):

* Andres, 40, is a member of a large emergency response organization. He needs to create formal reports detailing the specifics of a crisis situation. In order to create such reports, Andres interviews people on the ground, speaks on the phone with a number of actors, and also uses a number of tools to monitor news and social media.
* Josefina, 30, is a member of a digital volunteer network that creates crisis maps. The crisis maps show a visual representation of the situation in a crisis-stricken area. She works with dozens of other volunteers to create crisis maps that are exposed to the public and to formal response agencies. 
* Maurice, 30, is a journalist and blogger. Maurice uses a series of tools to get the pulse of social media around certain events or developments. This involves finding out about new events, understanding the sides of a crisis, etc.
* Carlos, 40, is a developer of web applications. He creates crisis mash-ups collecting data from different sources. Carlos uses Google Maps, a news crawler, and many data sources to create visually appealing experiences that describe a crisis.

## User Needs: Labelers

Labelers provide volunteer work to classify tweets in AIDR. Their main needs are:

* Finding tasks that they can do, being able to focus on crises that motivate them to be volunteers.
* Learning to do that task, by reading instructions and seeing examples.
* Performing the task (i.e. classify tweets).
* Understanding how well they performed the task.
* Seeing the output of AIDR.
* Understanding the effect of their work in the output of AIDR.

Labelers can be anyone, not necessarily members of volunteer organizations.




