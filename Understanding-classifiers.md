Designing a classifier is an art that requires a lot of practice to master. The following tips will help you through the process. The most important thing to keep it mind is that AIDR doesn't do magic (well, maybe a little :-) but it simply learns from what you give to it as human-labeled examples.

# What is a "classifier"?

A classifier is a set of tags. For instance, "color" can be a classifier, with tags "red," "blue," and "green."

AIDR will run each of the messages through each of the classifiers you have defined, and attach for each classifier one of the tags to the message. Currently AIDR will chose the most likely tag for a message, even for messages that could have more than one tag.

# What are "human-labeled examples"?

Human-labeled examples, also known as "training examples" are the data from which the artificial intelligence (AI) part of AIDR learns. 

AIDR first separates the human-labeled examples by the tags that you have applied to them, and then learns a statistical model for each of the tags. AIDR can learn, for instance, that a specific word (e.g. "blackout") is often indicative of a certain specific tag (e.g. "infrastructure damage"). 

AIDR needs that _each tag has a sufficient number of human-labeled examples_, at least a few hundred.

# What do "Precision", "Recall", and "AUC" mean?

AIDR continuously evaluate its classifiers. In the evaluations you can see in the classifier details page, more is always better. Ideally having Precision, Recall, and AUC of 100% means the classifier is perfect. But things are never perfect.

The evaluation is done per-tag, i.e. there is a precision, recall, and AUC value for each tag. The values can be averaged, but it is good to look at them individually, too.

Precision is a measure of how specific is the classifier for a tag. For instance, if the classifier for the tag "missing people" has 80% precision, it means that 80% of what AIDR labels as "missing people", is actually about missing people.

Recall is a measure of how sensitive is the classifier for a tag. For instance, if the classifier for the tag "missing people" has 80% recall, it means that AIDR is able to find 80% of the messages about missing people and label them as "missing people".

There is a trade-off between precision and recall. You can have a lot of precision at the expense of little recall, and viceversa. AUC (Area Under the Curve) can be understand as a summary of precision and recall.

All you really need to know is 100% AUC means perfection, 50% means very bad, and that acceptable values start at 70%. With 80% and 90% you really start to feel the classifier is doing a good job. If your classifier doesn't reach that number for a tag, you need more training examples for that tag.

# What does "confidence" means?

AIDR uses statistical models, which involve probabilities: they are never black or white.

When AIDR attaches a tag to an item, it does it with a certain **confidence**, which is how certain AIDR is that the item should have that tag. A confidence of 1.0 often means that AIDR has seen the exact same message in the past on its human-labeled examples. A low confidence means that AIDR isn't sure if the message belongs to a category or not.

# How many classifiers should I use?

Start with one classifier, two maximum, and wait until you train them to reach a high value of AUC. After you have trained them, you can add more classifiers. 

Don't add all the classifiers at the same time: that generates unnecessary work for AIDR, who has to move data back and forth from AIDR to MicroMappers and viceversa (as of May 2015; this may change in the future). Add the classifiers when you are ready to train them.

# How many tags should I use in each classifier?

As a rule of thumb, you should have 3 to 10 tags in each classifier. If you have only two, you are most likely missing something (there are exceptions). If you have more than 10, then you are most likely including tags for which messages will be rarely found, meaning that those tags will never reach a good value of AUC.

It is best to experiment a little. Create a classifier with some tags, then tag some items. Once you are done, look at how many items you placed in each tag. Are there empty tags? Perhaps you can merge some tags into more general concepts. Are there tags that have all the messages? Perhaps you can divide them into more specific concepts.