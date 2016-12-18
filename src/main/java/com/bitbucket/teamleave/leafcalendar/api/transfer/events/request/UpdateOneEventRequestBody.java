package com.bitbucket.teamleave.leafcalendar.api.transfer.events.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Events.*;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings({"InstanceVariableMayNotBeInitialized", "unused"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateOneEventRequestBody {
    @JsonProperty(NAME)
    private String name;
    @JsonProperty(DESCRIPTION)
    private String description;
    @JsonProperty(LOCATION)
    private String location;
    @JsonProperty(COLOR)
    private String color;
    @JsonProperty(START_TIME)
    private String startTimeUTC;
    @JsonProperty(START_TIME_TZ)
    private String startTimeTZ;
    @JsonProperty(END_TIME)
    private String endTimeUTC;
    @JsonProperty(END_TIME_TZ)
    private String endTimeTZ;
    @JsonProperty(ORIGINAL_START_TIME)
    private String originalStartTime;
    @JsonProperty(ORIGINAL_END_TIME)
    private String originalEndTime;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

    public String getColor() {
        return color;
    }

    public void setColor(final String color) {
        this.color = color;
    }

    public String getStartTimeUTC() {
        return startTimeUTC;
    }

    public void setStartTimeUTC(final String startTimeUTC) {
        this.startTimeUTC = startTimeUTC;
    }

    public String getStartTimeTZ() {
        return startTimeTZ;
    }

    public void setStartTimeTZ(final String startTimeTZ) {
        this.startTimeTZ = startTimeTZ;
    }

    public String getEndTimeUTC() {
        return endTimeUTC;
    }

    public void setEndTimeUTC(final String endTimeUTC) {
        this.endTimeUTC = endTimeUTC;
    }

    public String getEndTimeTZ() {
        return endTimeTZ;
    }

    public void setEndTimeTZ(final String endTimeTZ) {
        this.endTimeTZ = endTimeTZ;
    }

    public String getOriginalStartTime() {
        return originalStartTime;
    }

    public void setOriginalStartTime(final String originalStartTime) {
        this.originalStartTime = originalStartTime;
    }

    public String getOriginalEndTime() {
        return originalEndTime;
    }

    public void setOriginalEndTime(final String originalEndTime) {
        this.originalEndTime = originalEndTime;
    }
}