# Collector API (aidr-collector)

Base URI: `http://host:port/AIDRCollector/webresources`

The collector project uses different status-codes to represents its different states as follows:

* INITIALIZING: represents that a collection request is starting-up. 
* RUNNING: represents collection task is running fine.
* ERROR: represents a fatal error. Collection request must be submitted again.
* RUNNING-WARNING: represents that collection task is running but with some warning (e.g., twitter track limit warning, stall warning etc.)
* STOPPED: shows that request to stop a collection is fulfilled and collection has been stopped.
* NOT-FOUND: represents a given collection reference is not found in the Fetcher module.

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

Following are the details of parameters used in the above API call.

* `collectionCode` represents user provided unique collection Code
* `collectionName` represents user provided name to a collection 
* `toTrack` represents a comma-separated list of keywords that will be used for tracking purposes.
* `toFollow` represents a comma-separated list twitter users’ IDs which will be followed. A follower ID must be in numeric format.
* `geoLocation` this field represents a comma-separated pairs of longitude and latitude. A valid geo location represents a bounding box with southwest corner of the box coming first. More about this field can be found on this link.

* `languageFilter` represents a comma-separated list of language values to filter tweet stream. The values must be a valid BCP 47 language identifier. 

**Response**
The response of this service will be in JSON format. The details of attributes and their datatypes are as follows:

`statusCode` represents one of the statutes mentioned and described above.

`message`: this shows textual description of a given statusCode. 

Example:

    {
    "statusCode": "RUNNING",
    "statusMessage": "",
    }

## 2. Stop a collection
GET: `/stop?id=xxx`

id: represents the collectionCode.

Example call: `.../fetcher/twitter/stop?id=4534`

## Get status of a running collection by collection code 
GET: `/status?id=xxx`

id: represents the `collectionCode`.

Example call: `.../fetcher/twitter/status?id=324`

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
			
## 3. Get the status of all running tasks 
GET: `/status/all`

Example call: `.../fetcher/twitter/status/all`

## 4. Persist all running collections to disk 
GET: `/manage/persist`

This service intended to be used before deploying new versions of the application. The service persist all running collections in a JSON file on server's default configuration location. After the deployment, you can use the following service to run collections that were persisted.

## 5. Run persisted collections
GET: `/manage/runPersisted`

This service intended to be used after deploying a new version of the system so to run the persisted collection those were backed up. This service reads the persisted file from the disk, and starts collections.
