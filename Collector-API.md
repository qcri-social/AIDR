# TwitterCollectorAPI

## start

startTask() - start collection

## stop

stopTask() - stop collection

---

# Obsolete documentation

The documentation below was marked as obsolete by ChaTo on 2015-04-28.

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

