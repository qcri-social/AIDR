# Documentation possibly obsolete (was in a README.txt file inside output)

This code creates a long pooling connection to stream JSONP data from a REDIS DB to a client using a servlet. The connection is kept alive until one of the conditions occur:

1. The streaming connection duration expires (subscription_duration parameter value)
1. The REDIS DB connection times out (REDIS_CALLBACK_TIMEOUT constant)
1. Connection loss, e.g., client closes the connection 

The code accepts i) channel name, ii) fully qualified channel name and, iii) wildcard '*' for pattern based subscription.
 
Dependencies:  jersey 2.5.1, jax-rs 2.0, jedis-2.2.1, gson-2.2.4, commons-pool-1.6, slf4j-1.7.5
 	
Hints for testing:
1. You can increase the test duration by adjusting the SUBSCRIPTION_MAX_DURATION. 
1. Tune REDIS_CALLBACK_TIMEOUT, in case the rate of publication is very slow
1. Tune the number of threads in ExecutorService
 
Deployment steps: 
1. [Required] Set redisHost and redisPort in code, as per your REDIS setup/location
1. [Optional] Tune time-out and other parameters, if necessary
1. [Required]Compile and package as WAR file
1. [Required] Deploy as WAR file in glassfish 3.1.2
1. [Optional] Setup ssh tunneling (e.g. command: ssh tunneling:: ssh -f -L 1978:localhost:6379 scd1.qcri.org -N)
1. Issue stream request from client
 
Invocation: host:port/context-root/crisis/stream/channel?crisisCode={crisisCode}&callback={callback}&rate={rate}&duration={duration}  

Channel Name based example:
* http://localhost:8080/aidr-output/rest/crisis/stream/channel?crisisCode=clex_20131201&duration=1h&callback=print&rate=10
  
Wildcard based example:
* http://localhost:8080/aidr-output/crisis/stream/channel?crisisCode=*&duration=1h&callback=print&rate=10
  
Fully qualified channel name example:
* http://localhost:8080/aidr-output/crisis/stream/channel?crisisCode=aidr_predict.clex_20131201&duration=1h&callback=print&rate=10
  
Parameter explanations:
1. crisisCode [mandatory]: the REDIS channel to which to subscribe
1. subscription_duration [optional]: time for which to subscribe (connection automatically closed after that). The allowed suffixes are: s (for seconds), m (for minutes), h (for hours) and d (for days). The max subscription duration is specified by the hard coded SUBSCRIPTION_MAX_DURATION value (default duration). 
1. callback [optional]: name of the callback function for JSONP data
1. rate [optional]: an upper bound on the rate at which to send messages to client, expressed as messages/min (a floating point number). If <= 0, then default rate is assumed.

