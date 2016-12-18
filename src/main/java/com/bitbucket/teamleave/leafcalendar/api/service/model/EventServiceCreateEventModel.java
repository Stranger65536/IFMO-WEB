package com.bitbucket.teamleave.leafcalendar.api.service.model;

/**
 * @author vladislav.trofimov@emc.com
 */
public class EventServiceCreateEventModel {
    private String eventId;

    public EventServiceCreateEventModel(final String eventId) {
        this.eventId = eventId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(final String eventId) {
        this.eventId = eventId;
    }
}