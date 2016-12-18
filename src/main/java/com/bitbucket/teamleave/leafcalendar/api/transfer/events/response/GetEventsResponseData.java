package com.bitbucket.teamleave.leafcalendar.api.transfer.events.response;

import com.bitbucket.teamleave.leafcalendar.api.service.model.EventServiceGetEventsModel;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Events.EVENTS;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings("unused")
public class GetEventsResponseData {
    @JsonProperty(EVENTS)
    private List<GetEventsResponseEntity> events;

    public GetEventsResponseData(final EventServiceGetEventsModel events) {
        this.events = events.getEvents();
    }

    public List<GetEventsResponseEntity> getEvents() {
        return events;
    }

    public void setEvents(final List<GetEventsResponseEntity> events) {
        this.events = events;
    }
}