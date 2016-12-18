General information
===================

***Generic request data format***

This is a RESTful API service. Most endpoints of service requires authorization token in request headers. This token will be generated to your account after registraion. This token is not permanent, it can be regenerated manually by user, or automatically when email or password got changed. Token can be retrieved as response field of some service endpoints and using login endpoint.

    auth_token: '^[A-F0-9]{32}$' (3B578C494C7C47CA8E2DA59C7E631B2C)

***Generic response data format*** 

    {
        "status": Boolean,
        "message": String,
        "data": {
            ...
        }
    }

Any valid request to API returns HTTP status 200 OK, except transfer protocol errors (returns 400 Bad request) and some epic impossible exceptions (returns 500 Internal server error).

All valid requests contains field "status" that indicates success of executing specified request; "message" field that contains short information about request execution result (empty string if success); polymorphic field "data" that contains result data set for specified request (null if request failed).

----------

1. User endpoints
=================

1.1. Registration
-----------------

> POST /users/

***Request header***

This endpoint does not require auth token.

***Request body***

    {
        "first_name": String,
        "last_name": String,
        "auth_base64": String
    } 

***Constraints***

    first_name  : '^.{1,255}$' after whitespace trim
    last_name   : '^.{1,255}$' after whitespace trim
    auth_base64 : email '^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$' and password '^.{8,}$' encoded with base64 with ":" delimiter

***Response***

    {
        "id": '^[A-F0-9]{32}$' 
    } 

----------

1.2. Validation
---------------

> POST /users/{user_id}/validate/

***Request header***

This endpoint does not require auth token.

***Request body***

    {
        "email_validation_token": String
    } 

***Constraints***

    user_id                : '^[A-F0-9]{32}$'
    email_validation_token : '^[A-F0-9]{32}$'

***Response***

This endpoint has empty response data set.

----------

1.3. Login
----------

Return auth token by user credentials.

> POST /users/login/

***Request header***

This endpoint does not require auth token.

***Request body***

    {
        "auth_base64": String
    } 

***Constraints***

    auth_base64 : email '^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$' and password '^.{8,}$' encoded with base64 with ":" delimiter

***Response***

    {
        "id": '^[A-F0-9]{32}$'
        "auth_token": '^[A-F0-9]{32}$' 
    } 

----------

1.4. Update password
--------------------

After successfully execution auth token will be regenerated. Old token will be invalid. New token can be retrieved by login.

> POST /users/{user_id}/update/password/

***Request header***

This endpoint requires auth token to process.

***Request body***

    {
        "old_password_base64": String,
        "new_password_base64": String
    } 

***Constraints***

    user_id             : '^[A-F0-9]{32}$'
    old_password_base64 : '^.{8,}$' encoded with base64
    new_password_base64 : '^.{8,}$' encoded with base64

***Response***

This endpoint has empty response data set.

----------

1.5. Update email
-----------------

After successfully execution auth token will be regenerated. Old token will be invalid. New token can be retrieved by login.

> POST /users/{user_id}/update/email/

***Request header***

This endpoint requires auth token to process.

***Request body***

    {
        "password_base64": String,
        "new_email_base64": String
    } 

***Constraints***

    user_id          : '^[A-F0-9]{32}$'
    password_base64  : '^.{8,}$' encoded with base64
    new_email_base64 : '^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$' encoded with base64

***Response***

This endpoint has empty response data set.

----------

1.6. Send validation mail
-------------------------

Send validation mail if account was not validated. If account is already validated, error message will occur.

> POST /users/{user_id}/notify/validate/

***Request header***

This endpoint requires auth token to process.

***Request body***

This endpoint does not require request body.

***Constraints***

    user_id : '^[A-F0-9]{32}$'

***Response***

This endpoint has empty response data set.

----------

1.7. Logout
-----------

After successfully execution auth token will be regenerated. Old token will be invalid. New token can be retrieved by login.

> POST /users/{user_id}/logout

***Request header***

This endpoint requires auth token to process.

***Request body***

This endpoint does not require request body.

***Constraints***

    user_id : '^[A-F0-9]{32}$'

***Response***

This endpoint has empty response data set.

----------

1.8. Get user info
------------------

> GET /users/{user_id}/

***Request header***

This endpoint requires auth token to process.

***Request body***

This endpoint does not require request body.

***Constraints***

    user_id : '^[A-F0-9]{32}$'

***Response***

    {
        first_name: '^.{1,255}$' 
        last_name: '^.{1,255}$' 
        email: '^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$' 
        email_validated: true/false
    }

----------

1.9. Update user info
---------------------

All request fields must be specified to process.

> POST /users/{user_id}/update/

***Request header***

This endpoint requires auth token to process.

***Request body***

    {
        "first_name": String
        "last_name": String
    }

***Constraints***

    user_id    : '^[A-F0-9]{32}$'
    first_name : '^.{1,255}$' after whitespace trim
    last_name  : '^.{1,255}$' after whitespace trim

