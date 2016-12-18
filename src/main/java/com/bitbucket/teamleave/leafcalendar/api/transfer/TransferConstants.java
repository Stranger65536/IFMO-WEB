package com.bitbucket.teamleave.leafcalendar.api.transfer;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings("InterfaceNeverImplemented")
public interface TransferConstants {
    char BASE64_DELIMITER = ':';
    String NULL = "null";

    @SuppressWarnings({"PublicInnerClass", "DuplicateStringLiteralInspection"})
    interface Users {
        String USER_ID = "id";
        String FIRST_NAME = "first_name";
        String LAST_NAME = "last_name";
        String PASSWORD = "password";
        String AUTH_BASE64 = "auth_base64";
        String AUTH_TOKEN = "auth_token";
        String EMAIL_BASE64 = "email_base64";
        String NEW_EMAIL_BASE64 = "new_email_base64";
        String EMAIL = "email";
        String PASSWORD_BASE64 = "password_base64";
        String OLD_PASSWORD = "old_password";
        String OLD_PASSWORD_BASE64 = "old_password_base64";
        String NEW_PASSWORD = "new_password";
        String NEW_PASSWORD_BASE64 = "new_password_base64";
        String EMAIL_VALIDATED = "email_validated";
        String EMAIL_VALIDATION_TOKEN = "email_validation_token";
    }

    @SuppressWarnings({"PublicInnerClass", "DuplicateStringLiteralInspection"})
    interface Calendars {
        String CALENDAR_ID = "id";
        String NAME = "name";
        String DESCRIPTION = "description";
        String COLOR = "color";
        String VISIBLE = "visible";
        String REQUIRED = "required";
        String TIMEZONE = "timezone";
    }

    @SuppressWarnings({"DuplicateStringLiteralInspection", "PublicInnerClass"})
    interface Events {
        String EVENTS = "events";
        String EVENT_ID = "id";
        String NAME = "name";
        String DESCRIPTION = "description";
        String LOCATION = "location";
        String COLOR = "color";
        String RELATED = "rel";
        String START_TIME = "start_time";
        String ORIGINAL_START_TIME = "original_start_time";
        String START_TIME_TZ = "start_time_tz";
        String ORIGINAL_END_TIME = "original_end_time";
        String END_TIME = "end_time";
        String END_TIME_TZ = "end_time_tz";
        String REC_END_TIME = "rec_end_time";
        String REC_END_TIME_TZ = "rec_end_time_tz";
        String REC_PERIOD = "rec_period";
        String REC_TYPE_ID = "rec_period_id";
        String RANGE_START = "range_start";
        String RANGE_END = "range_end";
        String REQUEST_TZ = "request_tz";
        String RELATED_SELF = "self";
    }
}