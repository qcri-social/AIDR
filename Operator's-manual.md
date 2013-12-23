This document covers the actions of an operator of AIDR, i.e. the person in charge of maintaining data collections and automatic taggers. It assumes you are familiar with is AIDR and what it does.

# Logging in

To enter AIDR, go to http://aidr.qcri.org/ -- you will be asked to log-in using your Twitter credentials.

You need a Twitter account to be able to use AIDR.

# Creating a collection

After entering AIDR, you will be in a page listing your collections. Click on "Create a New Collection".

The "Create New Collection" form appears. The following fields are mandatory:

* Name: a name for your collection, typically the name of a crisis/disaster, e.g. "Typhoon Haiyan"
* Code: a machine-readable code for your collection, which allows AIDR to uniquely identify your collection. This field can not be edited later. Conventionally we use `Year-Month-Name` for this code.

The following fields are optional:

* Follow specific users: a list of numeric user-IDs of users in Twitter to follow.

Next, you have to specify either keywords or a geographical region.

## Specifying keywords

General rules:
* Not case-sensitive ("bridge" matches "Bridge").
* Whole words match ("bridge" does not match "damagedbridge").

Multi-word queries
* If you include two or more words on a query, all of them must be present in the tweet ("Brooklin bridge" does not match a tweet that does not contain "Brooklin" or does not contain "bridge")
* The words does not need to be consecutive or in that order ("Brooklin bridge" will match "the bridge to Brooklin")

Queries with or without hashtags:
* If you don't include '#', you also match hashtags ("bridge" matches "#bridge")
* If you do include '#', you only match hashtags ("#bridge" does not match "bridge")

# Creating an automatic tagger

(TBA)