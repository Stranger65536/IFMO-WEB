package com.bitbucket.teamleave.leafcalendar.api.service.model;

import com.bitbucket.teamleave.leafcalendar.api.model.EventModel;

import java.sql.Timestamp;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings({"InstanceVariableMayNotBeInitialized", "OverlyComplexClass"})
public class EventServiceGetEventInfoModel {
    private String eventId;
    private String name;
    private String description;
    private String location;
    private String color;
    private String parentEventId;
    private Timestamp startTimeUTC;
    private String startTimeTZ;
    private Timestamp endTimeUTC;
    private String endTimeTZ;
    private Timestamp recEndTimeUTC;
    private String recEndTimeTZ;
    private Integer recPeriod;
    private Integer recTypeId;

    public EventServiceGetEventInfoModel(final EventModel model) {
        this.eventId = model.getEventId();
        this.name = model.getName();
        this.description = model.getDescription();
        this.location = model.getLocation();
        this.color = model.getColor();
        this.parentEventId = model.getParentEventId();
        this.startTimeUTC = model.getStartTimeUTC();
        this.startTimeTZ = model.getStartTimeTZ();
        this.endTimeUTC = model.getEndTimeUTC();
        this.endTimeTZ = model.getEndTimeTZ();
        this.recEndTimeUTC = model.getRecEndTimeUTC();
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

    public String getParentEventId() {
        return parentEventId;
    }

    public void setParentEventId(final String parentEventId) {
        this.parentEventId = parentEventId;
    }

    public Timestamp getStartTimeUTC() {
        return startTimeUTC;
    }

    public void setStartTimeUTC(final Timestamp startTimeUTC) {
        this.startTimeUTC = startTimeUTC;
    }

    public String getStartTimeTZ() {
        return startTimeTZ;
    }

    public void setStartTimeTZ(final String startTimeTZ) {
        this.startTimeTZ = startTimeTZ;
    }

    public Timestamp getEndTimeUTC() {
        return endTimeUTC;
    }

    public void setEndTimeUTC(final Timestamp endTimeUTC) {
        this.endTimeUTC = endTimeUTC;
    }

    public String getEndTimeTZ() {
        return endTimeTZ;
    }

    public void setEndTimeTZ(final String endTimeTZ) {
        this.endTimeTZ = endTimeTZ;
    }

    public Timestamp getRecEndTimeUTC() {
        return recEndTimeUTC;
    }

    public void setRecEndTimeUTC(final Timestamp recEndTimeUTC) {
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