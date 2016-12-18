package com.bitbucket.teamleave.leafcalendar.api.transfer.events.response;

import com.bitbucket.teamleave.leafcalendar.api.service.model.EventServiceGetEventInfoModel;
import com.bitbucket.teamleave.leafcalendar.api.utils.Utils;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Events.*;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings({"InstanceVariableMayNotBeInitialized", "unused"})
public class GetEventInfoResponseData {
    @JsonProperty(RELATED)
    private final String parentEventId;
    @JsonProperty(EVENT_ID)
    private String eventId;
    @JsonProperty(NAME)
    private String name;
    @JsonProperty(DESCRIPTION)
    private String description;
    @JsonProperty(LOCATION)
    private String location;
    @JsonProperty(COLOR)
    private String color;
    @JsonProperty(START_TIME)
    private String startTime;
    @JsonProperty(START_TIME_TZ)
    private String startTimeTZ;
    @JsonProperty(END_TIME)
    private String endTime;
    @JsonProperty(END_TIME_TZ)
    private String endTimeTZ;
    @JsonProperty(REC_END_TIME)
    private String recEndTime;
    @JsonProperty(REC_END_TIME_TZ)
    private String recEndTimeTZ;
    @JsonProperty(REC_PERIOD)
    private Integer recPeriod;
    @JsonProperty(REC_TYPE_ID)
    private Integer recTypeId;

    @SuppressWarnings("AssignmentToNull")
    public GetEventInfoResponseData(final EventServiceGetEventInfoModel model) {
        this.eventId = model.getEventId();
        this.name = model.getName();
        this.description = model.getDescription();
        this.location = model.getLocation();
        this.color = model.getColor();
        this.parentEventId = model.getParentEventId();
        this.startTime = model.getStartTimeUTC() == null ? null :
                Utils.representAsUTCZoned(model.getStartTimeUTC())
                        .withZoneSameInstant(Utils.getOffset(model.getStartTimeTZ()))
                        .toLocalDateTime().toString();
        this.startTimeTZ = model.getStartTimeTZ();
        this.endTime = model.getEndTimeUTC() == null ? null :
                Utils.representAsUTCZoned(model.getEndTimeUTC())
                        .withZoneSameInstant(Utils.getOffset(model.getEndTimeTZ()))
                        .toLocalDateTime().toString();
        this.endTimeTZ = model.getEndTimeTZ();
        this.recEndTime = model.getRecEndTimeUTC() == null ? null :
                Utils.representAsUTCZoned(model.getRecEndTimeUTC())
                        .withZoneSameInstant(Utils.getOffset(model.getRecEndTimeTZ()))
                        .toLocalDateTime().toString();
        this.recEndTimeTZ = model.getRecEndTimeTZ();
        this.recPeriod = model.getRecPeriod();
        this.recTypeId = model.getRecTypeId();
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(final String eventId) {
        this.eventId = eventId;
    }

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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(final String startTime) {
        this.startTime = startTime;
    }

    public String getStartTimeTZ() {
        return startTimeTZ;
    }

    public void setStartTimeTZ(final String startTimeTZ) {
        this.startTimeTZ = startTimeTZ;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(final String endTime) {
        this.endTime = endTime;
    }

    public String getEndTimeTZ() {
        return endTimeTZ;
    }

    public void setEndTimeTZ(final String endTimeTZ) {
        this.endTimeTZ = endTimeTZ;
    }

    public String getRecEndTime() {
        return recEndTime;
    }

    public void setRecEndTime(final String recEndTime) {
        this.recEndTime = recEndTime;
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