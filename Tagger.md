Name: aidr-tagger

Code: https://github.com/qcri-social/AIDR/tree/master/aidr-tagger and https://github.com/qcri-social/AIDR/tree/master/aidr-tagger-api

# Overview

The aidr-tagger module reads collected items from a Redis queue, annotates them using an automatic classifier, and writes them to another Redis queue. The input to this module is two-fold: elements to be automatically classified, and elements already classified by a human.

When elements to be automatically classified are received, they are classified using the current classification model. Additionally, some items are sampled from the input and placed into a buffer (known as the _task buffer_) which is a set of items waiting to be classified by a human. The selection of these items is done using an [active learning](https://en.wikipedia.org/wiki/Active_learning) criterion by which the items for which the classifier's confidence is minimum are selected first.

When elements already classified by a human are received, they are considered as _training data_. Whenever a pre-defined number of new training data items are received (e.g. 50 new items), the current classification model is re-trained using all the available training items to date.

# Automatic classification

**Features**: each item is converted to a set of unigrams (words) and bigrams (consecutive two-word sequences). For instance "the house is red" is converted into { "the", "house", "is", "red", "the house", "house is", "is red" }.

**Feature selection**: a feature selection algorithm is ran over the data, keeping the 500 most discriminant features for a given classifier.

**Learning scheme**: the learning scheme is a [random forest](https://en.wikipedia.org/wiki/Random_forest) of decision trees.

# Technologies

For legacy reasons, aidr-tagger is implemented in two different modules, one of them a stand-alone Java application, and the other a Java EE application. Both are needed for the tagger to operate, and the plan is to merge them both into a Java EE application.

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
