package com.bitbucket.teamleave.leafcalendar.api.exceptions.data.users;

/**
 * @author vladislav.trofimov@emc.com
 */
public class UserAlreadyValidatedException extends Exception {
    public UserAlreadyValidatedException() {
    }

    public UserAlreadyValidatedException(final String message) {
        super(message);
    }

    public UserAlreadyValidatedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyValidatedException(final Throwable cause) {
        super(cause);
    }
}