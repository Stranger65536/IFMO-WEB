package com.bitbucket.teamleave.leafcalendar.api.service;

import com.bitbucket.teamleave.leafcalendar.api.dao.EventDao;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.InternalServerException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.data.calendars.CalendarNotExistsException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.data.events.EventNotExistsException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.data.users.UserNotExistsException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.AuthTokenModelException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.CalendarModelException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.EventModelException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.UserModelException;
import com.bitbucket.teamleave.leafcalendar.api.model.EventModel;
import com.bitbucket.teamleave.leafcalendar.api.model.RecurrenceType;
import com.bitbucket.teamleave.leafcalendar.api.service.model.CalendarServiceGetCalendarInfoModel;
import com.bitbucket.teamleave.leafcalendar.api.service.model.EventServiceCreateEventModel;
import com.bitbucket.teamleave.leafcalendar.api.service.model.EventServiceGetEventInfoModel;
import com.bitbucket.teamleave.leafcalendar.api.service.model.EventServiceGetEventsModel;
import com.bitbucket.teamleave.leafcalendar.api.transfer.events.request.CreateEventRequestBody;
import com.bitbucket.teamleave.leafcalendar.api.transfer.events.request.UpdateEntireEventRequestBody;
import com.bitbucket.teamleave.leafcalendar.api.transfer.events.response.GetEventsResponseEntity;
import com.bitbucket.teamleave.leafcalendar.api.utils.DateDiff;
import com.bitbucket.teamleave.leafcalendar.api.utils.Utils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static com.bitbucket.teamleave.leafcalendar.api.utils.Utils.NOT_DELETED;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings("OverlyCoupledClass")
@Service("EventService")
@Transactional(propagation = Propagation.REQUIRED,
        isolation = Isolation.SERIALIZABLE,
        rollbackFor = Exception.class)
public class EventServiceImpl implements EventService {
    @Autowired
    private EventDao eventDao;
    @Autowired
    private CalendarService calendarService;

    private static EventServiceGetEventsModel processEventsForResponse(
            final CalendarServiceGetCalendarInfoModel calendarModel,
            final EventModel request,
            final Collection<EventModel> singleEvents,
            final Collection<EventModel> recurrentEvents,
            final Collection<EventModel> deletedFromSequenceEvents,
            final Collection<EventModel> movedFromSequenceEvents) {
        final List<GetEventsResponseEntity> resultList = new LinkedList<>();
        processSingleEventsForResponse(calendarModel, request, resultList, singleEvents);
        processRecurrentEventsForResponse(calendarModel, request, resultList,
                recurrentEvents, deletedFromSequenceEvents, movedFromSequenceEvents);
        return new EventServiceGetEventsModel(resultList);
    }

    private static Map<String, Set<EventModel>> organizeEventsByParent(
            final Collection<EventModel> events) {
        return events.stream().collect(Collectors.groupingBy(
                EventModel::getParentEventId, Collectors.toSet()));
    }

    @SuppressWarnings("ObjectAllocationInLoop")
    private static Map<String, Set<Pair<Timestamp, Timestamp>>> organizeEventsByParentAndTime(
            final Collection<EventModel> events) {
        final Map<String, Set<EventModel>> mapperByParent = organizeEventsByParent(events);
        final Map<String, Set<Pair<Timestamp, Timestamp>>> result = new HashMap<>(events.size());
        for (Entry<String, Set<EventModel>> eventSet : mapperByParent.entrySet()) {
            final Set<Pair<Timestamp, Timestamp>> set = new HashSet<>(eventSet.getValue().size());
            for (EventModel eventModel : eventSet.getValue()) {
                set.add(Pair.of(eventModel.getStartTimeUTC(), eventModel.getEndTimeUTC()));
            }
            result.put(eventSet.getKey(), set);
        }
        return result;
    }

