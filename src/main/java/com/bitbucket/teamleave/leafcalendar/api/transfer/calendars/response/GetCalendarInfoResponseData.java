package com.bitbucket.teamleave.leafcalendar.api.transfer.calendars.response;

import com.bitbucket.teamleave.leafcalendar.api.service.model.CalendarServiceGetCalendarInfoModel;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Calendars.*;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings({"FieldHasSetterButNoGetter", "unused"})
public class GetCalendarInfoResponseData {
    @JsonProperty(NAME)
    private String name;
    @JsonProperty(DESCRIPTION)
    private String description;
    @JsonProperty(COLOR)
    private String color;
    @JsonProperty(VISIBLE)
    private Boolean visible;
    @JsonProperty(REQUIRED)
    private Boolean required;
    @JsonProperty(TIMEZONE)
    private String timezone;

    public GetCalendarInfoResponseData(final CalendarServiceGetCalendarInfoModel data) {
        this.name = data.getName();
        this.description = data.getDescription();
        this.color = data.getColor();
        this.visible = data.isVisible();
        this.timezone = data.getTimezone();
        this.required = data.getRequired();
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

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(final String timezone) {
        this.timezone = timezone;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(final Boolean visible) {
        this.visible = visible;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(final Boolean required) {
        this.required = required;
    }
}