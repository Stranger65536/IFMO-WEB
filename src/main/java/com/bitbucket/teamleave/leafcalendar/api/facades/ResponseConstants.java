package com.bitbucket.teamleave.leafcalendar.api.facades;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings("InterfaceNeverImplemented")
public interface ResponseConstants {
    String INVALID_FIELD_FORMAT = "Incorrect field format";

    @SuppressWarnings("PublicInnerClass")
    interface Users {
        String USER_ALREADY_REGISTERED = "User already registered";
        String USER_ALREADY_VALIDATED = "User already validated";
        String USER_NOT_EXISTS = "User does not exist";
        String INVALID_CREDENTIALS = "Invalid credentials";
    }

    @SuppressWarnings("PublicInnerClass")
    interface Calendars {
        String CALENDAR_NOT_EXISTS = "Calendar does not exist";
        String CALENDAR_IS_REQUIRED = "Calendar is required";
    }

    interface Events {
        String EVENT_NOT_EXISTS = "Event does not exist";
    }

    @SuppressWarnings("PublicInnerClass")
    interface Internal {
        String OPERATION_UNAVAILABLE = "Operation unavailable";
        String INTERNAL_EXCEPTION = "Internal exception";
    }
}