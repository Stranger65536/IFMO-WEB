package com.bitbucket.teamleave.leafcalendar.api.exceptions.model;

/**
 * @author vladislav.trofimov@emc.com
 */
public class CalendarModelException extends ModelException {
    public CalendarModelException(final String field, final String value) {
        super(field, value);
    }
}