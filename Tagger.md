Name: aidr-tagger

Code: https://github.com/qcri-social/AIDR/tree/master/aidr-tagger and https://github.com/qcri-social/AIDR/tree/master/aidr-tagger-api

# Preliminary: concepts

If you haven't worked with automatic classification of documents, read this [short explanation](http://chato.cl/blog/en/2014/04/how_does_automatic_classification_of_documents_using_machine_learning_works.html) first.

A **nominal attribute** is an attribute that takes a number of values, such as "color".

A **nominal label** is a possible value of a nominal attribute. For instance, if the nominal attribute is "color", possible nominal labels are "red", "green", and "blue".

A **model** is an automatic classifier, associated to a collection and nominal attribute, that has been trained to assign automatically a nominal label to an item, based on a set of human-labelled items.

A **model family** is a set of models for the same collection and nominal attribute. At every moment, only one model is active within a model family. The active model is usually the model that has been trained with the larger number of human-labelled items, or the one that has the greater [http://www.dataschool.io/roc-curves-and-auc-explained/](AUC).

A **feature** is a characteristic of a message (e.g. "it includes the word 'house'"). In aidr-tagger item is converted to a set of features containing all unigrams (words) and bigrams (consecutive two-word sequences). For instance "the house is red" is converted into { "the", "house", "is", "red", "the house", "house is", "is red" }.

A **feature selection method** is a way of selecting features that are of interest. In aidr-tagger a feature selection algorithm is ran over the data, keeping the 500 most discriminant features for a given classifier.

A **learning scheme** is a machine learning algorithm. In aidr-tagger this is a [random forest](https://en.wikipedia.org/wiki/Random_forest) of decision trees.

# Overview of aidr-tagger

The aidr-tagger module has three main functions: to tag (classify) new items, to build and re-build the classification model, and to sample for the human annotators.

The main function of aidr-tagger is to **classify items.** It reads collected items and automatically assign a label to them. This is done using a classification model built using [Weka](http://www.cs.waikato.ac.nz/ml/weka/). 

The second function of aidr-tagger is to **build a classification model.** This is done by receiving human-tagged items and feeding them as _training examples_ to Weka. Every 50 new human-tagged items, a new model is built using all human-tagged items available up to that point in time.

The third function of aidr-tagger is to **sample data for human annotators.** It selects a varied and recent subset of tagged elements that were not trivial to tag (i.e. for which its confidence was low), and places them in the [[task buffer]]. The selection of these items is done using an [active learning](https://en.wikipedia.org/wiki/Active_learning) criterion by which the items for which the classifier's confidence is minimum are selected first.

# 1. Tagging

Tagging is done in two steps: 1a. feature extraction, and 1b. classification. 

As an initial step `AidrFetcherJsonInputProcessor` reads from the Redis queue to which the collector writes collected items, and writes them to Redis queue `REDIS_FOR_EXTRACTION_QUEUE`.

## 1a. Feature extraction

The feature extractor reads from the queue `REDIS_FOR_EXTRACTION_QUEUE` and perform the following processing for each item:

1. Tokenization - this converts a single string into a list of strings, using spaces and other separators.
2. Stopword removal - this removes words that have no meaning on their own, such as determinants and prepositions. Currently, this is a no-operation. In the future, this should look at the language of the tweet (as returned by Twitter, no need to analyze it here), and use a language-specific [stopword list](https://code.google.com/p/stop-words/).
3. Stemming - this converts words to a normalized form, e.g. by removing plural suffixes or -ing endings. Currently, there is a naive stemmer based on 3-4 patterns. In the future, this should use a language-sensitive stemming package such as Snowball. Weka code also includes the [Snowball Stemmer](http://weka.sourceforge.net/doc.dev/weka/core/stemmers/SnowballStemmer.html), so this should be simple.
4. Construction of bigrams - in addition to all words, features include all two-word sequences in the input string.

The feature extractor writes the extracted features into the `REDIS_FOR_CLASSIFICATION_QUEUE`.

## 1b. Classification

The model controller reads from the `REDIS_FOR_CLASSIFICATION_QUEUE` and passing them to the current classification model for that classifier (the active model in the model family).

Additionally, it maintains statistics about how many items have been classified into different categories.

Finally, it writes the output to `REDIS_FOR_OUTPUT_QUEUE`

# 2. 

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
