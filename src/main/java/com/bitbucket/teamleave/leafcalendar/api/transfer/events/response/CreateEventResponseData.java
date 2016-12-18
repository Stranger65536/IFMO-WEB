package com.bitbucket.teamleave.leafcalendar.api.transfer.events.response;

import com.bitbucket.teamleave.leafcalendar.api.service.model.EventServiceCreateEventModel;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Events.EVENT_ID;

/**
 * @author vladislav.trofimov@emc.com
 */
public class CreateEventResponseData {
    @JsonProperty(EVENT_ID)
    private String id;

    public CreateEventResponseData(final EventServiceCreateEventModel data) {
        this.id = data.getEventId();
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }
}