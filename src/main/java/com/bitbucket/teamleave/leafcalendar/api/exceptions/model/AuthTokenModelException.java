package com.bitbucket.teamleave.leafcalendar.api.exceptions.model;

/**
 * @author vladislav.trofimov@emc.com
 */
public class AuthTokenModelException extends ModelException {
    public AuthTokenModelException(final String field, final String value) {
        super(field, value);
    }
}