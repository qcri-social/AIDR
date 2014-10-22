# COLLECTOR API (aidr-collector)

Base URI: `http://host:port/AIDRCollector/webresources`

## Twitter Collector
URL: Base URI + 'twitter/'

### 1. Start a collection
POST Method: `/start`
	Request Headers: `Content-Type: application/json`
	Accept:  `application/json` 
	
Request Body Example: 

    {
    "collectionCode": "JapanEQ2013",
    "collectionName": "Japan Earthquake",
    "toTrack": "#earthquake, #japan",
    "toFollow": "2342355, 9837498, 3489098",
    "geoLocation": "-74,40,-73,41",
    "languageFilter": "en, ar, ja",
    "consumerKey": "cd2CbYFSSkRi20hsfsdfaaQ",
    "consumerSecret": "zdRugnKMqBmiIRVLbasdfasdfuQS3w5YpR0naYyHSYCY",
    "accessToken": "57342232-T7YwJSZ34XesdfsdfDBboduYzOFikHDJ9zXVXR0g",
    "accessTokenSecret": "VZr1beowvLksdfsdfszkEXx1z68oks4hm8JCUGeRDw"
    }

Parameters details:

* `collectionCode` represents user provided unique collection Code
* `collectionName` represents user provided name to a collection 
* `toTrack` represents a comma-separated list of keywords that will be used for tracking purposes.
* `toFollow` represents a comma-separated list of twitter users’ IDs to be followed. A follower ID must be in numeric format.
* `geoLocation` represents a comma-separated pairs of longitude and latitude. A valid geo location represents a bounding box with the southwest corner of the box coming first. 
* `languageFilter` represents a comma-separated list of languages to filter tweets stream. The values must be a valid BCP 47 language identifier. 

**Response**
Response of this service will be in JSON format. Details of attributes and their datatypes are following:

Response example:

    {
    "statusCode": "RUNNING",
    "statusMessage": "",
    }

`statusCode` represents one of the statutes mentioned below.

* INITIALIZING: represents that a collection request is valid and the collection is in initializing state. 
* RUNNING: represents collection task is running.
* ERROR: represents a fatal error. Collection request must be submitted again.
* RUNNING-WARNING: represents that collection task is running but with some warning (e.g., twitter track limit warning, stall warning etc.)
* STOPPED: shows that request to stop a collection is fulfilled and collection has been stopped.
* NOT-FOUND: represents that there is no collection running with the provided collection reference.

`message`: this shows textual description of a given statusCode. 


### 2. Stop a collection
GET: `/stop?id=xxx`

id: represents the collectionCode.

Example call: `.../twitter/stop?id=4534`

### 3. Get status of a running collection by collection code 
GET: `/status?id=xxx`

id: represents the `collectionCode`.

Example call: `.../twitter/status?id=324`

Response:
    
    {
    "collectionCode": "syria-civil-war",
    "collectionName": "Syria Collection",
    "toTrack": "syria, damascus, hama, #syrie",
    "tweetsCount": 60,
    "lastDocument":"here twitter message will appear",
    "statusCode": "RUNNING",
    "statusMessage": "",
     }
			
### 4. Get the status of all running tasks 
GET: `/status/all`

Example call: `.../twitter/status/all`

### 5. Persist all running collections to disk 
GET: `/manage/persist`

This service intended to be used before deploying new versions of the application. The service persists all running collections in a JSON file on server's default configuration location. After the deployment, you can use the following service to run collections that were persisted.

### 6. Run persisted collections
GET: `/manage/runPersisted`

This service intended to be used after deploying a new version of the application so to re-start the persisted collections. This service reads the persisted file from the disk, and starts collections.

## SMS Collector
URL: `Base URI + '/sms/'`

### 1. Start a collection
GET Method: `/start?collection_code=xyz`
	Accept:  `application/json` 

### 2. Receive SMSs
POST Method: `/endpoint/receive/{collection_code}'
	Accept:  `application/json` 

Request Body Example: 

    {
    "text":"Magnitude1 1.7 #earthquake, 3.6 km SE of Dyer, CA http://t.co/22qJDL6snM", 
    "aidr":
    {
      "crisis_code":"Earthquake_WW",
      "crisis_name":"World Wide Earthquake Tracker",
      "doctype":"sms"
    }
    }
   
### 2. Stop a collection
GET: `/stop?collection_code=xxx`

Example call: `.../sms/stop?collection_code=4534`

### 3. Get status of a running collection by collection code 
GET: `/status?collection_code=xxx`

Example call: `.../sms/status?collection_code=324`

# TAGGER API (aidr-tagger-api)
Base URI: `http://localhost:port/aidr-tagger-api/rest/`

