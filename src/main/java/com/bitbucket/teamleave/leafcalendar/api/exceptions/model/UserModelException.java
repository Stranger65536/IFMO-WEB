package com.bitbucket.teamleave.leafcalendar.api.exceptions.model;

/**
 * @author vladislav.trofimov@emc.com
 */
public class UserModelException extends ModelException {
    public UserModelException(final String field, final String value) {
        super(field, value);
    }
}