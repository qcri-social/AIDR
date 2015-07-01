Name: aidr-tagger

Code: https://github.com/qcri-social/AIDR/tree/master/aidr-tagger and https://github.com/qcri-social/AIDR/tree/master/aidr-tagger-api

See [[Tagger Concepts]] if you are unfamiliar with machine learning.

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
