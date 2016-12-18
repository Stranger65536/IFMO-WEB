package com.bitbucket.teamleave.leafcalendar.api.model;

import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.CalendarModelException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.UserModelException;
import com.bitbucket.teamleave.leafcalendar.api.transfer.calendars.request.CreateCalendarRequestBody;
import com.bitbucket.teamleave.leafcalendar.api.transfer.calendars.request.UpdateCalendarRequestBody;

import java.util.regex.Pattern;

import static com.bitbucket.teamleave.leafcalendar.api.dao.DaoConstants.Calendars.*;
import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.NULL;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings({"InstanceVariableMayNotBeInitialized", "FieldHasSetterButNoGetter"})
public class CalendarModel {
    private static final Pattern CALENDAR_ID_PATTERN = Pattern.compile("^[A-F0-9]{32}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^.{1,255}$");
    private static final Pattern DESCRIPTION_PATTERN = Pattern.compile("^.{0,1024}$");
    private static final Pattern COLOR_PATTERN = Pattern.compile("^[0-9a-fA-F]{6}$");
    private static final Pattern TIMEZONE_PATTERN = Pattern.compile("^Z|[+-]\\d{2}|[+-]\\d{4}|[+-]\\d{2}:\\d{2}$");

    private String calendarId;
    private String name;
    private String description;
    private String color;
    private Boolean visible;
    private Boolean deleted;
    private Boolean required;
    private String timezone;
    private String userId;

    public CalendarModel() {
    }

    public CalendarModel(final String userId) throws
            UserModelException {
        this.userId = UserModel.validateUserId(userId);
    }

    public CalendarModel(final String calendarId, final String userId) throws
            UserModelException, CalendarModelException {
        this.calendarId = validateCalendarId(calendarId);
        this.userId = UserModel.validateUserId(userId);
    }

    public static String validateCalendarId(final String id) throws
            CalendarModelException {
        if (id != null && CALENDAR_ID_PATTERN.matcher(id).matches()) {
            return id;
        } else {
            throw new CalendarModelException(CALENDAR_ID, id);
        }
    }

    private static String validateName(final String name) throws
            CalendarModelException {
        final String preparedString;
        if (name != null && NAME_PATTERN.matcher(preparedString = name.trim()).matches()) {
            return preparedString;
        } else {
            throw new CalendarModelException(NAME, name);
        }
    }

    @SuppressWarnings("ReturnOfNull")
    private static String validateDescription(final String description) throws
            CalendarModelException {
        final String preparedString;
        if (description == null) {
            return null;
        } else if (DESCRIPTION_PATTERN.matcher(preparedString = description.trim()).matches()) {
            return preparedString;
        } else {
            throw new CalendarModelException(DESCRIPTION, description);
        }
    }

    private static String validateColor(final String color) throws
            CalendarModelException {
        if (color != null && COLOR_PATTERN.matcher(color).matches()) {
            return color;
        } else {
            throw new CalendarModelException(COLOR, color);
        }
    }

    @SuppressWarnings("ReturnOfNull")
    private static String validateTimezone(final String timezone) throws
            CalendarModelException {
        if (timezone == null) {
            return null;
        } else if (TIMEZONE_PATTERN.matcher(timezone).matches()) {
            return timezone;
        } else {
            throw new CalendarModelException(TIMEZONE, timezone);
        }
    }

    private static Boolean validateVisible(final Boolean visible) throws
            CalendarModelException {
        if (visible != null) {
            return visible;
        } else {
            throw new CalendarModelException(VISIBLE, NULL);
        }
    }

    private static Boolean validateDeleted(final Boolean deleted) throws
            CalendarModelException {
        if (deleted != null) {
            return deleted;
        } else {
            throw new CalendarModelException(DELETED, NULL);
        }
    }

    private static Boolean validateRequired(final Boolean required) throws
            CalendarModelException {
        if (required != null) {
            return required;
        } else {
            throw new CalendarModelException(REQUIRED, NULL);
        }
    }

    public void populateWithCreateInfo(
            final String userId,
            final CreateCalendarRequestBody calendarData) throws
            CalendarModelException, UserModelException {
        this.setUserId(userId);
        this.setName(calendarData.getName());
        this.setDescription(calendarData.getDescription());
        if (calendarData.getColor() != null) {
            this.setColor(calendarData.getColor());
        }
        if (calendarData.getVisible() != null) {
            this.setVisible(calendarData.getVisible());
        }
        if (calendarData.getTimezone() != null) {
            this.setTimezone(calendarData.getTimezone());
        }
    }

    public void populateWithUpdateInfo(final UpdateCalendarRequestBody data) throws
            CalendarModelException {
        this.setName(data.getName());
        this.setDescription(data.getDescription());
        this.setColor(data.getColor());
        this.setVisible(data.isVisible());
        this.setTimezone(data.getTimezone());
    }

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(final String calendarId) throws
            CalendarModelException {
        this.calendarId = validateCalendarId(calendarId);
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) throws
            CalendarModelException {
        this.name = validateName(name);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) throws
            CalendarModelException {
        this.description = validateDescription(description);
    }

    public String getColor() {
        return color;
    }

    public void setColor(final String color) throws
            CalendarModelException {
        this.color = validateColor(color);
    }

    public Boolean isVisible() {
        return visible;
    }

    public void setVisible(final Boolean visible) throws
            CalendarModelException {
        this.visible = validateVisible(visible);
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(final Boolean deleted) throws
            CalendarModelException {
        this.deleted = validateDeleted(deleted);
    }

    public void delete() throws
            CalendarModelException {
        this.setDeleted(true);
    }

    public Boolean isRequired() {
        return required;
    }

    public void setRequired(final Boolean required) throws
            CalendarModelException {
        this.required = validateRequired(required);
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(final String timezone) throws
            CalendarModelException {
        this.timezone = validateTimezone(timezone);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) throws
            UserModelException {
        this.userId = UserModel.validateUserId(userId);
    }
}