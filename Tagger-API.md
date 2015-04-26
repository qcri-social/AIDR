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

