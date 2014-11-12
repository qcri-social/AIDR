Ideally all calls from the front-end to the back-end must be responded in a uniform way with a response object.

All responses from the back-end to the manager front-end should be of this form:

    { 
     return_code: STRING mandatory,
     message_for_user: STRING optional,
     details_for_developer: STRING optional,
    }

Where return_code can take one of the following 3 values:

    SUCCESS if the operation was done.
    ERROR if the operation was not done.
    WARNING if the operation was done but there were problems.

# Messages for user

Messages for the user are included in the message_for_user field.

If there is a message_for_user, the message MUST be displayed to the user, independently on whether there was SUCCESS, ERROR, or WARNING. 


- On success the back-end should not return a message_for_user (it should let the front-end decide how to present success for users). However, if there is a message_for_user, this message should be displayed.
- On error the back-end should return a specific message_for_user (not a generic error message).

The details_for_developer is for technical details. In general these SHOULD NOT be shown to the user but e.g. shown in the console.

The string constants "SUCCESS", "ERROR" and "WARNING" should be defined in:

- aidr-common.jar
- some shared .js in the front-end.
