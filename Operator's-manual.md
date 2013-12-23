This document covers the actions of an operator of AIDR, i.e. the person in charge of maintaining data collections and automatic taggers. It assumes you are familiar with is AIDR and what it does.

# 1. Logging in

To enter AIDR, go to http://aidr.qcri.org/ -- you will be asked to log-in using your Twitter credentials.

You need a Twitter account to be able to use AIDR.

# 2. Collecting data 

After entering AIDR, you will be in a page listing your collections. Click on "Create a New Collection".

# 2.1. Creating a collection

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

# 2.2. Starting and stopping a collection

Once you create a collection, you need to start it. Do this by click on the "Start" button next to the collection. You can monitor the progress of a collection by clicking on its name on the "My Collections" page and looking at the number of downloaded items and the latest downloaded item. You can click on the "Refresh" button on the upper-right corner of that page to view up-to-date information.

To stop a collection, click on the "Stop" button next to it.

Note that currently only one collection per user is allowed. If you start a new collection, you need to stop the currently-running ones.

# 3. Automatically tagging data

# 3.1. Starting the automatic tagger

From the collection details page, click on "Enable Tagger" or "Go To Tagger" button if the tagger is already enabled. A tagger is a set of classifiers. Each classifier corresponds to one way of sorting out tweets into categories.

To start the automatic tagger, you need to add a classifier to it. Click on the "Add a new classifier" button and you will see a list of standard classifiers that are useful in disaster scenarios. You can also create your own one, by specifying the different labels in that classifier.

Click on the "Add" button next to a classifier to add it. We recommend you to add only one classifier at the beginning -- it will be easier to handle for you.

# 3.2. Collecting training examples

Automatic taggers require training examples, which are items that have been classified manually by humans. Depending on the specific classifier, in particular on the number of different labels or categories contained on it, the number of training examples will vary. Typically, at least a few hundred training examples are needed before the classifier starts to work accurately.

To collect training examples, copy-paste the "Public link for volunteers" and share it with your digital volunteers. They will be able to provide training labels for all the classifiers of your tagger from that page.

If you do not have volunteers to tag data, you can enter examples yourself. For this, click on "Manage Training Examples" and then on "Add Training Examples". 

# 3.3. Monitoring the automatic tagger

The automatic tagger will not run until a minimum number of training examples (50) is provided. At that point, it will evaluate its own performance and report a number, the **AUC**, which is a standard measure of how well it is performance.

The more examples you provide, the better the classification accuracy will be. An AUC of 0.5 or lower indicates a very bad classifier that is not doing anything better than random guessing -- this is expected when you have few training examples, but may also signal errors or inconsistencies in your examples. An AUC of 0.8 or higher indicates reasonable classification accuracy and 0.9 is sometimes attainable with a few thousands examples.

