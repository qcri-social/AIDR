
 This code makes a HTTP request to get the last 'n' JSONP data 
 from a REDIS DB to a client using a servlet. After sending the data, it 
 closes the connection. The data from the REDIS channels is buffered in 
 the background by a continuous running buffering system - started at servlet startup
 The jsonp messages are returned in an ArrayList data structure, in reverse chronological order.
 
 The code accepts i) channel name or, ii) fully qualified channel name. However, wildcard '*' for
 pattern based subscription are NOT allowed.
 
 @author Koushik Sinha
 Last modified: 14/01/2014
 
 Dependencies:  servlets 3+, jedis-2.2.1, gson-2.2.4, commons-pool-1.6, slf4j-1.7.5, jax-rs 2.0
 	
 Hints for testing:
 	1. Tune the socket timeout parameter in JedisPool(...) call if connecting over a slow network
  	2. Tune REDIS_CALLBACK_TIMEOUT, in case the rate of publication is very slow
  	3. Tune the number of threads in ExecutorService 	 
 
 Deployment steps: 
 	1. [Required] Set redisHost and redisPort in code, as per your REDIS setup/location
 	2. [Optional] Tune time-out and other parameters, if necessary
 	3. [Required]Compile and package as WAR file
 	4. [Required] Deploy as WAR file in glassfish 3.1.2
 	5. [Optional] Setup ssh tunneling (e.g. command: ssh tunneling:: ssh -f -L 1978:localhost:6379 scd1.qcri.org -N)
 	6. Issue getLast request from client
 
 
 Invocation:	host:port/context-root/rest/crisis/fetch/channel/{crisisCode}?callback={callback}&count={count} 
 ============	
  Channel name based examples: 
    1. http://localhost:8080/aidr-output/rest/crisis/fetch/channel/clex_20131201?count=50
    2. http://localhost:8080/aidr-output/rest/crisis/fetch/channel/clex_20131201?callback=JSONP
    3. http://localhost:8080/aidr-output/rest/crisis/fetch/channel/clex_20131201?callback=JSONP&count=50
    
  Fully qualified channel name based examples: 
    1. http://localhost:8080/aidr-output/rest/crisis/fetch/channel/aidr_predict.clex_20131201?count=50
    2. http://localhost:8080/aidr-output/rest/crisis/fetch/channel/aidr_predict.clex_20131201?callback=func
    3. http://localhost:8080/aidr-output/rest/crisis/fetch/channel/aidr_predict.clex_20131201?callback=func&count=50
   
  Apart from the above valid paths one can use:
    1. http://localhost:8080/aidr-output/rest/crisis/fetch/channels/list     => returns list of active channels
    2. http://localhost:8080/aidr-output/rest/crisis/fetch/channels/latest	=> returns the latest tweet data from  across all channels
 
  
  Parameter explanations:
  	1. crisisCode [mandatory]: the REDIS channel to which to subscribe
  	2. callback [optional]: name of the callback function for JSONP data
  	3. count [optional]: the specified number of messages that have been buffered by the service. If unspecified
  		or <= 0 or larger than the MAX_MESSAGES_COUNT, the default number of messages are returned  

