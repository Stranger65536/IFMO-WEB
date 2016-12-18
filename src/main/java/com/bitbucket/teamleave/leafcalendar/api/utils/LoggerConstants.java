package com.bitbucket.teamleave.leafcalendar.api.utils;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings("InterfaceNeverImplemented")
public interface LoggerConstants {
    @SuppressWarnings("PublicInnerClass")
    interface Users {
        String USER_REGISTRATION_OK = "User registered successfully";
        String USER_REGISTRATION_FAIL = "User registration failed";
        String USER_VALIDATION_OK = "User validation successfully";
        String USER_VALIDATION_FAIL = "User validation failed";
        String USER_LOGIN_OK = "User login successfully";
        String USER_LOGIN_FAIL = "User login failed";
        String USER_UPDATE_PASSWORD_OK = "User password updated successfully";
        String USER_UPDATE_PASSWORD_FAIL = "User update password failed";
        String USER_UPDATE_EMAIL_OK = "User email updated successfully";
        String USER_UPDATE_EMAIL_FAIL = "User update email failed";
        String USER_SEND_EMAIL_OK = "User validation mail send successfully";
        String USER_SEND_EMAIL_FAIL = "User validation mail send failed";
        String USER_UPDATE_TOKEN_OK = "User token updated successfully";
        String USER_UPDATE_TOKEN_FAIL = "User token update failed";
        String USER_GET_INFO_OK = "User info fetched successfully";
        String USER_GET_INFO_FAIL = "User info fetch failed";
        String USER_UPDATE_INFO_OK = "User info updated successfully";
        String USER_UPDATE_INFO_FAIL = "User info update failed";
        String USER_DELETE_OK = "User deleted successfully";
        String USER_DELETE_FAIL = "User delete failed";
    }

    @SuppressWarnings("PublicInnerClass")
    interface Calendars {
        String CALENDAR_CREATION_OK = "Calendar created successfully";
        String CALENDAR_CREATION_FAIL = "Calendar creation failed";
        String CALENDAR_GET_LIST_OK = "Calendars list fetched successfully";
        String CALENDAR_GET_LIST_FAIL = "Calendars list fetch failed";
        String CALENDAR_GET_INFO_OK = "Calendar info fetched successfully";
        String CALENDAR_GET_INFO_FAIL = "Calendar info fetch failed";
        String CALENDAR_UPDATE_INFO_OK = "Calendar info updated successfully";
        String CALENDAR_UPDATE_INFO_FAIL = "Calendar info update failed";
        String CALENDAR_DELETE_OK = "Calendar deleted successfully";
        String CALENDAR_DELETE_FAIL = "Calendar delete failed";
    }

    @SuppressWarnings("PublicInnerClass")
    interface Events {
        String EVENT_CREATION_OK = "Event created successfully";
        String EVENT_CREATION_FAIL = "Event creation failed";
        String EVENTS_GET_LIST_OK = "Event list fetched successfully";
        String EVENTS_GET_LIST_FAIL = "Event list fetch failed";
        String EVENT_GET_INFO_OK = "Event info fetched successfully";
        String EVENT_GET_INFO_FAIL = "Event info fetch failed";
        String EVENT_DELETE_ENTIRE_OK = "Entire event deleted successfully";
        String EVENT_DELETE_ENTIRE_FAIL = "Entire event delete failed";
        String EVENT_UPDATE_ENTIRE_OK = "Entire event info updated successfully";
        String EVENT_UPDATE_ENTIRE_FAIL = "Entire event info update failed";
    }
}
