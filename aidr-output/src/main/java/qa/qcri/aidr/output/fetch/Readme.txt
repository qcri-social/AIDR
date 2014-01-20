 This code creates a long pooling connection to get 'n' JSONP data  
 from a REDIS DB to a client using a servlet. After sending the data, it 
 closes the connection. The difference between /getAll and /fetch is that
 while /getAll get historical data, /fetch starts buffering from the time
 the request is received by the servlet and destroyed after the response 
 is sent. /getAll on the other hand, continues to buffer internally even
 after the client connection is closed.
 
 The code accepts i) channel name, ii) fully qualified channel name and, iii) wildcard '*' for
 pattern based subscription.
 
 @author Koushik Sinha
 Last modified: 02/01/2014
 
 Dependencies:  servlets 3+, jedis-2.2.1, gson-2.2.4, commons-pool-1.6, slf4j-1.7.5
 	
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
 		6. Issue fetch request from client
 
 
 Invocation: host:port/context-path/channel?crisisCode={crisisCode}&callback={callback}&count={count}
 ============
 Channel name based examples:
 	1. http://localhost:8080/aidr-output/crisis/getlist/channel?crisisCode=clex_20131201&count=50
  2. http://localhost:8080/aidr-output/crisis/getlist/channel?crisisCode=clex_20131201&callback=func
  3. http://localhost:8080/aidr-output/crisis/getlist/channel?crisisCode=clex_20131201&callback=func&count=50
 
 Wildcard based examples: 
  1. http://localhost:8080/aidr-output/crisis/getlist/channel?crisisCode=*&count=50
  2. http://localhost:8080/aidr-output/crisis/getlist/channel?crisisCode=*&callback=func
  3. http://localhost:8080/aidr-output/crisis/getlist/channel?crisisCode=*&callback=func&count=50
  
 Fully qualified channel name based examples:
  1. http://localhost:8080/aidr-output/crisis/getlist/channel?crisisCode=aidr_predict.clex_20131201&count=50
  2. http://localhost:8080/aidr-output/crisis/getlist/channel?crisisCode=aidr_predict.clex_20131201&callback=func
  3. http://localhost:8080/aidr-output/crisis/getlist/channel?crisisCode=aidr_predict.clex_20131201&callback=func&count=50
  
  
  Parameter explanations:
  	1. crisisCode [mandatory]: the REDIS channel to which to subscribe
  	2. callback [optional]: name of the callback function for JSONP data
  	3. count [optional]: the specified number of messages that have been buffered by the service. If unspecified
  		or <= 0 or larger than the MAX_MESSAGES_COUNT, the default number of messages are returned 
 