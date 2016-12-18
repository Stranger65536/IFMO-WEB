package com.bitbucket.teamleave.leafcalendar.api.service.model;

import com.bitbucket.teamleave.leafcalendar.api.transfer.events.response.GetEventsResponseEntity;

import java.util.List;

/**
 * @author vladislav.trofimov@emc.com
 */
public class EventServiceGetEventsModel {
    private List<GetEventsResponseEntity> events;

    public EventServiceGetEventsModel(final List<GetEventsResponseEntity> events) {
        this.events = events;
    }

    public List<GetEventsResponseEntity> getEvents() {
        return events;
    }

    public void setEvents(final List<GetEventsResponseEntity> events) {
        this.events = events;
    }
}