    private static void processSingleEventsForResponse(
            final CalendarServiceGetCalendarInfoModel calendarModel,
            final EventModel request,
            final Collection<GetEventsResponseEntity> result,
            final Collection<EventModel> source) {
        final ZoneOffset calendarTZ = calendarModel.getTimezone() == null ?
                null : Utils.getOffset(calendarModel.getTimezone());
        final ZoneOffset requestTZ = Utils.getOffset(request.getRecEndTimeTZ());
        final LocalDateTime rangeStart = request.getStartTimeUTC().toLocalDateTime();
        final LocalDateTime rangeEnd = request.getEndTimeUTC().toLocalDateTime();
        source.stream().forEach(event -> {
            final GetEventsResponseEntity processedEvent = new GetEventsResponseEntity();
            final Pair<LocalDateTime, LocalDateTime> shiftedEventRange = calendarTZ == null ?
                    applyRequestTimeZone(processedEvent, event, requestTZ) :
                    inheritTimeZoneFromCalendar(processedEvent, event, calendarTZ);
            final LocalDateTime eventStart = shiftedEventRange.getLeft();
            final LocalDateTime eventEnd = shiftedEventRange.getRight();
            if (checkRangeEntry(eventStart, eventEnd, rangeStart, rangeEnd)) {
                processedEvent.populateFromSingleEvent(event, eventStart, eventEnd);
                result.add(processedEvent);
            }
        });
    }

    @SuppressWarnings({"MagicNumber", "ObjectAllocationInLoop", "OverlyLongMethod"})
    private static void processRecurrentEventsForResponse(
            final CalendarServiceGetCalendarInfoModel calendarModel,
            final EventModel request,
            final Collection<GetEventsResponseEntity> result,
            final Collection<EventModel> recurrent,
            final Collection<EventModel> deleted,
            final Collection<EventModel> moved) {
        final ZoneOffset calendarTZ = calendarModel.getTimezone() == null ?
                null : Utils.getOffset(calendarModel.getTimezone());
        final ZoneOffset requestTZ = Utils.getOffset(request.getRecEndTimeTZ());
        final LocalDateTime rangeStart = request.getStartTimeUTC().toLocalDateTime();
        final LocalDateTime rangeEnd = request.getEndTimeUTC().toLocalDateTime();
        final Map<String, Set<Pair<Timestamp, Timestamp>>> deletedMapped =
                organizeEventsByParentAndTime(deleted);
        LocalDateTime currentDay = rangeStart
                .withHour(23).withMinute(59).withSecond(59).withNano(999_999_999);
        LocalDateTime extendedRangeEnd = rangeEnd
                .plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        while (extendedRangeEnd.isAfter(currentDay)) {
            checkAndProcessCurrentDay(result, recurrent, calendarTZ, requestTZ,
                    rangeStart, rangeEnd, deletedMapped, currentDay);
            currentDay = currentDay.plusDays(1);
        }
        processMovedEvents(result, moved, calendarTZ, requestTZ, rangeStart, rangeEnd);
    }

    private static void processMovedEvents(
            final Collection<GetEventsResponseEntity> result,
            final Collection<EventModel> moved,
            final ZoneOffset calendarTZ,
            final ZoneOffset requestTZ,
            final LocalDateTime rangeStart,
            final LocalDateTime rangeEnd) {
        moved.stream()
                .forEach(event -> {
                    final GetEventsResponseEntity processedEvent = new GetEventsResponseEntity();
                    final Pair<LocalDateTime, LocalDateTime> shiftedEventRange = calendarTZ == null ?
                            applyRequestTimeZone(processedEvent, event, requestTZ) :
                            inheritTimeZoneFromCalendar(processedEvent, event, calendarTZ);
                    final LocalDateTime eventStart = shiftedEventRange.getLeft();
                    final LocalDateTime eventEnd = shiftedEventRange.getRight();
                    if (checkRangeEntry(eventStart, eventEnd, rangeStart, rangeEnd)) {
                        processedEvent.populateFromMovedEvent(event, eventStart, eventEnd);
                        result.add(processedEvent);
                    }
                });
    }

    private static void checkAndProcessCurrentDay(
            final Collection<GetEventsResponseEntity> result,
            final Collection<EventModel> recurrent,
            final ZoneOffset calendarTZ,
            final ZoneOffset requestTZ,
            final LocalDateTime rangeStart,
            final LocalDateTime rangeEnd,
            final Map<String, Set<Pair<Timestamp, Timestamp>>> deletedMapped,
            final LocalDateTime currentDay) {
        recurrent.stream()
                .filter(event -> Utils.representAsUTCZoned(event.getStartTimeUTC())
                        .toLocalDateTime().isBefore(currentDay))
                .forEach(event -> {
                    final Set<Pair<Timestamp, Timestamp>> deletedForCurrentRecurrent =
                            deletedMapped.get(event.getEventId());
                    DateDiff dateDiff = new DateDiff(
                            event.getStartTimeUTC().toLocalDateTime(), currentDay);
                    checkAndProcessCurrentDayAsRecurrenceRealization(
                            result, calendarTZ, requestTZ, rangeStart, rangeEnd, currentDay,
                            event, deletedForCurrentRecurrent, dateDiff);
                });
    }

