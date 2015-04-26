Name: aidr-common

Code: https://github.com/qcri-social/AIDR/tree/master/aidr-common

# Overview

The aidr-common module contains code that is common to the entire application. The core elements of this common code are:

* Definition of logging mechanisms (package qa.qcri.aidr.common.logging)
* Definition of common exceptions (package qa.qcri.aidr.common.exception)
* Definition of common response formats (package qa.qcri.aidr.common.values)

Additional elements may include utility functions common to more than one module of the application.

# Technologies

This is implemented as a Java EE application, using the following libraries:

* [Jackson](http://jackson.codehaus.org/) 1.9.13 (JSON processor wrapper for backward compatibility)
* FasterXML (JSON processor wrapper) -- (why not GSON? ChaTo)

# Module Dependencies

* None.



