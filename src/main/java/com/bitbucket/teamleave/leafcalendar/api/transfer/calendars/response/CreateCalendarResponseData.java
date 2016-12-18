package com.bitbucket.teamleave.leafcalendar.api.transfer.calendars.response;

import com.bitbucket.teamleave.leafcalendar.api.service.model.CalendarServiceCreateCalendarModel;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Calendars.CALENDAR_ID;

/**
 * @author vladislav.trofimov@emc.com
 */
public class CreateCalendarResponseData {
    @JsonProperty(CALENDAR_ID)
    private String id;

    public CreateCalendarResponseData(final CalendarServiceCreateCalendarModel data) {
        this.id = data.getCalendarId();
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }
}