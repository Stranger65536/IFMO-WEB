package com.bitbucket.teamleave.leafcalendar.api.model;

import com.bitbucket.teamleave.leafcalendar.api.exceptions.InternalServerException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.CalendarModelException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.EventModelException;
import com.bitbucket.teamleave.leafcalendar.api.service.model.CalendarServiceGetCalendarInfoModel;
import com.bitbucket.teamleave.leafcalendar.api.transfer.events.request.CreateEventRequestBody;
import com.bitbucket.teamleave.leafcalendar.api.transfer.events.request.UpdateEntireEventRequestBody;
import com.bitbucket.teamleave.leafcalendar.api.utils.Utils;
import org.apache.commons.lang3.tuple.Pair;

import java.sql.Timestamp;
import java.time.DateTimeException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

import static com.bitbucket.teamleave.leafcalendar.api.dao.DaoConstants.Events.*;
import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Events.NAME;
import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.NULL;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings({"InstanceVariableMayNotBeInitialized", "OverlyComplexClass"})
public class EventModel {
    private static final Pattern EVENT_ID_PATTERN = Pattern.compile("^[A-F0-9]{32}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^.{1,255}$");
    private static final Pattern DESCRIPTION_PATTERN = Pattern.compile("^.{0,1024}$");
    private static final Pattern LOCATION_PATTERN = Pattern.compile("^.{0,1024}$");
    private static final Pattern COLOR_PATTERN = Pattern.compile("^[0-9a-fA-F]{6}$");
    private static final Pattern TIMEZONE_PATTERN = Pattern.compile("^Z|[+-]\\d{2}|[+-]\\d{4}|[+-]\\d{2}:\\d{2}$");
    private static final String NOT_TOGETHER_TZ = "Start & end time must be set up together";
    private static final String NOT_TOGETHER_REC_TZ = "Recurrence end time can be set up if recurrence end timezone specified";
    private static final String END_BEFORE_START = "Event must be ended after starting";
    private static final String NOT_TOGETHER_REC_INFO = "Recurrence type and period must be set up together";
    private static final String REC_END_BEFORE_END = "Recurrence ends before initial event ends";
    private static final String DELIMITER = "; ";
    private static final int MAX_RECC_PERIOD = 31;

    private String eventId;
    private String name;
    private String description;
    private String location;
    private Boolean deleted;
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
    private Boolean showInSeries;
    private String calendarId;

    public EventModel() {
    }

    public EventModel(
            final String eventId,
            final String calendarId) throws
            EventModelException, CalendarModelException {
        this.eventId = validateEventId(eventId);
        this.calendarId = CalendarModel.validateCalendarId(calendarId);
    }

    public static String validateEventId(final String id) throws
            EventModelException {
        if (id != null && EVENT_ID_PATTERN.matcher(id).matches()) {
            return id;
        } else {
            throw new EventModelException(EVENT_ID, id);
        }
    }

    private static String validateName(final String name) throws
            EventModelException {
        final String preparedString;
        if (name != null && NAME_PATTERN.matcher(
                preparedString = name.trim()).matches()) {
            return preparedString;
        } else {
            throw new EventModelException(NAME, NULL);
        }
    }

    @SuppressWarnings("ReturnOfNull")
    private static String validateDescription(final String description) throws
            EventModelException {
        final String preparedString;
        if (description == null) {
            return null;
        } else if (DESCRIPTION_PATTERN.matcher(
                preparedString = description.trim()).matches()) {
            return preparedString;
        } else {
            throw new EventModelException(DESCRIPTION, description);
        }
    }

    @SuppressWarnings("ReturnOfNull")
    private static String validateLocation(final String location) throws
            EventModelException {
        final String preparedString;
        if (location == null) {
            return null;
        } else if (LOCATION_PATTERN.matcher(
                preparedString = location.trim()).matches()) {
            return preparedString;
        } else {
            throw new EventModelException(LOCATION, location);
        }
    }

    private static Boolean validateDeleted(final Boolean deleted) throws
            EventModelException {
        if (deleted != null) {
            return deleted;
        } else {
            throw new EventModelException(DELETED, NULL);
        }
    }

    private static String validateColor(final String color) throws
            EventModelException {
        if (color != null && COLOR_PATTERN.matcher(color).matches()) {
            return color;
        } else {
            throw new EventModelException(COLOR, color);
        }
    }

    private static String validateParentEventId(final String id) throws
            EventModelException {
        if (id == null || EVENT_ID_PATTERN.matcher(id).matches()) {
            return id;
        } else {
            throw new EventModelException(PARENT_EVENT_ID, id);
        }
    }

