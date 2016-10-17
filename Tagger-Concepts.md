If you haven't worked with automatic classification of documents, read this [short explanation](http://chato.cl/blog/en/2014/04/how_does_automatic_classification_of_documents_using_machine_learning_works.html) first.

A **nominal attribute** is an attribute that takes a number of values, such as "color".

A **nominal label** is a possible value of a nominal attribute. For instance, if the nominal attribute is "color", possible nominal labels are "red", "green", and "blue".

A **model** is an automatic classifier, associated to a collection and nominal attribute, that has been trained to assign automatically a nominal label to an item, based on a set of human-labelled items.

A **model family** is a set of models for the same collection and nominal attribute. At every moment, only one model is active within a model family. The active model is usually the model that has been trained with the larger number of human-labelled items, or the one that has the greater [http://www.dataschool.io/roc-curves-and-auc-explained/](AUC).

A **feature** is a characteristic of a message (e.g. "it includes the word 'house'"). In aidr-tagger item is converted to a set of features containing all unigrams (words) and bigrams (consecutive two-word sequences). For instance "the house is red" is converted into { "the", "house", "is", "red", "the house", "house is", "is red" }.

A **feature selection method** is a way of selecting features that are of interest. In aidr-tagger a feature selection algorithm is ran over the data, keeping the 500 most discriminant features for a given classifier.

A **learning scheme** is a machine learning algorithm. In aidr-tagger this is a [random forest](https://en.wikipedia.org/wiki/Random_forest) of decision trees.
