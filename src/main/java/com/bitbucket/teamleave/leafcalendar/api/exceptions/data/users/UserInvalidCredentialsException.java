package com.bitbucket.teamleave.leafcalendar.api.exceptions.data.users;

/**
 * @author vladislav.trofimov@emc.com
 */
public class UserInvalidCredentialsException extends Exception {
    public UserInvalidCredentialsException(final String message) {
        super(message);
    }

    public UserInvalidCredentialsException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UserInvalidCredentialsException(final Throwable cause) {
        super(cause);
    }
}