(TO BE COMPLETED)

Release numbering: [major.minor](https://www.gnu.org/prep/standards/html_node/Releases.html#index-version-numbers_002c-for-releases), optionally major.minor.revision for smaller changes. Release numbers are not related to compatibility.

# Release 2.0: robustness of collector, output, and persister

The objective of this release is to make the collector, output, and persister modules more robust. This includes introducing a basic level of testing and fixing any bugs found during the testing.

* dbmanager has unit tests which cover CRUD of all entities in the successful cases, to avoid regressions.
* aidr-collector, aidr-output, and aidr-persister have unit tests which cover all the essential APIs at a minimum in the successful cases, to avoid regressions.
* aidr-collector additionally has an automated testing suite covering start/status/stop collection.
* aidr-output has an automated testing suite covering receiving elements and making them available through the get latest
* aidr-persister has an automated testing suite covering receiving elements and saving them to disk.

# Release 3.0: robustness of tagger, trainer

The objective of this release is to make the tagger and trainer modules more robust. This includes introducing a basic level of testing and fixing any bugs found during the testing.

* taskmanager has unit tests which cover CRUD of all entities in the successful cases, to avoid regressions.
* aidr-tagger and aidr-trainer have unit tests which cover all the essential APIs at a minimum in the successful cases, to avoid regressions.
* aidr-trainer additionally has an automated testing suite covering the labelling of examples.
* aidr-tagger additionally has an automated testing suite covering the creation of a classifier and the application of it to tag new items.

# Release 4.0: integrating aidr-tagger into aidr-tagger-api

The objective of this release is to transform aidr-tagger into a Java EE application, instead of a stand-alone application. This will mean merging the functionality of aidr-tagger into aidr-tagger-api.

# Release 5.0: view/download complete

The objective of this release is to ensure users are able to view and download their collections correctly. This means completing all features necessary for people to download their collections, even the ones where there is no tagger.

# Release 6.0: basic analytics

The objective of this release is to produce the first version of aidr-analytics module, which is a dashboard showing the percentage of tweets in different categories and some graphs.

Epic: analysis-mvp, https://www.pivotaltracker.com/epic/show/1372904

# Release 7.0: UI improvements

The objective of this release is to implement usability fixes from our user testing and feedback. This release might be accelerated if the usability fixes are done in parallel to the tasks needed for the previous releases.

# Release 8.0: Cold-start collections

The objective create a collection without having to tag data, by re-using existing data.

Story: https://www.pivotaltracker.com/story/show/65556676