## Check crisis exists or not by CrisisCode
GET `/crisis/code/{code}`

Example: /crisis/code/sandy2012

Response:

    {
    "crisisCode":"sand2012", 
    "exists":"false"
    }

## Get all running collections by UserID (from aidr-collector)

GET `/collection/{userID}`

## Get all crisis types

GET `/crisisType/all`

## Check if user exists by username

GET `/user/{userName}`
	
## Add a new user

POST `/user`
	
Example:
    
    {
       "name": "Jhon13","role": "normal",
    }

## Create a new crisis

POST `/crisis

Example:
   {
     "code": "Test2",
     "name": "Syria Civil War",
       "crisisType":
       {
           "crisisTypeID": "2"
       },
       "users":
       {
           "userID": "1"
       }
   }

## Get crises, attributes, and labels by UserID 

GET `crisis?userID={id}`

Example: `crisis?userID=1`
	
## Get all the models for a given crisisID

GET `model/crisis/{crisisID}`

## Get all attributes

GET `attribute/all`

Note: 
Standard attributes always come under SYSTEM user. So in the resultset look for userID=1 for standard
attributes. Custom attributes are user-defined attribute so they appear with userID other than 1.

## Get attributes and labels by attributeID

GET `attribute/{attributeID}`

## Get all attributes except the attributes of the given Crisis
GET `attribute/crisis/all?exceptCrisis={crisisID}`

Example: `attribute/crisis/all?exceptCrisis=23`

## Add an attribute to a crisis

POST `/modelfamily`

Body:
    {
    "crisis":{
    "crisisID": "1"
    },
    "nominalAttribute":{
    "nominalAttributeID":"23"
    },
    "isActive":"false"
    }

## Delete an attribute/classifier from a crisis

DELETE `/modelfamily/{id}`

id: represents the id of an attribute/classifier

## Get all labels given a modelID
GET `modelNominalLabel/{modelID}`

## Get training data by crisis and attributeID

GET `base_uri/misc/getTrainingData?crisisID=14&attributeID=15&fromRecord=0&limit=50`
	
## Is attribute exists

GET `base_uri/attribute/code/{code}`

Returns attributeID > 0 if attribute exists, otherwise attributeID = 0
	
# Manage crisis types (resource path: /crisisType)

## Add a new crisis type
POST: `/`
Example: 

    {
      "name": "Earthquake"
    }

Response example:
    
    {
    "crisisTypeID":"293",
    "name":"Earthquake"	
    }

## Retrieve crisis type by crisisID
GET: `/{id}`

id: represents the crisisID.
	
Example call: `.../crisisType/2`

## Update crisis type

PUT: `/`

Example body:
   
    {
     "crisisTypeID": "213",
     "name": "Earthquake Crisis",
    }

## Delete crisis type

DELETE: `/{id}`

id: represents the crisisId.
Example call: `.../crisisType/5`

# Manage Attributes (resource path: /attribute)

## Add Attribute(s)
POST `/`

Example: 

    {
    "code": "Casualties",
    "name": "People Killed",
    "description" :"Represents people killed",
    "users":
           {
               "userID": "1"
           }
    }

## Retrieve an attribute
GET: `/{id}`

ID represents attributeID.

## Update an attribute
PUT `/`

Example: 

    {
    "nominalAttributeID" : "1",
    "code": "Casualties",
    "name": "People Killed",
    "description" :"Represents people killed",
    "users":
           {
               "userID": "1"
           }
    }

## Delete an attribute

DELETE: `/{id}`

ID represents attributeID.

Example call: `.../attribute/5`

# Manage Labels (resource path: /label)

## Add label(s)
POST `/`

Example: 

    {
    "nominalLabelCode": "test123", 
    "description": "testsd", 
    "name": "test", 
    "nominalAttributeID":"1"
    }

## Retrieve a label
GET: `/{id}`

ID represents labelID.

## Update a label
PUT `/`

Example: 
    
    {
     "description": "This is description",
     "name": "test",
     "nominalLabelCode": "test123",
     "nominalLabelID": "93",
     "nominalAttributeID": "1"
    }

## Delete a Label
DELETE: `/{id}`

ID represents labelID.

Example call: `.../label/5`

# PERSISTER API (aidr-persister)

Base URI: `http://localhost:port/aidr-persister/webresources`

## Start persister for aidr-collector

GET `.../persister/start?file="file_location"&collectionCode=XXX`

`file` parameter represents the location where persister should persist the content of collection.

`collectionCode` represents the collectionCode for which the persister should start persistance.

