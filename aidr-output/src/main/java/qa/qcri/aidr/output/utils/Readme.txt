@author Koushik Sinha
Last modified: 26/01/2014
 
Provides a method to test whether:
  		i) Connection to REDIS is available
  		ii) AIDR Output services are returning non-null results
  
   Invocation:	host:port/context-root/rest/manage/ping?callback={callback}
   
   Example: 
   	1. localhost:8080/AIDROutput/rest/manage/ping
   	2. localhost:8080/AIDROutput/rest/manage/ping?callback=JSONP
