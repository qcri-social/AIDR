Release numbering: [major.minor](https://www.gnu.org/prep/standards/html_node/Releases.html#index-version-numbers_002c-for-releases), optionally major.minor.revision for smaller changes. Release numbers are not related to compatibility.

# Release 2.0: minimal level of test coverage for collector, output, and persister

* dbmanager and taskmanager have unit tests which 
* aidr-collector, aidr-output, and aidr-persister have unit tests which cover all the essential APIs at a minimum in the successful cases, to avoid regressions.
* aidr-collector additionally has an automated testing suite covering start/status/stop collection.
* aidr-output has an automated testing suite covering receiving elements and making them available through the get latest
* aidr-persister has an automated testing suite covering receiving elements and saving them to disk.

# Release 3.0: minimal level of test coverage for tagger and trainer

This rele

# Release 4.0
 
Fixes to download/view data: currently users are not completely able to get their own data, due to some missing features

# Release 5.0

Dashboard: show % of tweets in different categories and some graphs, important for the AIRS project in Qatar

# Release 6.0

UI improvements: usability fixes from our user testing and feedback

# Release 7.0

Cold-start collections: create a collection without having to tag data, by re-using existing data.