    private static void checkAndProcessCurrentDayAsRecurrenceRealization(
            final Collection<GetEventsResponseEntity> result,
            final ZoneOffset calendarTZ,
            final ZoneOffset requestTZ,
            final LocalDateTime rangeStart,
            final LocalDateTime rangeEnd,
            final LocalDateTime currentDay,
            final EventModel event,
            final Collection<Pair<Timestamp, Timestamp>> deletedForCurrentRecurrent,
            final DateDiff dateDiff) {
        if (dateDiff.isValidRecurrenceRealization(
                RecurrenceType.valueOf(event.getRecTypeId()), event.getRecPeriod())) {
            final LocalDateTime recStart = event.getStartTimeUTC().toLocalDateTime();
            final LocalDateTime recEnd = event.getEndTimeUTC().toLocalDateTime();
            final LocalDateTime recRealizStart = currentDay
                    .withHour(recStart.getHour())
                    .withMinute(recStart.getMinute())
                    .withSecond(recStart.getSecond())
                    .withNano(recStart.getNano());
            final LocalDateTime recRealizEnd = currentDay
                    .withHour(recEnd.getHour())
                    .withMinute(recEnd.getMinute())
                    .withSecond(recEnd.getSecond())
                    .withNano(recEnd.getNano());
            final Pair<Timestamp, Timestamp> recRange =
                    Pair.of(Timestamp.valueOf(recRealizStart),
                            Timestamp.valueOf(recRealizEnd));
            checkAndProcessIfRealizationNotDeleted(
                    result, calendarTZ, requestTZ, rangeStart,
                    rangeEnd, event, deletedForCurrentRecurrent,
                    recRealizStart, recRealizEnd, recRange);
        }
    }

    private static void checkAndProcessIfRealizationNotDeleted(
            final Collection<GetEventsResponseEntity> result,
            final ZoneOffset calendarTZ,
            final ZoneOffset requestTZ,
            final LocalDateTime rangeStart,
            final LocalDateTime rangeEnd,
            final EventModel event,
            final Collection<Pair<Timestamp, Timestamp>> deletedForCurrentRecurrent,
            final LocalDateTime recRealizStart,
            final LocalDateTime recRealizEnd,
            final Pair<Timestamp, Timestamp> recRange) {
        if (deletedForCurrentRecurrent == null || !deletedForCurrentRecurrent.contains(recRange)) {
            if (deletedForCurrentRecurrent != null) {
                deletedForCurrentRecurrent.remove(recRange);
            }
            final GetEventsResponseEntity processedEvent = new GetEventsResponseEntity();
            final Pair<LocalDateTime, LocalDateTime> shiftedEventRange = calendarTZ == null ?
                    applyRequestTimeZone(processedEvent, recRealizStart, recRealizEnd, requestTZ) :
                    inheritTimeZoneFromCalendar(processedEvent, recRealizStart, recRealizEnd, calendarTZ);
            final LocalDateTime recRealizShiftedStart = shiftedEventRange.getLeft();
            final LocalDateTime recRealizShiftedEnd = shiftedEventRange.getRight();
            final LocalDateTime eventRecEnd = event.getRecEndTimeUTC() == null ? null :
                    Utils.convertToZoned(event.getRecEndTimeUTC(),
                            Utils.getOffset(event.getRecEndTimeTZ()))
                            .toLocalDateTime();
            if (checkRecurRangeEntry(recRealizShiftedStart, recRealizShiftedEnd, rangeStart, rangeEnd, eventRecEnd)) {
                processedEvent.populateFromRecurrentEvent(event, recRealizShiftedStart, recRealizShiftedEnd);
                result.add(processedEvent);
            }
        }
    }

    @SuppressWarnings("TypeMayBeWeakened")
    private static boolean checkRangeEntry(
            final LocalDateTime eventStart,
            final LocalDateTime eventEnd,
            final LocalDateTime UTCRangeStart,
            final LocalDateTime UTCRangeEnd) {
        return eventStart.isBefore(UTCRangeEnd) && !eventEnd.isBefore(UTCRangeStart);
    }

    @SuppressWarnings("TypeMayBeWeakened")
    private static boolean checkRecurRangeEntry(
            final LocalDateTime eventStart,
            final LocalDateTime eventEnd,
            final LocalDateTime UTCRangeStart,
            final LocalDateTime UTCRangeEnd,
            final LocalDateTime recEnd) {
        return checkRangeEntry(eventStart, eventEnd, UTCRangeStart, UTCRangeEnd) &&
                (recEnd == null || recEnd.isAfter(eventStart));
    }

