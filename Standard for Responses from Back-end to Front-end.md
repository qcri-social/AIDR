Ideally all calls from the front-end to the back-end must be responded in a uniform way with a response object.

All responses from the back-end to the manager front-end should be of this form:

    { 
     return_code: STRING mandatory,
     message_for_user: STRING optional,
     details_for_developer: STRING optional,
    }

Where return_code can take one of the following 3 values, which are strings:

    "SUCCESS" if the operation was done.
    "ERROR" if the operation was not done.
    "WARNING" if the operation was done but there were problems.

The string constants "SUCCESS", "ERROR" and "WARNING" should be defined in the _common_ module in the back-end, and as shared Javascript in the front-end.

# Message for user

Messages for the user are included in the message_for_user field and, when present, must be displayed to the end user.

On SUCCESS, ideally there should not be a message_for_user, allowing the front-end to decide what to show to the user. However, if the response includes a message_for_user, it must be displayed to the user by the front-end.

On WARNING, there must be a message_for_user. A warning indicates that the operation was done, but not perfectly. The message should tell the user what were the issues, and ideally how to correct them.

On ERROR, there must be a message_for_user. An error indicates that the operation was not done. The message should tell the user what was the cause of the problem, and ideally what to do to make the operation succeed.

On both WARNING and ERROR, the message must be _specific_. Do not use generic messages such as "the system was down" or "the system is unavailable", but instead "AIDR could not communicate with Twitter".

# Details for developer

Details for the developer are included in the details_for_developer field. These messages are not displayed to the user, but instead must be shown in the Javascript console.

On SUCCESS, there must not be a details_for_developer.

On WARNING and ERROR, there may be details_for_developer, if the message_for_user is not enough for a developer to understand and locate the cause of a problem. These should include all the information necessary and can be arbitrarily long.
