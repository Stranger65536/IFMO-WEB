package com.bitbucket.teamleave.leafcalendar.api.service.model;

import com.bitbucket.teamleave.leafcalendar.api.model.CalendarModel;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings("FieldHasSetterButNoGetter")
public class CalendarServiceGetCalendarInfoModel {
    private String name;
    private String description;
    private String color;
    private Boolean visible;
    private Boolean deleted;
    private Boolean required;
    private String timezone;

    public CalendarServiceGetCalendarInfoModel(final CalendarModel model) {
        this.name = model.getName();
        this.description = model.getDescription();
        this.color = model.getColor();
        this.visible = model.isVisible();
        this.deleted = model.isDeleted();
        this.required = model.isRequired();
        this.timezone = model.getTimezone();
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

    public String getColor() {
        return color;
    }

    public void setColor(final String color) {
        this.color = color;
    }

    public Boolean isVisible() {
        return visible;
    }

    public void setVisible(final Boolean visible) {
        this.visible = visible;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(final Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(final Boolean required) {
        this.required = required;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(final String timezone) {
        this.timezone = timezone;
    }
}