## Stop persister for aidr-collector

GET `.../persister/stop?collectionCode`

`collectionCode` represents the code of collection for which the persistance should be stopped.

## Generate CSV out of last X collected tweets

GET `.../persister/genCSV?collectionCode=XXX&exportLimit=34234`

`collectionCode` represents the code of collection for which a CSV file should be generated.

`exportLimit` represents limit of tweets to be exported. Datatype of this parameter is number.

Note: this export service uses full tweet content. Use the next service if only tweet-ids needed to be exported.

# Persister APIs for downloading data

## Generate JSON data file out of last X collected tweets

GET `.../persister/genJson?collectionCode=XXX&exportLimit=34234&jsonType=JSON`

`collectionCode` represents the code of collection for which a CSV file should be generated.

`exportLimit` represents limit of tweets to be exported. Datatype of this parameter is number.

`jsonType` instructs `aidr-persister to create one JSON array of all tweets to be included.  


## Generate TEXT JSON data file out of last X collected tweets

GET `.../persister/genJson?collectionCode=XXX&exportLimit=34234&jsonType=TEXT_JSON`

`collectionCode` represents the code of collection for which a CSV file should be generated.

`exportLimit` represents limit of tweets to be exported. Datatype of this parameter is number.

`jsonType` instructs `aidr-persister to create a text file with one JSON object per line.  


## Generate CSV of all tweets (only tweet-ids)

GET `.../persister/genTweetIds?collectionCode=XXX`

`collectionCode` represents the code of collection for which a CSV file should be generated.


## Generate JSON data file from all tweets (only tweet-ids)

GET `.../persister/genJsonTweetIds?collectionCode=XXX&jsonType=JSON`

`collectionCode` represents the code of collection for which a CSV file should be generated.

`jsonType` instructs `aidr-persister to create one JSON array of all tweet IDs to be included.  


## Generate TEXT JSON data file from all tweets (only tweet-ids)

GET `.../persister/genJsonTweetIds?collectionCode=XXX&jsonType=TEXT_JSON`

`collectionCode` represents the code of collection for which a CSV file should be generated.

`jsonType` instructs `aidr-persister to create a text file with one JSON object per line.  


## NOTE 

**For each of the above persister download REST APIs, replacing `/persister/...` by `/taggerPersister/...` will generate a downloadable file in the corresponding format but only for classified tweets, i.e., with AIDR classification data appended to each classified tweet**.  


## Generate CSV out of last X collected tweets, filtered by selection criteria

POST `.../taggerPersister/filter/genCSV?collectionCode=XXX&exportLimit=34234`

`collectionCode` represents the code of collection for which a CSV file should be generated.

`exportLimit` represents limit of tweets to be exported. Datatype of this parameter is number.


## Generate CSV out of last X collected tweets, filtered by selection criteria

POST `.../taggerPersister/filter/genCSV?collectionCode=XXX&exportLimit=34234`

`collectionCode` represents the code of collection for which a CSV file should be generated.

`exportLimit` represents limit of tweets to be exported. Datatype of this parameter is number.

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
      "timestamp": 1495339860    
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


## Generate JSON out of last X collected tweets, filtered by selection criteria

POST `.../taggerPersister/filter/genJson?collectionCode=XXX&exportLimit=34234&jsonType=JSON`

`collectionCode` represents the code of collection for which a CSV file should be generated.

`exportLimit` represents limit of tweets to be exported. Datatype of this parameter is number.

`jsonType` instructs `aidr-persister to create one JSON array of all tweet IDs to be included. 

For the POST request payload details, refer to the `POST .../taggerPersister/filter/genCSV` documentation above.


## Generate TEXT JSON out of last X collected tweets, filtered by selection criteria

POST `.../taggerPersister/filter/genJson?collectionCode=XXX&exportLimit=34234&jsonType=TEXT_JSON`

`collectionCode` represents the code of collection for which a CSV file should be generated.

`exportLimit` represents limit of tweets to be exported. Datatype of this parameter is number.

`jsonType` instructs `aidr-persister to create a text file with one JSON object per line. 

For the POST request payload details, refer to the `POST .../taggerPersister/filter/genCSV` documentation above.


## Generate CSV of all tweets (only tweet-ids), filtered by selection criteria

POST `.../taggerPersister/filter/genTweetIds?collectionCode=XXX`

`collectionCode` represents the code of collection for which a CSV file should be generated.

For the POST request payload details, refer to the `POST .../taggerPersister/filter/genCSV` documentation above.


## Generate JSON of all tweets (only tweet-ids), filtered by selection criteria

