Designing a classifier is an art that requires a lot of practice to master. The following tips will help you through the process.

# What is a "classifier"?

A classifier is a set of tags. For instance, "color" can be a classifier, with tags "red," "blue," and "green."

AIDR will run each of the items through a classifier, and attach one of the tags to the item. Currently AIDR will chose the most likely tag for an item, even for items that have two or more tags.

# Why do you need human-labeled examples?

AIDR is an automated system that learns from human-labeled examples, which are also known as "training data." It looks at words and sequences of words in the items, and learns a statistical model for each of the tags. For instance, it may learn that a certain word combination is often indicative of a certain tag. 

Each human-labeled example is considered as evidence for learning, and AIDR learns from the set of all examples it receives. AIDR will need a minimum number of human-labeled examples (ideally a few hundred) for each of the tags.

# What does "AUC", "Precision" and "Recall" mean?

In the classifier details page, you can see different ways in which you classifier has been evaluated.

# What does "confidence" means?

When AIDR attaches a tag to an item, it does it with a certain **confidence**, which is how certain AIDR is that the item should have that tag. A confidence of 1.0 often means that AIDR has seen the exact same item in the past on its human-labeled examples.

# How many classifiers?