    private static Timestamp validateStartTime(final String time) throws
            EventModelException {
        if (time != null) {
            try {
                return Utils.convertToUTCTimestamp(time);
            } catch (DateTimeParseException | IllegalArgumentException ignored) {
                throw new EventModelException(START_TIME_UTC, time);
            }
        } else {
            throw new EventModelException(START_TIME_UTC, NULL);
        }
    }

    private static Timestamp validateEndTime(final String time) throws
            EventModelException {
        if (time != null) {
            try {
                return Utils.convertToUTCTimestamp(time);
            } catch (DateTimeParseException | IllegalArgumentException ignored) {
                throw new EventModelException(END_TIME_UTC, time);
            }
        } else {
            throw new EventModelException(END_TIME_UTC, NULL);
        }
    }

    @SuppressWarnings("ReturnOfNull")
    private static Timestamp validateRecurrenceEndTime(final String time) throws
            EventModelException {
        if (time == null) {
            return null;
        } else {
            try {
                return Utils.convertToUTCTimestamp(time);
            } catch (DateTimeParseException | IllegalArgumentException ignored) {
                throw new EventModelException(REC_END_TIME_UTC, time);
            }
        }
    }

    private static String validateStartTimeTimezone(final String timezone)
            throws EventModelException {
        if (timezone == null || TIMEZONE_PATTERN.matcher(timezone).matches()) {
            return timezone;
        } else {
            throw new EventModelException(START_TIME_TZ, timezone);
        }
    }

    private static String validateEndTimeTimezone(final String timezone)
            throws EventModelException {
        if (timezone == null || TIMEZONE_PATTERN.matcher(timezone).matches()) {
            return timezone;
        } else {
            throw new EventModelException(END_TIME_TZ, timezone);
        }
    }

    private static String validateRecurrenceEndTimezone(final String timezone) throws
            EventModelException {
        if (timezone == null || TIMEZONE_PATTERN.matcher(timezone).matches()) {
            return timezone;
        } else {
            throw new EventModelException(REC_END_TIME_TZ, timezone);
        }
    }

    private static Integer validateRecurrencePeriod(final Integer recPeriod) throws
            EventModelException {
        if (recPeriod == null || recPeriod > 0 && recPeriod < MAX_RECC_PERIOD) {
            return recPeriod;
        } else {
            throw new EventModelException(REC_PERIOD, String.valueOf(recPeriod));
        }
    }

    private static Boolean validateShowInSeries(final Boolean showInSeries) {
        return showInSeries;
    }

    @SuppressWarnings("ReturnOfNull")
    private static Integer validateRecurrenceType(final Integer recTypeId) throws
            EventModelException {
        if (recTypeId == null) {
            return null;
        } else if (RecurrenceType.values().length > recTypeId && recTypeId >= 0) {
            return RecurrenceType.values()[recTypeId].getValue();
        } else {
            throw new EventModelException(REC_TYPE_ID, recTypeId.toString());
        }
    }

    public void checkValidSingleEvent() throws
            InternalServerException, EventModelException {
        checkStartEndTime();
    }

    public void checkValidRecurrentEvent() throws
            InternalServerException, EventModelException {
        final Pair<ZonedDateTime, ZonedDateTime> range = checkStartEndTime();
        checkRecurrencePeriod();
        checkRecurrenceEndTime(range);
    }

    public void checkValidGetEventsRequest() throws
            InternalServerException, EventModelException {
        checkStartEndTime();
        checkSpecifiedTimeZone();
    }

    private void checkSpecifiedTimeZone() throws EventModelException {
        if (recEndTimeTZ == null) {
            throw new EventModelException(REC_END_TIME_TZ, NULL);
        }
    }

    @SuppressWarnings("StringConcatenationMissingWhitespace")
    private void checkRecurrenceEndTime(
            final Pair<ZonedDateTime, ZonedDateTime> range) throws
            EventModelException, InternalServerException {
        if (recEndTimeUTC == null && recEndTimeTZ == null) {
            return;
        }
        if (recEndTimeUTC == null ^ recEndTimeTZ == null) {
            throw new EventModelException(REC_END_TIME_UTC +
                    DELIMITER +
                    REC_END_TIME_TZ,
                    NOT_TOGETHER_REC_TZ);
        }
        final ZoneOffset recEndZoneOffset = Utils.getOffset(recEndTimeTZ);
        try {
            final ZonedDateTime recEndTimeZoned =
                    Utils.representAsZoned(recEndTimeUTC, recEndZoneOffset);
            final ZonedDateTime endTimeZoned = range.getRight();
            if (recEndTimeZoned.isBefore(endTimeZoned)) {
                throw new EventModelException(END_TIME_UTC + DELIMITER +
                        END_TIME_TZ + DELIMITER +
                        REC_END_TIME_UTC + DELIMITER +
                        REC_END_TIME_TZ, REC_END_BEFORE_END);
            }
        } catch (DateTimeException e) {
            throw new InternalServerException(e);
        }
    }

