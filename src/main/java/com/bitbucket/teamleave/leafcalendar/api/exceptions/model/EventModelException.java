package com.bitbucket.teamleave.leafcalendar.api.exceptions.model;

/**
 * @author vladislav.trofimov@emc.com
 */
public class EventModelException extends ModelException {
    public EventModelException(final String field, final String value) {
        super(field, value);
    }
}