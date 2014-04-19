### Introduction

Social media platforms are increasingly used to communicate crisis information when major disasters strike. Hence, the rise of Big (Crisis) Data. Humanitarian organizations, digital humanitarians and disaster-affected communities know that some of this user-generated content can increase situational awareness. To do this, relevant and actionable content must be identified in near real-time so that can be triangulated with other sources. By doing this, informed decisions can be made more quickly. However, finding potentially life-saving information in this growing stack of Big Crisis Data is like looking for the proverbial needle in a giant haystack. This is why Patrick Meier and his team at QCRI developed AIDR.

AIDR: Artificial Intelligence for Disaster Response

The free and open source Artificial Intelligence for Disaster Response (AIDR) platform leverages machine learning to automatically identify informative content on Twitter during disasters. Unlike the vast majority of related platforms that currently exists, AIDR goes beyond simple keyword search to filter for informative content. Why? This is because recent research shows that keyword searches can miss over 50% of relevant content posted on Twitter. This is very far from optimal for information collection for disaster response. Furthermore, tweets captured via keyword search may not be relevant since words can have multiple meanings depending on context. Finally, keywords are restricted to one language only. Machine learning overcomes all these limitations, which is why AIDR has been developed.

So how does AIDR work? There are three components of AIDR: the Collector, Trainer and Tagger. The Collector simply allows you to collect and save a collection of tweets posted during a disaster. You can download these tweets for analysis at any time and also use them to create an automated filter using machine learning, which is where the Trainer and Tagger come in. The Trainer allows one or more users to train the AIDR platform to automatically tag tweets in a given collection according to topics  of interest that are determined by the user. Topics, for example, could include those that refer to “Needs,” “Infrastructure Damage,” and “Rumors.” 

Collector

The Collector simply allows you to collect and save a collection of tweets posted during a disaster. You can download these tweets for analysis at any time and also use them to create an automated filter using machine learning, which is where the Trainer and Tagger come in. The Collector helps you do this by filtering tweets using keywords and/or hashtags such as "hurricane" and "#Sandy," for example. This is just like a regular Search on Twitter.com, which means most of the resulting tweets will not be relevant to disaster response or to the specific information needs of humanitarian organizations. This is where the Trainer comes in.

Trainer

While the Collector is a word-filter, the Trainer is a topic-filter. The more tweets are tagged, the more accurate the automatic classifier will be. When enough tweets have been classified (20 minimum), the Tagger automatically begins to tag new tweets by topic of interest.

AIDR creates two types of trainers for a collection automatically: 1) Internal Trainer, which is used by collection owners to tag tweets without using volunteers. 2) PyBossa trainer, which is used to crowdsource tweets to involve volunteers or third parties to help tag tweets. 

With the Trainer, users classify collected tweets by topics of interest, such as "Infrastructure Damage," and "Urgent needs," for example. Any number of users can participate in classifying these tweets. Once AIDR has created  a Trainer, the user can classify the tweets herself, invite your organization to help classify, utilize the crowd to help classify the tweets, or all of the above. To do this, the user simply shares a link to the users training page with whoever you like. If you choose to crowdsource the classification of tweets, AIDR includes a built-in quality control mechanism to ensure that the crowdsourced classification is accurate. 

Tagger

The Tagger component of AIDR automatically applies the topics of interest to tweets collected in real-time using the Collector. This is where the machine learning kicks in. The Tagger uses the human-classified tweets to learn what kinds of tweets the user is interested in. When enough tweets have been classified (20 minimum), the Tagger automatically begins to tag new tweets by topic of interest. The more tweets a user classifies, the more accurate the Tagger will be. Note that each automatically tagged tweet includes an accuracy score—i.e., the probability that the tweet was correctly tagged by the automatic Tagger.
The Tagger displays a list of automatically tagged tweets updated in real-time. The user can filter this list by topic and/or accuracy score—display all tweets tagged as “Needs” with an accuracy of 90% or more, for example. You can also download the tagged tweets for further analysis. In addition, you can share the data link of her Tagger with developers so they can import the tagged tweets directly into to their own platforms, e.g., MicroMappers, Ushahidi, CrisisTracker, etc. (Note that AIDR already powers CrisisTracker by automating the classification of tweets). In addition, the user can share a display link with individuals who wish to embed the live feed into their websites, blogs, etc.

### Instructions

This manual covers the working procedures for an operator of AIDR using Twitter for collection. The following section contains step-by-step instructions for using the platform.

