Production server: http://aidr-prod.qcri.org/AIDRFetchManager/

Staging server: http://10.5.4.10:8080/AIDRFetchManager/index.jsp (must be accessed from the internal "lab" network).

# Commands to execute testers

To run the collector tester in the staging server, use this command:

`sudo mvn -P <profile> test -Dtest=CollectorTesterTest -Dconfig=<configFilePath> -DcollectionTask=<collectionFilePath(properties,xml or json file)> -Dquiet=<true,false(default)> -Dtime=<(in seconds)(default 60)>`