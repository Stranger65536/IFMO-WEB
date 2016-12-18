package com.bitbucket.teamleave.leafcalendar.api.controller;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings({"InterfaceNeverImplemented", "HardcodedFileSeparator"})
public interface ContextPaths {
    String USERS = "/users";
    String USER_ID = "userId";
    String USER_ID_PATH = "/{userId}";
    String CALENDARS = "/calendars";
    String CALENDAR_ID = "calendarId";
    String CALENDAR_ID_PATH = "/{calendarId}";
    String EVENTS = "/events";
    String EVENT_ID = "eventId";
    String EVENT_ID_PATH = "/{eventId}";
    String UPDATE = "/update";
    String VALIDATE = "/validate";
    String LOGIN = "/login";
    String PASSWORD = "/password";
    String EMAIL = "/email";
    String NOTIFY = "/notify";
    String LOGOUT = "/logout";
    String ONE = "/one";
    String AFTER = "/after";
}