# 1. Logging in
![Figure 1. AIDR Logging](https://dl.dropboxusercontent.com/u/7614003/aidr-login.png)
To enter AIDR, go to http://aidr.qcri.org/ -- you will be asked to log-in using your Twitter credentials [Figure 1]. 
You need a Twitter account to be able to use AIDR.

Once you have signed in, you will receive a prompt asking you to authorize AIDR to use your Twitter account [Figure 2]. Click “Authorize app” to proceed.

Every time you log into AIDR you will have to authorize the platform to use your account. 


# 2. Collecting data 

After authorizing AIDR to access you account, you will be redirected back to the application. This will bring you to your “My Collections” page. To start, click on "Create a New Collection" [Figure 3].


# 2.1. Creating a collection

The "Create New Collection" form appears [Figure 4].

The following fields are mandatory:

Name: a name for your collection, typically the name of a crisis/disaster, e.g. "Typhoon Haiyan"

Short name: Collection code consists of alpha-numeric shortcode name to a collection. Space are not allowed. For example, Sandy2012. This is a machine-readable code for your collection that allows AIDR to uniquely identify your collection. A suggested code will appear in the field which you can use or change to suit your requirements. Conventionally a code in the format Year-Month-Name is used for this code. It is important to remember that this field can not be edited later.

Keywords: comma-separated keywords to filter the Twitter stream. You can specify up to 400 keywords in this field.

General rules:

Not case-sensitive ("bridge" matches "Bridge")
Whole words match ("bridge" does not match "damaged bridge")

Multi-word queries:

If you include two or more words in a query, all of them must be present in the tweet ("Brooklyn bridge" does not match a tweet that does not contain "Brooklyn" or does not contain "bridge")
The words does not need to be consecutive or in that order ("Brooklyn bridge" will match "the bridge to Brooklyn")

Queries with or without hashtags:

If you don't include “#”, you also match hashtags ("bridge" matches "#bridge")
If you do include “#”, you only match hashtags ("#bridge" does not match "bridge")

Language: filters results only by the specified languages. You can chose more than one language. Although not mandatory, specifying a language or languages is recommended if you intend to use the automatic tagger.

Automatically stop after: Collection duration specifies the length in days after which the collection will be automatically stopped. 

There are also some optional fields. They are:

Geographical regions: comma-separated pairs of latitude or longitude. If you specify a geographical region, all messages from within the region will be collected. This is independent of whether or not they contain the keywords.

Follow specific users: comma-separated list of twitter user IDs to be followed.

A geographical boundary, or both a filter based on keywords and geographical coordinates, includes tweets that either match the keywords or fall within the geographical bounding box. When setting a geographical boundary, one or more regions can be specified. Each region is determined establishing a rectangle over the determined space on the map. To do this, click the link (http://boundingbox.klokantech.com/) below the geographical regions drop-down. On the BoundingBox page, Click on the area of the map you want to cover and expand the box that appears. After you have the box drawn over the region you wish to collect tweets from, click the drop-down on the bottom left-hand corner of the page under “Copy & Paste” [Figure 5]. This will convert the coordinates to a CSV format. Once in a CSV format, paste the link into the geographical regions section of the form.

# 2.2. Starting and stopping a collection

Once you create a collection, you need to start it. Do this by click on the "Start" button next to the collection [Figure 6]. You can monitor the progress of a collection by clicking on its name on the "My Collections" page and looking at the number of downloaded items and the latest downloaded item.

You can click on the "Refresh" button on the upper-right corner of that page to view up-to-date information. To stop a collection, click on the "Stop" button next to it. Note that you can currently only run one collection at a time. If you start a new collection, you need to stop the currently-running ones.

# 3. Automatically tagging data

Automatic taggers require training examples, which are items that have been classified manually by humans. Depending on the specific classifier, in particular on the number of different labels or categories contained on it, the number of training examples will vary. Typically, at least a few hundred training examples are needed before the classifier starts to work accurately.

# 3.1. Starting the automatic tagger

From the collection details page, click on "Enable Tagger" or "Go To Tagger" button if the tagger is already enabled [Figure 7]. A tagger is a set of classifiers. Each classifier corresponds to one way of sorting out tweets into user-defined categories. Before enabling automatic tagging, you must chose a crisis type to define your collection [Figure 8]. 

To start the tagger, you must then add a classifier to it. Click on the "Add a new classifier" button under the collection you wish to start. A list of standard classifiers that are useful in disaster scenarios will appear [Figure 9]. You can also create your own one, by specifying the different labels in that classifier [Figure 10]. You will chose the classier based on what types of tweets you are interested in. Click on the "Add" button next to a classifier to add it. We recommend you to add only one classifier at the beginning -- it will be easier for you to handle.

# 3.2. Collecting training examples

At this stage, you can copy-paste the "Public link for volunteers" and share it with your organization or volunteers. All users with the link will then be able to apply training labels for your tagger. If you do not have others to tag the tweets, then proceed with the training examples yourself. If your collection has not acquired enough tweets for the training, try again in a few minutes. 

To complete the training data, click on "Go to human-tagged items" [Figure 11]. On the next screen, click "Tag more items" [Figure 12].

Once you click to tag more items, you will begin to label training examples. To complete the training data, you will be asked to indicate the proper label for tweets gathered as part of your collection. The labels are determined by the classifier you chose for your collection based on what type of information you are looking to collect from the tweets [Figure 13]. Again, these training examples provide the basis for your automatic tagger.

# 3.3. Monitoring the automatic tagger

The automatic tagger will not run until a minimum number of training examples (20) is provided. At that point, it will evaluate its own performance and report a number, the **AUC** (Area under curve), which is a standard measure of how well it is performance.

The more examples you provide, the better the classification accuracy will be. An AUC of 0.5 or lower indicates a very bad classifier that is not doing anything better than random guessing -- this is expected when you have few training examples, but may also signal errors or inconsistencies in your examples. An AUC of 0.8 or higher indicates reasonable classification accuracy and 0.9 is sometimes attainable with a few thousands examples.

# 3.4. Downloading collected data

To download/export the data gathered click the Download/Export button

Choose which format to download/export. Click relevant button

Note that Twitter’s Terms of Service allow you to download up to 100,000 tweets per day. But you can download as many tweet IDs as you’d like. Tweet IDs are simply unique identifiers for every posted tweet. You can then convert  tweet IDs back into their original tweets. This is permissible according to Twitter’s Terms of Service
