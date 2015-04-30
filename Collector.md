Name: aidr-collector

Code: https://github.com/qcri-social/AIDR/tree/master/aidr-collector

# Overview

The aidr-collector module is responsible for acquiring data for the rest of the application. The basic concept is that of a _collection_, also known as a _crisis_ in some parts of the code, which defines an event or situation for which messages should be collected.

After acquiring a message, aidr-collector does small amounts of in-memory processing, including some degree of filtering.

The output of aidr-collector is published to Redis through channels. Every collection starts its dedicated sub-channel under the aidr-collector channel, which is `FetcherChannel`.


There are two main types of collector: Twitter collector and SMS collector.

## Twitter collector

The Twitter collector uses Twitter's filtering/streaming API to collect tweets based on keywords or geographical locations. It connects to the filtering/streaming API using the Twitter4j library. Then, it receives the messages from Twitter and may do a limited amount of post-processing. 

Currently the post-processing done by the collector has two possible elements:

* Geo-strict filtering, which is an optional post-filtering in which only locations having GPS coordinates within a pre-defined area are kept (otherwise Twitter also does some sloppy matching in which tweets without GPS coordinates can be selected)
* Conjunction (AND) of keywords and geographical locations, which is necessary because by default when given keywords and geographical locations, Twitter returns tweets that have the keywords OR are in the geographical location, which is never what our users want.

## SMS collector

The SMS collector passively listens for tweets that are posted by an external service using an API.

An operator using the SMS collector would have a set-up in which their machine that receives the SMS, automatically pushes those SMS to AIDR.

# Technologies

* Jersey RESTful Web Services framework 2+ (for JAX-RX 2.0)
* JEDIS 2.5.2 (REDIS API library)
* Twitter4j 4.0+ (twitter API library)
* Google GSON 2.2.4 (JSON processor)
* Jackson 1.9.13 (JSON processor) -- (redundant? --ChaTo)
* SuperCSV (CSV library) -- (why? --ChaTo)

# Module Dependencies

* [aidr-common](Common)

