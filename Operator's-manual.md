This document covers the actions of an operator of AIDR, i.e. the person in charge of maintaining data collections and automatic taggers. It assumes you are familiar with is AIDR and what it does.

# 1. Logging in

To enter AIDR, go to http://aidr.qcri.org/ -- you will be asked to log-in using your Twitter credentials.

You need a Twitter account to be able to use AIDR.

# 2. Collecting data 

After entering AIDR, you will be in a page listing your collections. Click on "Create a New Collection".

The "Create New Collection" form appears. The following fields are mandatory:

* **Name**: a name for your collection, typically the name of a crisis/disaster, e.g. "Typhoon Haiyan"
* **Code**: a machine-readable code for your collection, which allows AIDR to uniquely identify your collection. This field can not be edited later. Conventionally we use `Year-Month-Name` for this code.

Next, you have to specify keywords, a geographical boundary, or both* If you specify both, any tweet that either contains the keywords or are inside the geographical boundary will be selected.

For **keywords**, you can specify up to 400 comma-separated keywords in this field.

General rules:
* Not case-sensitive ("bridge" matches "Bridge").
* Whole words match ("bridge" does not match "damagedbridge").

Multi-word queries
* If you include two or more words on a query, all of them must be present in the tweet ("Brooklin bridge" does not match a tweet that does not contain "Brooklin" or does not contain "bridge")
* The words does not need to be consecutive or in that order ("Brooklin bridge" will match "the bridge to Brooklin")

Queries with or without hashtags:
* If you don't include '#', you also match hashtags ("bridge" matches "#bridge")
* If you do include '#', you only match hashtags ("#bridge" does not match "bridge")

For the **geographical boundary**, one or several regions can be specified. Each one should be indicated by a rectangle in coordinate space. The following website can help you determine these coordinates: http://boundingbox.klokantech.com/ -- chose the option "Copy/paste CSV format of a boundingbox".




# Creating an automatic tagger

(TBA)