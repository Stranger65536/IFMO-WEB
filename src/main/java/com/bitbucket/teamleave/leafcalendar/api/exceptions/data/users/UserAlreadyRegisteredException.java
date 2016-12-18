package com.bitbucket.teamleave.leafcalendar.api.exceptions.data.users;

/**
 * @author vladislav.trofimov@emc.com
 */
public class UserAlreadyRegisteredException extends Exception {
    public UserAlreadyRegisteredException(final String message) {
        super(message);
    }

    public UserAlreadyRegisteredException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyRegisteredException(final Throwable cause) {
        super(cause);
    }
}