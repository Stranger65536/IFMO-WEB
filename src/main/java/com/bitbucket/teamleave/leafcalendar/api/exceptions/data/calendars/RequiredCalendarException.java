package com.bitbucket.teamleave.leafcalendar.api.exceptions.data.calendars;

/**
 * @author vladislav.trofimov@emc.com
 */
public class RequiredCalendarException extends Exception {
    public RequiredCalendarException(final String message) {
        super(message);
    }

    public RequiredCalendarException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public RequiredCalendarException(final Throwable cause) {
        super(cause);
    }
}