***Response***
    
This endpoint has empty response data set.

----------

1.10. Delete user
-----------------

Delete user from system. All user data will be erased immediately. Can't be undone.

> DELETE /users/{user_id}/

***Request header***

This endpoint requires auth token to process.

***Request body***

    {
        "password_base64": String
    } 

***Constraints***

    user_id         : '^[A-F0-9]{32}$'
    password_base64 : '^.{8,}$' encoded with base64

***Response***

This endpoint has empty response data set.

----------

2. Calendar endpoints
=====================

2.1. Create calendar
--------------------

> POST /users/{user_id}/calendars/

***Request header***

This endpoint requires auth token to process.

***Request body***

    {
        "name": String,
        "description": String,
        "color": String,
        "visible": String,
        "timezone": String
    } 

***Constraints***

    user_id     : '^[A-F0-9]{32}$'
    name        : '^.{1,255}$' after whitespace trim
    description : null or '^.{0,1024}$' after whitespace trim
    color       : null or '^[0-9a-fA-F]{6}$' 
    visible     : null or true/false
    timezone    : null or ISO 8601 (timezone)

***Response***

    {
        "id": '^[A-F0-9]{32}$' 
    } 

----------

2.2. Get all calendars
----------------------

> GET /users/{user_id}/calendars/

***Request header***

This endpoint requires auth token to process.

***Request body***

This endpoint does not require request body.

***Constraints***

    user_id     : '^[A-F0-9]{32}$'

***Response***

    {
        "id": [
            '^[A-F0-9]{32}$'
        ]
    } 

----------

2.2. Get calendar info
----------------------

> GET /users/{user_id}/calendars/{calendar_id}/

***Request header***

This endpoint requires auth token to process.

***Request body***

This endpoint does not require request body.

***Constraints***

    user_id     : '^[A-F0-9]{32}$'
    calendar_id : '^[A-F0-9]{32}$'

***Response***

    {
        "name": '^.{1,255}$'
        "description": '^.{0,1024}$'
        "color": '^[0-9a-fA-F]{6}$' 
        "visible": true/false
        "timezone": null or ISO 8601 (timezone)
    } 

----------

2.3. Update calendar info
-------------------------

All request fields must be specified to process.

> POST /users/{user_id}/calendars/{calendar_id}/update/

***Request header***

This endpoint requires auth token to process.

***Request body***

    {
        "name": String,
        "description": String,
        "color": String,
        "visible": String,
        "timezone": String
    } 

***Constraints***

    user_id     : '^[A-F0-9]{32}$'
    calendar_id : '^[A-F0-9]{32}$'
    name        : '^.{1,255}$' after whitespace trim
    description : '^.{0,1024}$' after whitespace trim
    color       : '^[0-9a-fA-F]{6}$' 
    visible     : true/false
    timezone    : null or '^Z|[+-]\d{2}|[+-]\d{4}|[+-]\d{2}:\d{2}$'

***Response***

This endpoint has empty response data set.

----------

2.4. Delete calendar
--------------------

> DELETE /users/{user_id}/calendars/{calendar_id}/

***Request header***

This endpoint requires auth token to process.

***Request body***

This endpoint does not require request body.

***Constraints***

    user_id     : '^[A-F0-9]{32}$'
    calendar_id : '^[A-F0-9]{32}$'

***Response***

This endpoint has empty response data set.

----------

3. Event endpoints
==================

Provides creation of single and recurrent events. 
"start_time_tz" and "end_time_tz" are optional, but must be specified in pair. "color" is optional, inherits from calendar if not specified. Last 4 parameters are optional, but "rec_end_time" and "rec_end_time_tz"; "rec_period" and "rec_period_id" must be specified together. Event will be processed as recurrent if rec_period_id will be specified.

3.1. Create event
-----------------

> POST /users/{user_id}/calendars/{calendar_id}/events/

***Request header***

This endpoint requires auth token to process.

***Request body***

    {
        "name": String,
        "description": String,
        "location": String,
        "color": String,
        "start_time": String,
        "start_time_tz": String,
        "end_time": String,
        "end_time_tz": String,
        "rec_end_time": String,
        "rec_end_time_tz": String,
        "rec_period": Integer,
        "rec_period_id": Integer
    } 

***Constraints***

    user_id         : '^[A-F0-9]{32}$'
    calendar_id     : '^[A-F0-9]{32}$'
    event_id        : '^[A-F0-9]{32}$'
    name            : '^.{1,255}$' after whitespace trim
    description     : null or '^.{0,1024}$' after whitespace trim
    location        : null or '^.{0,1024}$' after whitespace trim
    color           : null or '^[0-9a-fA-F]{6}$'
    start_time      : ISO 8601 (date + time + timezone)
    start_time_tz   : null or ISO 8601 (timezone)
    end_time        : ISO 8601 (date + time + timezone)
    end_time_tz     : null or ISO 8601 (timezone)
    rec_end_time    : null or ISO 8601 (date + time + timezone)
    rec_end_time_tz : null or ISO 8601 (timezone)
    rec_period      : null or 1-31
    rec_period_id   : null or DAY(0), WEEK(1), MONTH(2), YEAR(3)

