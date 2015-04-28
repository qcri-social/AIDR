# Essential API

The following are the essential elements of the API.

_(to be done)_

---

# Additional API

The following are additional elements of the API and/or obsolete documentation prior to 2015-04-28. These should be integrated into the documentation above or moved to the Javadoc-based documentation.


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