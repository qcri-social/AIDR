Name: aidr-tagger

Code: https://github.com/qcri-social/AIDR/tree/master/aidr-tagger and https://github.com/qcri-social/AIDR/tree/master/aidr-tagger-api

# Overview

The aidr-tagger module has three main functions: to tag (classify) new items, to build and re-build the classification model, and to sample for the human annotators.

The main function of aidr-tagger is to **classify items.** It reads collected items and automatically assign a label to them. This is done using a classification model built using [Weka](http://www.cs.waikato.ac.nz/ml/weka/). 

The second function of aidr-tagger is to **build a classification model.** This is done by receiving human-tagged items and feeding them as _training examples_ to Weka. Every 50 new human-tagged items, a new model is built using all human-tagged items available up to that point in time.

The third function of aidr-tagger is to *sample data for human annotators.* It selects a varied and recent subset of tagged elements that were not trivial to tag (i.e. for which its confidence was low), and places them in the [[task buffer]]. The selection of these items is done using an [active learning](https://en.wikipedia.org/wiki/Active_learning) criterion by which the items for which the classifier's confidence is minimum are selected first.

# Concepts

A **nominal attribute** is an attribute that takes a number of values, such as "color".

A **nominal label** is a possible value of a nominal attribute. For instance, if the nominal attribute is "color", possible nominal labels are "red", "green", and "blue".

A **model** is an automatic classifier, associated to a collection and nominal attribute, that has been trained to assign automatically a nominal label to an item, based on a set of human-labelled items.

A **model family** is a set of models for the same collection and nominal attribute. At every moment, only one model is active within a model family. The active model is usually the model that has been trained with the larger number of human-labelled items, or the one that has the greater [http://www.dataschool.io/roc-curves-and-auc-explained/](AUC).

# Automatic classification

Human-labelled items are converted to a set of features, which are then filtered by a feature selection method, and given to a learning scheme to create a model.

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