***Response***

    {
        "id": '^[A-F0-9]{32}$'
    } 

----------

3.2. Get all events for range
-----------------------------

Returns all events (include recurrence events realizations) for specified time range according specified timezone (events will be returned with times converted to the request timezone, except if calendar specifies force timezone).

Response contains only event id (does not exist for recurrent event realizations), relation (null for single events, contains id of parent recurrent event), and time range to show in calendar. All additional information should be fetched by event_id first (if not null), or by relation.

> GET /users/{user_id}/calendars/{calendar_id}/events/?range_start&range_end&request_tz

***Request header***

This endpoint requires auth token to process.

***Request body***

This endpoint does not require request body.

***Constraints***

    user_id     : '^[A-F0-9]{32}$'
    calendar_id : '^[A-F0-9]{32}$'
    range_start : ISO 8601 (date + time + timezone)
    range_end   : ISO 8601 (date + time + timezone)
    request_tz  : ISO 8601 (timezone)

***Response***

    {
        [
            "event_id": null or '^[A-F0-9]{32}$'
            "rel": null or '^[A-F0-9]{32}$'
            "start_time": ISO 8601 (date + time)
            "end_time": ISO 8601 (date + time)
        ]
    } 

----------

3.3. Get event info
-------------------

> GET /users/{user_id}/calendars/{calendar_id}/events/{event_id}/

***Request header***

This endpoint requires auth token to process.

***Request body***

This endpoint does not require request body.

***Constraints***

    user_id     : '^[A-F0-9]{32}$'
    calendar_id : '^[A-F0-9]{32}$'
    event_id    : '^[A-F0-9]{32}$'

***Response***

    {
        "rel": '^[A-F0-9]{32}$'
        "event_id": '^[A-F0-9]{32}$'
        "name": '^.{1,255}$' 
        "description": null or '^.{0,1024}$' 
        "location": null or '^.{0,1024}$' 
        "color": '^[0-9a-fA-F]{6}$'
        "start_time": ISO 8601 (date + time + timezone)
        "start_time_tz": null or ISO 8601 (timezone)
        "end_time": ISO 8601 (date + time + timezone)
        "end_time_tz": null or ISO 8601 (timezone)
        "rec_end_time": null or ISO 8601 (date + time + timezone)
        "rec_end_time_tz": null or ISO 8601 (timezone)
        "rec_period": null or 1-31
        "rec_period_id": null or DAY(0), WEEK(1), MONTH(2), YEAR(3)
    } 

----------

3.4. Update entire event
------------------------

All request fields must be specified to process.

> POST /users/{user_id}/calendars/{calendar_id}/events/{event_id}/update/

***Request header***

This endpoint requires auth token to process.

***Request body***

    {
        "name": String,
        "description": String,
        "location": String,
        "color": String,
        "start_time": String,
        "start_time_tz": String,
        "end_time": String,
        "end_time_tz": String,
        "rec_end_time": String,
        "rec_end_time_tz": String,
        "rec_period": Integer,
        "rec_period_id": Integer
    } 

***Constraints***

    user_id         : '^[A-F0-9]{32}$'
    calendar_id     : '^[A-F0-9]{32}$'
    event_id        : '^[A-F0-9]{32}$'
    name            : '^.{1,255}$' after whitespace trim
    description     : null or '^.{0,1024}$' after whitespace trim
    location        : null or '^.{0,1024}$' after whitespace trim
    color           : null or '^[0-9a-fA-F]{6}$'
    start_time      : ISO 8601 (date + time + timezone)
    start_time_tz   : null or ISO 8601 (timezone)
    end_time        : ISO 8601 (date + time + timezone)
    end_time_tz     : null or ISO 8601 (timezone)
    rec_end_time    : null or ISO 8601 (date + time + timezone)
    rec_end_time_tz : null or ISO 8601 (timezone)
    rec_period      : null or 1-31
    rec_period_id   : null or DAY(0), WEEK(1), MONTH(2), YEAR(3)

***Response***

This endpoint has empty response data set.

----------

3.5. Delete entire event
------------------------

> DELETE /users/{user_id}/calendars/{calendar_id}/events/{event_id}/

***Request header***

This endpoint requires auth token to process.

***Request body***

This endpoint does not require request body.

***Constraints***

    user_id     : '^[A-F0-9]{32}$'
    calendar_id : '^[A-F0-9]{32}$'
    event_id    : '^[A-F0-9]{32}$'

***Response***

This endpoint has empty response data set.

----------