Base URI: `http://localhost:port/aidr-output/rest`

## Get a JSONP list of labeled tweets from a specific channel

GET `.../rest/crisis/fetch/channel/{crisisCode}?callback={callbackName}&count={count}`

* `crisisCode` [mandatory]: Redis channel identifier to which to subscribe
* `callback` [optional]: name of the callback function for JSONP data
* `count` [optional]: the specified number of messages that have been buffered by the service. If unspecified or <= 0 or larger than the MAX_MESSAGES_COUNT field, the default number of messages are returned.

## Return a list of active channels

GET `.../rest/crisis/fetch/channels/list`

Returns an HTML page listing the currently active channels that are being monitored by `aidr-output`

## Get the latest labeled tweet across all channels as a JSONP object

GET `.../rest/crisis/fetch/channels/latest`?confidence={val}

When query parameter `confidence` is specified, then only a tweet which has at least one nominal label with confidence > val is returned, otherwise only a tweet which has at least one nominal label with confidence > 0.7 is returned (default behavior). 

## Stream jsonp data from Redis for a specific channel

GET `...rest/crisis/stream/filter/channel/{crisisCode}?callback={callback}&rate={rate}&duration={duration}`

* `crisisCode` [mandatory]: Redis channel identifier to which to subscribe
* `callback` [optional]: name of the callback function for JSONP data
* `rate` [optional]: an upper bound on the rate at which to send messages to client, expressed as messages/min (a floating point number). If <= 0, then default rate is assumed.
* `duration` [optional]: time for which to subscribe (connection automatically closed after that). The allowed suffixes are: s (for seconds), m (for minutes), h (for hours) and d (for days). If nothing is specified, then by default, duration is disabled. 

## Test whether aidr-output is running

GET `.../rest/manage/ping`

Provides a method to test whether:
* Connection to Redis is available
* `aidr-output` services are running as expected

## Get a JSONP list of labeled tweets from a specific channel, filtered by selection criteria 

POST `.../rest/crisis/fetch/channel/filter/{crisisCode}?callback={callbackName}&count={count}`

* `crisisCode` [mandatory]: Redis channel identifier to which to subscribe
* `callback` [optional]: name of the callback function for JSONP data
* `count` [optional]: the specified number of messages that have been buffered by the service. If unspecified or <= 0 or larger than the MAX_MESSAGES_COUNT field, the default number of messages are returned.

POST request payload example: 
Request Headers: `Content-Type: application/json`
	         `Accept`:  `application/json` 

**Note**: If no filtering required, then the payload will be as follows:

`{
  "constraints": []
 }`


Otherwise, if filtering required, then:

`{
  "constraints": [
    {
      "queryType": "date_query",    
      "comparator": "is_before",     
      "timestamp": 1575339860    
    },                                                        
    {    
      "queryType": "date_query",         
      "comparator": "is_after",      
      "timestamp": 1272339860    
    },    
    {   
      "queryType": "classifier_query",    
      "classifier_code": "informative_pray_personal",    
      "label_code": "praying",    
      "comparator": "is",     
      "min_confidence": 0.8    
    },    
    {     
      "queryType": "classifier_query",     
      "classifier_code": "informative_pray_personal",     
      "label_code": "030_info",     
      "comparator": "is_not"    
    },     
    {    
      "queryType": "classifier_query",    
      "classifier_code": "informative_pray_personal",    
      "label_code": null,     
      "comparator": "has_confidence",     
      "min_confidence": 0.5    
    }    
  ]    
}`   

**Note**: Only those documents that satisfy ALL the constraints are returned. 

**Parameter details**: 

* `queryType`: indicates type of query. Currently can take only two values - `"date_query"` and `"classifier_query"`. 

* `comparator`: query predicate evaluation criterion. For `date_query` it can be either `is_after` or `is_before` depending on whether to filter documents that occurred after or before the specified `timestamp` value, respectively. For `classifier_query` it can be either `is` or `is_not` or `has_confidence`.   

* `timestamp`: unix time specified in `date_query` as Java type `long`. 

* `classifier_code`: corresponds to the `attribute_code`.

* `label_code`: corresponds to a valid label_code for a given classifier_code. 

* `min_confidence`: Include only those documents for which the `confidence` of the specified `classifier_code` is greater than `min_confidence`. Is Java `float` type.


