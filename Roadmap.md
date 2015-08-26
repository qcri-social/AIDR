Release numbering: [major.minor](https://www.gnu.org/prep/standards/html_node/Releases.html#index-version-numbers_002c-for-releases), optionally major.minor.revision for smaller changes. Release numbers are not related to compatibility.

# Release 1.5: streamlined installer and documentation

**Original target: mid-May. Current target: end-June. Actual date: TBA**

The objective of this release is to streamline the installation of AIDR, and to improve the quality of the AIDR documentation to enable better collaboration among development partners.

Metacube side:
* installation of AIDR is streamlined

QCRI side:
* configuration files are reviewed
* per-module and basic per-package documentation is available
* urgent bugfixes

# Release 1.6: Bugfixes, logging and MicroMappers integration

**Target: early July**

The objective of this release is to fix bugs, complete logging from all aspects including adopting uniform logging standards,fix existing logging issues, logging where applicable, and integration with MicroMappers.

Metacube side:
* Bugfixes
* Logging changes
* MicroMappers integration

QCRI side:
* Urgent bugfixes

# Release 2.0: view/download complete

**Target: mid-July.**

The objective of this release is to ensure users are able to view and download their collections correctly. This means completing all features necessary for people to download their collections, even the ones that are very large, and even the ones where there is no tagger.

Metacube side:
* view/download complete

QCRI side:
* view/download complete
* UI enhancements

# Release 2.1: robustness of collector, output, persister, and tagger

**Original target: end-May. Current target: end-Aug. Actual date: TBA**

The objective of this release is to make the collector, output, persister, and tagger modules more robust. This includes introducing a basic level of testing and fixing any bugs found during the testing.

Metacube side:
* dbmanager has unit tests which cover CRUD of all entities in the successful cases, to avoid regressions.
* aidr-collector, aidr-output, aidr-persister, and aidr-tagger have unit tests which cover all the essential APIs at a minimum in the successful cases, to avoid regressions.
* taskmanager has unit tests which cover CRUD of all entities in the successful cases, to avoid regressions.
* aidr-collector additionally has an automated testing suite covering start/status/stop collection.
* aidr-tagger additionally has an automated testing suite covering the creation of a classifier and the application of it to tag new items.
* aidr-output has an automated testing suite covering receiving elements and making them available through the get latest
* aidr-persister has an automated testing suite covering receiving elements and saving them to disk.

QCRI side:
* urgent bugfixes
* crowdsourcing should be enabled manually, not automatically.
* fix bugs found during testing
* progress on back-end of analysis module
* small UI enhancements

# Release 3.0: complete AIDR analytics back end

**Original target: end-Oct.**

The objective of this release is to complete the back-end of aidr-analytics and start collecting data for it.

QCRI side:
* fixing bugs found during testing.
* AIDR analytics back-end

Epic: analysis-mvp, https://www.pivotaltracker.com/epic/show/1372904

# Release 3.1: complete AIDR analytics front end

**Original target: end-Aug. Current target: mid-Dec.**

The objective of this release is to produce the first version of aidr-analytics module, which is a dashboard showing the percentage of tweets in different categories and some graphs.

Epic: analysis-mvp, https://www.pivotaltracker.com/epic/show/1372904

Metacube side:
* analytics front-end
* analytics unit testing
* analytics module testing 

QCRI side:
* analysis back-end fixes if needed

# Release 3.2: tagger as a Java EE application, trainer more robust

**Original target: mid-July. Current target: end-Dec**

The objective of this release is to make the tagger and trainer modules more robust. This includes transforming aidr-tagger into a Java EE application, instead of a stand-alone application. It also includes implementing a tester for the trainer.

This may mean one of the following, to be decided later: (i) merging the functionality of aidr-tagger into aidr-tagger-api, (ii) keeping both separate, or (ii) deprecating aidr-tagger-api, so that aidr-manager speaks directly to dbmanager.


Metacube side:
* aidr-tagger is converted to a Java EE application
* aidr-trainer have unit tests which cover all the essential APIs at a minimum in the successful cases, to avoid regressions.
* aidr-trainer additionally has an automated testing suite covering the labelling of examples.

QCRI side:
* fixing bugs found during testing.
* back-end fixes to view/download as needed
* minor UI enhancements

# Release 5.0: UI improvements

**Original target: mid-Sep. Current target: end-Nov.**

The objective of this release is to implement usability fixes from our user testing and feedback. This release might be accelerated if the usability fixes are done in parallel to the tasks needed for the previous releases.

# Release 5.0: Cold-start collections

**Original target: end-Sep. Current target: Jan 2016**

The objective create a collection without having to tag data, by re-using existing data.

Story: https://www.pivotaltracker.com/story/show/65556676