    private static Pair<LocalDateTime, LocalDateTime> applyTimeZone(
            final GetEventsResponseEntity processedEvent,
            final EventModel event,
            final ZoneOffset timezone) {
        final LocalDateTime start = Utils.convertToZoned(
                event.getStartTimeUTC(), timezone).toLocalDateTime();
        final LocalDateTime end = Utils.convertToZoned(
                event.getEndTimeUTC(), timezone).toLocalDateTime();
        processedEvent.setStartTime(start.toString());
        processedEvent.setEndTime(end.toString());
        return Pair.of(start, end);
    }

    private static Pair<LocalDateTime, LocalDateTime> applyTimeZone(
            final GetEventsResponseEntity processedEvent,
            final LocalDateTime start,
            final LocalDateTime end,
            final ZoneOffset timezone) {
        final Pair<LocalDateTime, LocalDateTime> result = Pair.of(
                ZonedDateTime.of(start, ZoneOffset.UTC).withZoneSameInstant(timezone).toLocalDateTime(),
                ZonedDateTime.of(end, ZoneOffset.UTC).withZoneSameInstant(timezone).toLocalDateTime());
        processedEvent.setStartTime(result.getLeft().toString());
        processedEvent.setEndTime(result.getRight().toString());
        return result;
    }

    private static Pair<LocalDateTime, LocalDateTime> inheritTimeZoneFromCalendar(
            final GetEventsResponseEntity processedEvent,
            final EventModel event,
            final ZoneOffset calendarTZ) {
        return applyTimeZone(processedEvent, event, calendarTZ);
    }

    private static Pair<LocalDateTime, LocalDateTime> inheritTimeZoneFromCalendar(
            final GetEventsResponseEntity processedEvent,
            final LocalDateTime start,
            final LocalDateTime end,
            final ZoneOffset calendarTZ) {
        return applyTimeZone(processedEvent, start, end, calendarTZ);
    }

    private static Pair<LocalDateTime, LocalDateTime> applyRequestTimeZone(
            final GetEventsResponseEntity processedEvent,
            final EventModel event,
            final ZoneOffset requestTZ) {
        return applyTimeZone(processedEvent, event, requestTZ);
    }

    private static Pair<LocalDateTime, LocalDateTime> applyRequestTimeZone(
            final GetEventsResponseEntity processedEvent,
            final LocalDateTime start,
            final LocalDateTime end,
            final ZoneOffset requestTZ) {
        return applyTimeZone(processedEvent, start, end, requestTZ);
    }

    @Override
    public EventServiceCreateEventModel createEvent(
            final String authToken,
            final String userId,
            final String calendarId,
            final CreateEventRequestBody eventData) throws
            CalendarNotExistsException, UserNotExistsException,
            EventModelException, AuthTokenModelException,
            InternalServerException, CalendarModelException, UserModelException {
        return eventData.getRecTypeId() == null ?
                createSingleEvent(authToken, userId, calendarId, eventData) :
                createRecurrentEvent(authToken, userId, calendarId, eventData);
    }

    @Override
    public EventServiceGetEventsModel getEventsByRangeAndTZ(
            final String authToken,
            final String userId,
            final String calendarId,
            final String rangeStart,
            final String rangeEnd,
            final String requestTimezone) throws
            CalendarModelException, AuthTokenModelException,
            EventModelException, UserNotExistsException,
            CalendarNotExistsException, InternalServerException, UserModelException {
        final CalendarServiceGetCalendarInfoModel calendar =
                calendarService.getCalendarInfo(authToken, userId, calendarId);
        final EventModel request = new EventModel();
        request.populateForGetEventsByRange(calendarId,
                rangeStart, rangeEnd, requestTimezone);
        request.checkValidGetEventsRequest();
        try {
            final List<EventModel> singleEvents =
                    eventDao.getAllSingleEvents(request, NOT_DELETED);
            final List<EventModel> recurrentEvents =
                    eventDao.getAllRecurrentEvents(request, NOT_DELETED);
            final List<EventModel> deletedFromSequenceEvents =
                    eventDao.getAllDeletedRecurrentEvents(request, NOT_DELETED);
            final List<EventModel> movedFromSequenceEvents =
                    eventDao.getAllMovedRecurrentEvents(request, NOT_DELETED);
            return processEventsForResponse(calendar, request, singleEvents,
                    recurrentEvents, deletedFromSequenceEvents, movedFromSequenceEvents);
        } catch (DataAccessException e) {
            throw new InternalServerException(e);
        }
    }