    @SuppressWarnings("StringConcatenationMissingWhitespace")
    private void checkRecurrencePeriod() throws EventModelException {
        if (recTypeId == null || recPeriod == null) {
            throw new EventModelException(
                    REC_TYPE_ID + DELIMITER + REC_PERIOD, NOT_TOGETHER_REC_INFO);
        }
    }

    @SuppressWarnings("StringConcatenationMissingWhitespace")
    private Pair<ZonedDateTime, ZonedDateTime> checkStartEndTime() throws
            EventModelException, InternalServerException {
        if (startTimeUTC == null) {
            throw new EventModelException(START_TIME_UTC, NULL);
        }
        if (endTimeUTC == null) {
            throw new EventModelException(END_TIME_UTC, NULL);
        }
        if (startTimeTZ == null ^ endTimeTZ == null) {
            throw new EventModelException(START_TIME_TZ + DELIMITER +
                    END_TIME_TZ, NOT_TOGETHER_TZ);
        }
        final ZoneOffset startZoneOffset = Utils.getOffset(startTimeTZ);
        final ZoneOffset endZoneOffset = Utils.getOffset(endTimeTZ);
        try {
            final ZonedDateTime startTimeZoned = Utils.representAsZoned(startTimeUTC, startZoneOffset);
            final ZonedDateTime endTimeZoned = Utils.representAsZoned(endTimeUTC, endZoneOffset);
            if (endTimeZoned.isBefore(startTimeZoned)) {
                throw new EventModelException(START_TIME_UTC + DELIMITER +
                        START_TIME_TZ + DELIMITER +
                        END_TIME_UTC + DELIMITER +
                        END_TIME_TZ, END_BEFORE_START);
            }
            return Pair.of(startTimeZoned, endTimeZoned);
        } catch (DateTimeException e) {
            throw new InternalServerException(e);
        }

    }

    public void populateForCreateSingleEvent(
            final String calendarId,
            final CreateEventRequestBody eventData,
            final CalendarServiceGetCalendarInfoModel calendar) throws
            EventModelException, CalendarModelException {
        this.setName(eventData.getName());
        this.setDescription(eventData.getDescription());
        this.setLocation(eventData.getLocation());
        this.setColor(eventData.getColor() == null ?
                calendar.getColor() :
                eventData.getColor());
        this.setStartTimeUTCFromString(eventData.getStartTimeUTC());
        this.setStartTimeTZ(eventData.getStartTimeTZ());
        this.setEndTimeUTCFromString(eventData.getEndTimeUTC());
        this.setEndTimeTZ(eventData.getEndTimeTZ());
        this.setCalendarId(calendarId);
    }

    public void populateForCreateRecurrentEvent(
            final String calendarId,
            final CreateEventRequestBody eventData,
            final CalendarServiceGetCalendarInfoModel calendar) throws
            EventModelException, CalendarModelException {
        populateForCreateSingleEvent(calendarId, eventData, calendar);
        this.setRecEndTimeUTCFromString(eventData.getRecEndTimeUTC());
        this.setRecEndTimeTZ(eventData.getRecEndTimeTZ());
        this.setRecPeriod(eventData.getRecPeriod());
        this.setRecTypeId(eventData.getRecTypeId());
    }

    public void populateForGetEventsByRange(
            final String calendarId,
            final String rangeStart,
            final String rangeEnd,
            final String timezone) throws
            CalendarModelException, EventModelException {
        this.setCalendarId(calendarId);
        this.setStartTimeUTCFromString(rangeStart);
        this.setEndTimeUTCFromString(rangeEnd);
        this.setRecEndTimeTZ(timezone);
    }

