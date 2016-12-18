package com.bitbucket.teamleave.leafcalendar.api.service.model;

import java.util.List;

/**
 * @author vladislav.trofimov@emc.com
 */
public class CalendarServiceGetAllCalendarsModel {
    private List<String> calendarIds;

    public CalendarServiceGetAllCalendarsModel(final List<String> calendarIds) {
        this.calendarIds = calendarIds;
    }

    public List<String> getCalendarIds() {
        return calendarIds;
    }

    public void setCalendarIds(final List<String> calendarIds) {
        this.calendarIds = calendarIds;
    }
}