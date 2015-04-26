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