POST `.../taggerPersister/filter/genJsonTweetIds?collectionCode=XXX&jsonType=JSON`

`collectionCode` represents the code of collection for which a CSV file should be generated.

`jsonType` instructs `aidr-persister to create one JSON array of all tweet IDs to be included.

For the POST request payload details, refer to the `POST .../taggerPersister/filter/genCSV` documentation above.


## Generate TEXT JSON of all tweets (only tweet-ids), filtered by selection criteria

POST `.../taggerPersister/filter/genJsonTweetIds?collectionCode=XXX&jsonType=TEXT_JSON`

`collectionCode` represents the code of collection for which a CSV file should be generated.

`jsonType` instructs `aidr-persister to create a text file with one JSON object per line.

For the POST request payload details, refer to the `POST .../taggerPersister/filter/genCSV` documentation above.



# OUTPUT API (aidr-output)

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


# TRAINER API (aidr-trainer-api)

Base URI: `http://localhost:port/AIDRTrainerAPI/rest`


## For a given crisisID, retrieve all nominal attributes and their labels

GET `.../rest/crisis/id/{crisisID}`

Response Example: 

`{
  "crisisID": 117,
  "name": "Malaysia Airlines flight #MH370",
  "code": "2014-03-mh370",
  "nominalAttributeJsonModelSet": [
    {
      "nominalLabelJsonModelSet": [
        {
          "norminalLabelCode": "null",
          "name": "N/A: does not apply, or cannot judge",
          "norminalLabelID": 320,
          "description": "If these categories do not apply to this message, or you cannot be sure about which is the correct category"
        },
        {
          "norminalLabelCode": "praying",
          "name": "Praying",
          "norminalLabelID": 319,
          "description": "If author of the tweet prays for flight MH370 passengers."
        }
      ],
      "description": "This classifier classifies tweets into informative, praying, and personal categories.",
      "nominalAttributeID": 533,
      "name": "Message type",
      "code": "informative_pray_personal"
    },
    {
      "nominalLabelJsonModelSet": [
        {
          "norminalLabelCode": "yes",
          "name": "Yes",
          "norminalLabelID": 321,
          "description": "If the tweet reports possible terrorism act involved."
        },
        {
          "norminalLabelCode": "null",
          "name": "N/A: does not apply, or cannot judge",
          "norminalLabelID": 322,
          "description": "If these categories do not apply to this message, or you cannot be sure about which is the correct category"
        },
        {
          "norminalLabelCode": "no",
          "name": "No",
          "norminalLabelID": 323,
          "description": "If the tweet is not about terrorism related to the flight MH370"
        }
      ],
      "description": "Indicates if the tweet reports information about possible hijacking of the flight MH370",
      "nominalAttributeID": 534,
      "name": "Terrorism",
      "code": "terrorism"
    }
  ]
}`


**Important parameter details**

* `nominalAttributeID`: unique ID of a nominal attribute

* `norminalLabelID`: unique ID of a label associated with a particular nominal attribute

* `code`: code associated with a nominal attribute

* `norminalLabelCode`: code of a label associated with a particular nominal attribute


# AIDR Analytics API (aidr-analytics)

Base URI: `http://localhost:port/AIDRAnalytics/rest/analytics`

## Provide the total number of tweets per each `tag` in the time window from `startTime` to current time

GET `/getLabelSum/{crisisCode}/{classifierCode}/{granularity}?startTime=XXXX`

`Content-Type: application/json` 

* granularity: long value

* startTime: unix time (long value) 



## Provide the tweet count per `tag` per (granularity) instance in the time window from `startTime` to current time

GET `/getLabelCount/{crisisCode}/{attributeCode}/{granularity}?startTime=XXXX`

`Content-Type: application/json`  

* granularity: long value
* startTime: unix time (long value)

## Return Time Series data for each `tag` in the interval [startTime, endTime] 

GET `/getLabelTimeSeries/{crisisCode}/{attributeCode}/{granularity}?startTime=XXXX&endTime=YYYY`

`Content-Type: application/json`  

* granularity: long value
* startTime: unix time (long value)
* endTime: unix time (long value)


## Count total number of tweets for each `tag` in the interval [startTime, endTime]

GET `/getIntervalLabelSum/{crisisCode}/{attributeCode}/{granularity}?startTime=XXXX&endTime=YYYY`

`Content-Type: application/json` 

* granularity: long value
* startTime: unix time (long value)
* endTime: unix time (long value)


## Ping aidr-analytics tag statistics data generator module

GET `/ping`

`Content-Type: application/json` 

`{
     "aidr-analysis/tagData": "RUNNING"
}`