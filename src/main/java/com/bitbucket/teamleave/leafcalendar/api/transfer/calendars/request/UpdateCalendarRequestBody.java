package com.bitbucket.teamleave.leafcalendar.api.transfer.calendars.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Calendars.*;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings({"InstanceVariableMayNotBeInitialized", "FieldHasSetterButNoGetter"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateCalendarRequestBody {
    @JsonProperty(NAME)
    private String name;
    @JsonProperty(DESCRIPTION)
    private String description;
    @JsonProperty(COLOR)
    private String color;
    @JsonProperty(VISIBLE)
    private Boolean visible;
    @JsonProperty(TIMEZONE)
    private String timezone;

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

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(final String timezone) {
        this.timezone = timezone;
    }
}