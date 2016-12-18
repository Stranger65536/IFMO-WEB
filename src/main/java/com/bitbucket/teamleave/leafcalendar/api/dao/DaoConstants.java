package com.bitbucket.teamleave.leafcalendar.api.dao;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings("InterfaceNeverImplemented")
public interface DaoConstants {
    String DEFAULT = "DEFAULT";

    @SuppressWarnings({"PublicInnerClass", "DuplicateStringLiteralInspection"})
    interface Users {
        String USERS_TABLE = "users";
        String USER_ID = "user_id";
        String FIRST_NAME = "first_name";
        String LAST_NAME = "last_name";
        String PASSWORD_HASH = "password_hash";
        String EMAIL = "email";
        String EMAIL_VALIDATED = "email_validated";
        String EMAIL_VALIDATION_TOKEN = "email_validation_token";
    }

    @SuppressWarnings("PublicInnerClass")
    interface AuthTokens {
        String AUTH_TOKENS_TABLE = "auth_tokens";
        String TOKEN_ID = "token_id";
    }

    @SuppressWarnings({"PublicInnerClass", "DuplicateStringLiteralInspection"})
    interface Calendars {
        String CALENDARS_TABLE = "calendars";
        String CALENDAR_ID = "calendar_id";
        String NAME = "name";
        String DESCRIPTION = "description";
        String COLOR = "color";
        String VISIBLE = "visible";
        String DELETED = "deleted";
        String REQUIRED = "required";
        String TIMEZONE = "timezone";
    }

    @SuppressWarnings({"PublicInnerClass", "DuplicateStringLiteralInspection"})
    interface Events {
        String EVENTS_TABLE = "events";
        String EVENT_ID = "event_id";
        String NAME = "name";
        String DESCRIPTION = "description";
        String LOCATION = "location";
        String DELETED = "deleted";
        String COLOR = "color";
        String PARENT_EVENT_ID = "parent_event_id";
        String START_TIME_UTC = "start_time_utc";
        String START_TIME_TZ = "start_time_tz";
        String END_TIME_UTC = "end_time_utc";
        String END_TIME_TZ = "end_time_tz";
        String REC_END_TIME_UTC = "rec_end_time_utc";
        String REC_END_TIME_TZ = "rec_end_time_tz";
        String REC_PERIOD = "rec_period";
        String REC_TYPE_ID = "rec_type_id";
        String SHOW_IN_SERIES = "show_in_series";
    }
}