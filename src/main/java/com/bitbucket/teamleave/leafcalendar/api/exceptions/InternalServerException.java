package com.bitbucket.teamleave.leafcalendar.api.exceptions;

/**
 * @author vladislav.trofimov@emc.com
 */
public class InternalServerException extends Exception {
    public InternalServerException(final String message) {
        super(message);
    }

    public InternalServerException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InternalServerException(final Throwable cause) {
        super(cause);
    }
}