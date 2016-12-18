package com.bitbucket.teamleave.leafcalendar.api.transfer.calendars.response;

import com.bitbucket.teamleave.leafcalendar.api.service.model.CalendarServiceGetAllCalendarsModel;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Calendars.CALENDAR_ID;

/**
 * @author vladislav.trofimov@emc.com
 */
public class GetAllCalendarsResponseData {
    @JsonProperty(CALENDAR_ID)
    private List<String> ids;

    public GetAllCalendarsResponseData(final CalendarServiceGetAllCalendarsModel data) {
        this.ids = data.getCalendarIds();
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(final List<String> ids) {
        this.ids = ids;
    }
}