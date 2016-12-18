package com.bitbucket.teamleave.leafcalendar.api.transfer.events.response;

import com.bitbucket.teamleave.leafcalendar.api.model.EventModel;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Events.*;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings({"InstanceVariableMayNotBeInitialized", "unused"})
public class GetEventsResponseEntity {
    @JsonProperty(EVENT_ID)
    private String eventId;
    @JsonProperty(RELATED)
    private String relation;
    @JsonProperty(START_TIME)
    private String startTime;
    @JsonProperty(END_TIME)
    private String endTime;

    @SuppressWarnings("TypeMayBeWeakened")
    public void populateFromSingleEvent(
            final EventModel eventModel,
            final LocalDateTime start,
            final LocalDateTime end) {
        this.eventId = eventModel.getEventId();
        this.relation = RELATED_SELF;
        this.startTime = start.toString();
        this.endTime = end.toString();
    }

    @SuppressWarnings("TypeMayBeWeakened")
    public void populateFromRecurrentEvent(
            final EventModel eventModel,
            final LocalDateTime start,
            final LocalDateTime end) {
        this.relation = eventModel.getEventId();
        this.startTime = start.toString();
        this.endTime = end.toString();
    }

    @SuppressWarnings("TypeMayBeWeakened")
    public void populateFromMovedEvent(
            final EventModel eventModel,
            final LocalDateTime start,
            final LocalDateTime end) {
        this.eventId = eventModel.getEventId();
        this.relation = eventModel.getParentEventId();
        this.startTime = start.toString();
        this.endTime = end.toString();
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(final String eventId) {
        this.eventId = eventId;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(final String relation) {
        this.relation = relation;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(final String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(final String endTime) {
        this.endTime = endTime;
    }
}