    private EventServiceCreateEventModel createSingleEvent(
            final String authToken,
            final String userId,
            final String calendarId,
            final CreateEventRequestBody eventData) throws
            CalendarModelException, UserNotExistsException,
            InternalServerException, CalendarNotExistsException,
            AuthTokenModelException, EventModelException,
            UserModelException {
        final CalendarServiceGetCalendarInfoModel calendar =
                calendarService.getCalendarInfo(authToken, userId, calendarId);
        final EventModel eventModel = new EventModel();
        eventModel.populateForCreateSingleEvent(calendarId, eventData, calendar);
        eventModel.checkValidSingleEvent();
        try {
            final String eventId = eventDao.createEvent(eventModel);
            return new EventServiceCreateEventModel(eventId);
        } catch (DataIntegrityViolationException e) {
            throw new CalendarNotExistsException(e);
        }
    }

    private EventServiceCreateEventModel createRecurrentEvent(
            final String authToken,
            final String userId,
            final String calendarId,
            final CreateEventRequestBody eventData) throws
            EventModelException, AuthTokenModelException,
            InternalServerException, UserNotExistsException,
            CalendarModelException, CalendarNotExistsException,
            UserModelException {
        final CalendarServiceGetCalendarInfoModel calendar =
                calendarService.getCalendarInfo(authToken, userId, calendarId);
        final EventModel eventModel = new EventModel();
        eventModel.populateForCreateRecurrentEvent(calendarId, eventData, calendar);
        eventModel.checkValidRecurrentEvent();
        try {
            final String eventId = eventDao.createEvent(eventModel);
            return new EventServiceCreateEventModel(eventId);
        } catch (DataIntegrityViolationException e) {
            throw new CalendarNotExistsException(e);
        }
    }

    @Override
    public EventServiceGetEventInfoModel getEventInfo(
            final String authToken,
            final String userId,
            final String calendarId,
            final String eventId) throws
            AuthTokenModelException, CalendarModelException,
            EventModelException, UserNotExistsException,
            CalendarNotExistsException, EventNotExistsException,
            InternalServerException, UserModelException {
        calendarService.getCalendarInfo(authToken, userId, calendarId);
        EventModel eventModel = new EventModel(eventId, calendarId);
        try {
            eventModel = eventDao.getEventByEventAndCalendarId(
                    eventModel, NOT_DELETED);
            return new EventServiceGetEventInfoModel(eventModel);
        } catch (EmptyResultDataAccessException e) {
            throw new EventNotExistsException(e);
        }
    }

    @SuppressWarnings("LawOfDemeter")
    @Override
    public void deleteEntireEventById(
            final String authToken,
            final String userId,
            final String calendarId,
            final String eventId) throws
            EventNotExistsException, InternalServerException,
            CalendarModelException, UserNotExistsException,
            CalendarNotExistsException, AuthTokenModelException,
            EventModelException, UserModelException {
        calendarService.getCalendarInfo(authToken, userId, calendarId);
        EventModel eventModel = new EventModel(eventId, calendarId);
        try {
            eventModel = eventDao.getEventByEventAndCalendarId(eventModel, NOT_DELETED);
            eventModel.delete();
            eventDao.updateEventById(eventModel, NOT_DELETED);
        } catch (EmptyResultDataAccessException e) {
            throw new EventNotExistsException(e);
        }
    }

    @SuppressWarnings("LawOfDemeter")
    @Override
    public void updateEntireEventById(
            final String authToken,
            final String userId,
            final String calendarId,
            final String eventId,
            final UpdateEntireEventRequestBody request) throws
            InternalServerException, CalendarModelException,
            UserNotExistsException, CalendarNotExistsException,
            AuthTokenModelException, EventModelException,
            EventNotExistsException, UserModelException {
        calendarService.getCalendarInfo(authToken, userId, calendarId);
        EventModel eventModel = new EventModel(eventId, calendarId);
        try {
            eventModel = eventDao.getEventByEventAndCalendarId(eventModel, NOT_DELETED);
            eventModel.populateForUpdateEntire(request);
            if (eventModel.getRecTypeId() == null) {
                eventModel.checkValidSingleEvent();
            } else {
                eventModel.checkValidRecurrentEvent();
            }
            eventDao.updateEventById(eventModel, NOT_DELETED);
        } catch (EmptyResultDataAccessException e) {
            throw new EventNotExistsException(e);
        }
    }
}