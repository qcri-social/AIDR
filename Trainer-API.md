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

