 This code creates a long pooling connection to stream JSONP data 
 from a REDIS DB to a client using a servlet. The connection is
 kept alive until one of the conditions occur:
 		1. The streaming connection duration expires (subscription_duration parameter value)
 		2. The REDIS DB connection times out (REDIS_CALLBACK_TIMEOUT constant)
 		3. Connection loss, e.g., client closes the connection 

 The code accepts i) channel name, ii) fully qualified channel name and, iii) wildcard '*' for
 pattern based subscription.
 
 @author Koushik Sinha
 Last modified: 02/01/2014
 
 Dependencies:  servlets 3+, jedis-2.2.1, gson-2.2.4, commons-pool-1.6, slf4j-1.7.5
 	
 Hints for testing:
 		1. You can increase the test duration by adjusting the SUBSCRIPTION_MAX_DURATION. 
  	2. Tune REDIS_CALLBACK_TIMEOUT, in case the rate of publication is very slow
  	3. Tune the number of threads in ExecutorService
 
 Deployment steps: 
 		1. [Required] Set redisHost and redisPort in code, as per your REDIS setup/location
 		2. [Optional] Tune time-out and other parameters, if necessary
 		3. [Required]Compile and package as WAR file
 		4. [Required] Deploy as WAR file in glassfish 3.1.2
 		5. [Optional] Setup ssh tunneling (e.g. command: ssh tunneling:: ssh -f -L 1978:localhost:6379 scd1.qcri.org -N)
 		6. Issue stream request from client
 
 
 Invocation: host:port/context-root/crisis/stream/channel?crisisCode={crisisCode}&callback={callback}&rate={rate}&duration={duration}  
 ============
 Channel Name based examples:
  1. http://localhost:8080/aidr-output/crisis/stream/channel?crisisCode=clex_20131201&callback=print&rate=10  
  2. http://localhost:8080/aidr-output/crisis/stream/channel?crisisCode=clex_20131201&duration=1h 
  3. http://localhost:8080/aidr-output/crisis/stream/channel?crisisCode=clex_20131201&duration=1h&callback=print
  4. http://localhost:8080/aidr-output/crisis/stream/channel?crisisCode=clex_20131201&duration=1h&rate=15
  5. http://localhost:8080/aidr-output/crisis/stream/channel?crisisCode=clex_20131201&duration=1h&callback=print&rate=10
  
 Wildcard based examples:
  1. http://localhost:8080/aidr-output/crisis/stream/channel?crisisCode=*&callback=print&rate=10 
  2. http://localhost:8080/aidr-output/crisis/stream/channel?crisisCode=*&duration=1h 
  3. http://localhost:8080/aidr-output/crisis/stream/channel?crisisCode=*&duration=1h&callback=print
  4. http://localhost:8080/aidr-output/crisis/stream/channel?crisisCode=*&duration=1h&rate=15
  5. http://localhost:8080/aidr-output/crisis/stream/channel?crisisCode=*&duration=1h&callback=print&rate=10
  
 Fully qualified channel name examples:
  1. http://localhost:8080/aidr-output/crisis/stream/channel?crisisCode=aidr_predict.clex_20131201&callback=print&rate=10 
  2. http://localhost:8080/aidr-output/crisis/stream/channel?crisisCode=aidr_predict.clex_20131201&duration=1h 
  3. http://localhost:8080/aidr-output/crisis/stream/channel?crisisCode=aidr_predict.clex_20131201&duration=1h&callback=print
  4. http://localhost:8080/aidr-output/crisis/stream/channel?crisisCode=aidr_predict.clex_20131201&duration=1h&rate=15
  5. http://localhost:8080/aidr-output/crisis/stream/channel?crisisCode=aidr_predict.clex_20131201&duration=1h&callback=print&rate=10
 
  
  Parameter explanations:
  	1. crisisCode [mandatory]: the REDIS channel to which to subscribe
  	2. subscription_duration [optional]: time for which to subscribe (connection automatically closed after that). 
 		   	The allowed suffixes are: s (for seconds), m (for minutes), h (for hours) and d (for days). The max subscription 
 		   	duration is specified by the hard coded SUBSCRIPTION_MAX_DURATION value (default duration). 
  	3. callback [optional]: name of the callback function for JSONP data
  	4. rate [optional]: an upper bound on the rate at which to send messages to client, expressed as messages/min 
  	   	(a floating point number). If <= 0, then default rate is assumed.
 
