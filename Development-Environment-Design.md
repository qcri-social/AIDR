ADIR platform consists various modules, all are responsible for a specific set of tasks. There are two types of interactions 1) RESTFul API, 2) RPC (i.e. Remote EJBs) that two AIDR modules use in order to interact with each other. Almost all of the modules use Java language. Since AIDR is a hosted software, for its development we follow a three-tier development environment. That is development, test/staging, and production. Each tier uses a dedicated server either physical or a virtual machine as shown below.

![](http://i.imgur.com/aOScgjt.png)
