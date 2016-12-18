package com.bitbucket.teamleave.leafcalendar.api.exceptions.data.users;

/**
 * @author vladislav.trofimov@emc.com
 */
public class UserNotExistsException extends Exception {
    public UserNotExistsException(final String message) {
        super(message);
    }

    public UserNotExistsException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UserNotExistsException(final Throwable cause) {
        super(cause);
    }
}