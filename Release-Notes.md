## Release v1.0.0

This version of the AIDR system has been tested with GlassFish Open Source Edition 4.0 on the following operating systems:

1. OS X 10.9.1

2. Ubuntu 13.10

Known issues: 

* `aidr-manager` application, when deployed on Glassfish 4 application server, does not load its default homepage (`index.jsp` page) on startup. In this case, complete path (e.g., http://abc.org/aidr/index.jsp) must be provided to launch the default homepage.

