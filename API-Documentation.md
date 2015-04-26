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