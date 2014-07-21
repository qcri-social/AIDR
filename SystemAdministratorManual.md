See also [Installation Instructions](https://github.com/qcri-social/AIDR/wiki/Installation-instructions)

# 1. Starting AIDR

Successful deployment of all aidr.* WAR files (e.g., aidr_collector.war, aidr_tagger_api.war, etc.) to application server (e.g., GlassFish) means AIDR platform is up and running. For example, in case of GlassFish server, use GlassFish administration console (i.e., http://localhost:4848/), go to the application tab select application that you want to deploy and press the deploy button. 

# 2. Stopping AIDR

One can simply un-deploy one of the AIDR applications from application server using standard un-deploy operation. For example, in case of GlassFish server, use GlassFish administration console (i.e., http://localhost:4848/), go to application tab select application that you want to un-deploy and press the un-deploy button.

# 3. Re-starting AIDR

To re-starting an application perform the above two steps by stopping the application first and then starting it again.

One can re-start the application server to re-start all applications by using re-start command of the application server. For example, in case of GlassFish server do:

`./bin/asadmin restart-domain`

