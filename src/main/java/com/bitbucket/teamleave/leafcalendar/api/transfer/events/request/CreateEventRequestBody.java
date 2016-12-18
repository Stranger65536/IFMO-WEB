package com.bitbucket.teamleave.leafcalendar.api.transfer.events.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Events.*;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings({"InstanceVariableMayNotBeInitialized", "unused"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateEventRequestBody {
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
    @JsonProperty(REC_END_TIME)
    private String recEndTimeUTC;
    @JsonProperty(REC_END_TIME_TZ)
    private String recEndTimeTZ;
    @JsonProperty(REC_PERIOD)
    private Integer recPeriod;
    @JsonProperty(REC_TYPE_ID)
    private Integer recTypeId;

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

    public String getRecEndTimeUTC() {
        return recEndTimeUTC;
    }

    public void setRecEndTimeUTC(final String recEndTimeUTC) {
        this.recEndTimeUTC = recEndTimeUTC;
    }

    public String getRecEndTimeTZ() {
        return recEndTimeTZ;
    }

    public void setRecEndTimeTZ(final String recEndTimeTZ) {
        this.recEndTimeTZ = recEndTimeTZ;
    }

    public Integer getRecPeriod() {
        return recPeriod;
    }

    public void setRecPeriod(final Integer recPeriod) {
        this.recPeriod = recPeriod;
    }

    public Integer getRecTypeId() {
        return recTypeId;
    }

    public void setRecTypeId(final Integer recTypeId) {
        this.recTypeId = recTypeId;
    }
}