package com.bitbucket.teamleave.leafcalendar.api.exceptions.data.events;

/**
 * @author vladislav.trofimov@emc.com
 */
public class EventNotExistsException extends Exception {
    public EventNotExistsException(final String message) {
        super(message);
    }

    public EventNotExistsException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public EventNotExistsException(final Throwable cause) {
        super(cause);
    }
}