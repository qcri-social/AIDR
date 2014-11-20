Before running persister unit-tests, ReditTweetInjector.java is needed from aidr-output (src/test/java, package: qa.qcri.aidr.getdata)
to inject the tweets using Radis

** in InjectorConfig.java, I have changed the value of "useSingleTweet" to false, to read the tweets from a sample file which stored locally in local machine, I used collectioCode= 2014-05-emsc_landslides_2014. you can change the collectionCode in unit-test classes.

steps for running the test:
1- run test class (Persister4CollectionAPITest) as JUnit test
2- run ReditTweetInjector.java as Java Application

then Persister4CollectionAPITest should listen to Redis to create the appropriate files
