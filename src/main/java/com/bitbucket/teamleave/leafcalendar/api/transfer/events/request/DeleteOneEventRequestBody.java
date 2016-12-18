package com.bitbucket.teamleave.leafcalendar.api.transfer.events.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Events.END_TIME;
import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Events.START_TIME;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings({"InstanceVariableMayNotBeInitialized", "unused"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeleteOneEventRequestBody {
    @JsonProperty(START_TIME)
    private String startTime;
    @JsonProperty(END_TIME)
    private String endTime;

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