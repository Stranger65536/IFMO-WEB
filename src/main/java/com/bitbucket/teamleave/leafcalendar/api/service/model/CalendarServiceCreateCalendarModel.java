package com.bitbucket.teamleave.leafcalendar.api.service.model;

/**
 * @author vladislav.trofimov@emc.com
 */
public class CalendarServiceCreateCalendarModel {
    private String calendarId;

    public CalendarServiceCreateCalendarModel(final String calendarId) {
        this.calendarId = calendarId;
    }

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(final String calendarId) {
        this.calendarId = calendarId;
    }
}