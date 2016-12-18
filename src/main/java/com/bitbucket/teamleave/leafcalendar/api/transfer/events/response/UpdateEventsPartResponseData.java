package com.bitbucket.teamleave.leafcalendar.api.transfer.events.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Events.EVENT_ID;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings("InstanceVariableMayNotBeInitialized")
public class UpdateEventsPartResponseData {
    @JsonProperty(EVENT_ID)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }
}