    public void populateForUpdateEntire(
            final UpdateEntireEventRequestBody request) throws EventModelException {
        this.setName(request.getName());
        this.setDescription(request.getDescription());
        this.setLocation(request.getLocation());
        this.setColor(request.getColor());
        this.setStartTimeUTCFromString(request.getStartTimeUTC());
        this.setStartTimeTZ(request.getStartTimeTZ());
        this.setEndTimeUTCFromString(request.getEndTimeUTC());
        this.setEndTimeTZ(request.getEndTimeTZ());
        this.setRecEndTimeUTCFromString(request.getRecEndTimeUTC());
        this.setRecEndTimeTZ(request.getRecEndTimeTZ());
        this.setRecPeriod(request.getRecPeriod());
        this.setRecTypeId(request.getRecTypeId());
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(final String eventId) throws
            EventModelException {
        this.eventId = validateEventId(eventId);
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) throws
            EventModelException {
        this.name = validateName(name);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) throws
            EventModelException {
        this.description = validateDescription(description);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(final String location) throws
            EventModelException {
        this.location = validateLocation(location);
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(final Boolean deleted) throws
            EventModelException {
        this.deleted = validateDeleted(deleted);
    }

    public void delete() {
        this.deleted = true;
    }

    public String getColor() {
        return color;
    }

    public void setColor(final String color) throws
            EventModelException {
        this.color = validateColor(color);
    }

    public String getParentEventId() {
        return parentEventId;
    }

    public void setParentEventId(final String parentEventId) throws
            EventModelException {
        this.parentEventId = validateParentEventId(parentEventId);
    }

    public Timestamp getStartTimeUTC() {
        return startTimeUTC;
    }

    public void setStartTimeUTC(final Timestamp startTimeUTC) {
        this.startTimeUTC = startTimeUTC;
    }

    public void setStartTimeUTCFromString(final String startTimeUTC) throws
            EventModelException {
        this.startTimeUTC = validateStartTime(startTimeUTC);
    }

    public String getStartTimeTZ() {
        return startTimeTZ;
    }

    public void setStartTimeTZ(final String startTimeTZ) throws
            EventModelException {
        this.startTimeTZ = validateStartTimeTimezone(startTimeTZ);
    }

    public Timestamp getEndTimeUTC() {
        return endTimeUTC;
    }

    public void setEndTimeUTC(final Timestamp endTimeUTC) {
        this.endTimeUTC = endTimeUTC;
    }

    public void setEndTimeUTCFromString(final String endTimeUTC) throws
            EventModelException {
        this.endTimeUTC = validateEndTime(endTimeUTC);
    }

    public String getEndTimeTZ() {
        return endTimeTZ;
    }

    public void setEndTimeTZ(final String endTimeTZ) throws
            EventModelException {
        this.endTimeTZ = validateEndTimeTimezone(endTimeTZ);
    }

    public Timestamp getRecEndTimeUTC() {
        return recEndTimeUTC;
    }

    public void setRecEndTimeUTC(final Timestamp recEndTimeUTC) {
        this.recEndTimeUTC = recEndTimeUTC;
    }

    public void setRecEndTimeUTCFromString(final String recEndTimeUTC) throws
            EventModelException {
        this.recEndTimeUTC = validateRecurrenceEndTime(recEndTimeUTC);
    }

    public String getRecEndTimeTZ() {
        return recEndTimeTZ;
    }

    public void setRecEndTimeTZ(final String recEndTimeTZ) throws
            EventModelException {
        this.recEndTimeTZ = validateRecurrenceEndTimezone(recEndTimeTZ);
    }

    public Integer getRecPeriod() {
        return recPeriod;
    }

    public void setRecPeriod(final Integer recPeriod) throws
            EventModelException {
        this.recPeriod = validateRecurrencePeriod(recPeriod);
    }

    public Integer getRecTypeId() {
        return recTypeId;
    }

    public void setRecTypeId(final Integer recTypeId) throws
            EventModelException {
        this.recTypeId = validateRecurrenceType(recTypeId);
    }

    public Boolean getShowInSeries() {
        return showInSeries;
    }

    public void setShowInSeries(final Boolean showInSeries) {
        this.showInSeries = validateShowInSeries(showInSeries);
    }

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(final String calendarId) throws
            CalendarModelException {
        this.calendarId = CalendarModel.validateCalendarId(calendarId);
    }

    @SuppressWarnings("NonFinalFieldReferenceInEquals")
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EventModel)) {
            return false;
        }
        final EventModel model = (EventModel) obj;
        return eventId.equals(model.eventId);

    }

    @SuppressWarnings("NonFinalFieldReferencedInHashCode")
    @Override
    public int hashCode() {
        return eventId.hashCode();
    }
}