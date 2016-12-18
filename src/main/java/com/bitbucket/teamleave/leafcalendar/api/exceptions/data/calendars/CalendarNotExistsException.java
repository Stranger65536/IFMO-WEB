package com.bitbucket.teamleave.leafcalendar.api.exceptions.data.calendars;

/**
 * @author vladislav.trofimov@emc.com
 */
public class CalendarNotExistsException extends Exception {
    public CalendarNotExistsException(final String message) {
        super(message);
    }

    public CalendarNotExistsException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CalendarNotExistsException(final Throwable cause) {
        super(cause);
    }
}