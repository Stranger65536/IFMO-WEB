package com.bitbucket.teamleave.leafcalendar.api.facades;

import com.bitbucket.teamleave.leafcalendar.api.exceptions.InternalServerException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.data.calendars.CalendarNotExistsException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.data.events.EventNotExistsException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.data.users.UserNotExistsException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.ModelException;
import com.bitbucket.teamleave.leafcalendar.api.service.CalendarService;
import com.bitbucket.teamleave.leafcalendar.api.service.EventService;
import com.bitbucket.teamleave.leafcalendar.api.service.model.EventServiceCreateEventModel;
import com.bitbucket.teamleave.leafcalendar.api.service.model.EventServiceGetEventsModel;
import com.bitbucket.teamleave.leafcalendar.api.transfer.GenericResponseEntity;
import com.bitbucket.teamleave.leafcalendar.api.transfer.events.request.CreateEventRequestBody;
import com.bitbucket.teamleave.leafcalendar.api.transfer.events.request.UpdateEntireEventRequestBody;
import com.bitbucket.teamleave.leafcalendar.api.transfer.events.response.*;
import com.bitbucket.teamleave.leafcalendar.api.utils.Utils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.bitbucket.teamleave.leafcalendar.api.facades.ResponseConstants.Calendars.CALENDAR_NOT_EXISTS;
import static com.bitbucket.teamleave.leafcalendar.api.facades.ResponseConstants.Events.EVENT_NOT_EXISTS;
import static com.bitbucket.teamleave.leafcalendar.api.facades.ResponseConstants.INVALID_FIELD_FORMAT;
import static com.bitbucket.teamleave.leafcalendar.api.facades.ResponseConstants.Internal.INTERNAL_EXCEPTION;
import static com.bitbucket.teamleave.leafcalendar.api.facades.ResponseConstants.Internal.OPERATION_UNAVAILABLE;
import static com.bitbucket.teamleave.leafcalendar.api.facades.ResponseConstants.Users.USER_NOT_EXISTS;
import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Calendars.CALENDAR_ID;
import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Events.*;
import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Users.AUTH_TOKEN;
import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Users.USER_ID;
import static com.bitbucket.teamleave.leafcalendar.api.utils.LoggerConstants.Events.*;

/**
 * @author vladislav.trofimov@emc.com
 */
@Service("EventFacade")
public class EventFacadeImpl implements EventFacade {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventFacadeImpl.class);
    @Autowired
    CalendarService calendarService;
    @Autowired
    EventService eventService;

    @Override
    public GenericResponseEntity<CreateEventResponseData> createEvent(
            final String authToken,
            final String userId,
            final String calendarId,
            final CreateEventRequestBody eventData) {
        final GenericResponseEntity<CreateEventResponseData> response =
                new GenericResponseEntity<>();
        try {
            final EventServiceCreateEventModel creationInfo =
                    eventService.createEvent(authToken, userId, calendarId, eventData);
            response.setData(new CreateEventResponseData(creationInfo));
            LOGGER.info(Utils.wrap(EVENT_CREATION_OK,
                    AUTH_TOKEN, authToken,
                    CALENDAR_ID, calendarId,
                    NAME, eventData.getName(),
                    DESCRIPTION, eventData.getDescription(),
                    LOCATION, eventData.getLocation(),
                    COLOR, eventData.getColor(),
                    START_TIME, eventData.getStartTimeUTC(),
                    START_TIME_TZ, eventData.getStartTimeTZ(),
                    END_TIME, eventData.getEndTimeUTC(),
                    END_TIME_TZ, eventData.getEndTimeTZ(),
                    REC_END_TIME, eventData.getRecEndTimeUTC(),
                    REC_END_TIME_TZ, eventData.getRecEndTimeTZ(),
                    REC_PERIOD, eventData.getRecPeriod(),
                    REC_TYPE_ID, eventData.getRecTypeId()));
        } catch (ModelException e) {
            response.setStatus(false);
            response.setMessage(Utils.prepareInvalidFieldMessage(e));
            LOGGER.warn(Utils.wrap(EVENT_CREATION_FAIL,
                    INVALID_FIELD_FORMAT, Utils.prepareInvalidFieldMessage(e),
                    AUTH_TOKEN, authToken,
                    CALENDAR_ID, calendarId,
                    NAME, eventData.getName(),
                    DESCRIPTION, eventData.getDescription(),
                    LOCATION, eventData.getLocation(),
                    COLOR, eventData.getColor(),
                    START_TIME, eventData.getStartTimeUTC(),
                    START_TIME_TZ, eventData.getStartTimeTZ(),
                    END_TIME, eventData.getEndTimeUTC(),
                    END_TIME_TZ, eventData.getEndTimeTZ(),
                    REC_END_TIME, eventData.getRecEndTimeUTC(),
                    REC_END_TIME_TZ, eventData.getRecEndTimeTZ(),
                    REC_PERIOD, eventData.getRecPeriod(),
                    REC_TYPE_ID, eventData.getRecTypeId()));
        } catch (UserNotExistsException ignored) {
            response.setStatus(false);
            response.setMessage(USER_NOT_EXISTS);
            LOGGER.warn(Utils.wrap(EVENT_CREATION_FAIL,
                    USER_NOT_EXISTS, "",
                    AUTH_TOKEN, authToken,
                    CALENDAR_ID, calendarId,
                    NAME, eventData.getName(),
                    DESCRIPTION, eventData.getDescription(),
                    LOCATION, eventData.getLocation(),
                    COLOR, eventData.getColor(),
                    START_TIME, eventData.getStartTimeUTC(),
                    START_TIME_TZ, eventData.getStartTimeTZ(),
                    END_TIME, eventData.getEndTimeUTC(),
                    END_TIME_TZ, eventData.getEndTimeTZ(),
                    REC_END_TIME, eventData.getRecEndTimeUTC(),
                    REC_END_TIME_TZ, eventData.getRecEndTimeTZ(),
                    REC_PERIOD, eventData.getRecPeriod(),
                    REC_TYPE_ID, eventData.getRecTypeId()));
        } catch (CalendarNotExistsException ignored) {
            response.setStatus(false);
            response.setMessage(CALENDAR_NOT_EXISTS);
            LOGGER.warn(Utils.wrap(EVENT_CREATION_FAIL,
                    CALENDAR_NOT_EXISTS, "",
                    AUTH_TOKEN, authToken,
                    CALENDAR_ID, calendarId,
                    NAME, eventData.getName(),
                    DESCRIPTION, eventData.getDescription(),
                    LOCATION, eventData.getLocation(),
                    COLOR, eventData.getColor(),
                    START_TIME, eventData.getStartTimeUTC(),
                    START_TIME_TZ, eventData.getStartTimeTZ(),
                    END_TIME, eventData.getEndTimeUTC(),
                    END_TIME_TZ, eventData.getEndTimeTZ(),
                    REC_END_TIME, eventData.getRecEndTimeUTC(),
                    REC_END_TIME_TZ, eventData.getRecEndTimeTZ(),
                    REC_PERIOD, eventData.getRecPeriod(),
                    REC_TYPE_ID, eventData.getRecTypeId()));
        } catch (InternalServerException e) {
            response.setStatus(false);
            response.setMessage(OPERATION_UNAVAILABLE);
            LOGGER.error(Utils.wrap(EVENT_CREATION_FAIL,
                    OPERATION_UNAVAILABLE, "",
                    AUTH_TOKEN, authToken,
                    CALENDAR_ID, calendarId,
                    NAME, eventData.getName(),
                    DESCRIPTION, eventData.getDescription(),
                    LOCATION, eventData.getLocation(),
                    COLOR, eventData.getColor(),
                    START_TIME, eventData.getStartTimeUTC(),
                    START_TIME_TZ, eventData.getStartTimeTZ(),
                    END_TIME, eventData.getEndTimeUTC(),
                    END_TIME_TZ, eventData.getEndTimeTZ(),
                    REC_END_TIME, eventData.getRecEndTimeUTC(),
                    REC_END_TIME_TZ, eventData.getRecEndTimeTZ(),
                    REC_PERIOD, eventData.getRecPeriod(),
                    REC_TYPE_ID, eventData.getRecTypeId(),
                    INTERNAL_EXCEPTION, ExceptionUtils.getStackTrace(e)));
        }
        return response;
    }

    @Override
    public GenericResponseEntity<GetEventsResponseData> getEventsByRangeAndTZ(
            final String authToken,
            final String userId,
            final String calendarId,
            final String rangeStart,
            final String rangeEnd,
            final String requestTimezone) {
        final GenericResponseEntity<GetEventsResponseData> response =
                new GenericResponseEntity<>();
        try {
            final EventServiceGetEventsModel events =
                    eventService.getEventsByRangeAndTZ(authToken, userId, calendarId,
                            rangeStart, rangeEnd, requestTimezone);
            response.setData(new GetEventsResponseData(events));
            LOGGER.info(Utils.wrap(EVENTS_GET_LIST_OK,
                    AUTH_TOKEN, authToken,
                    CALENDAR_ID, calendarId,
                    RANGE_START, rangeStart,
                    RANGE_END, rangeEnd,
                    REQUEST_TZ, requestTimezone));
        } catch (ModelException e) {
            response.setStatus(false);
            response.setMessage(Utils.prepareInvalidFieldMessage(e));
            LOGGER.warn(Utils.wrap(EVENTS_GET_LIST_FAIL,
                    INVALID_FIELD_FORMAT, Utils.prepareInvalidFieldMessage(e),
                    AUTH_TOKEN, authToken,
                    CALENDAR_ID, calendarId,
                    RANGE_START, rangeStart,
                    RANGE_END, rangeEnd,
                    REQUEST_TZ, requestTimezone));
        } catch (UserNotExistsException ignored) {
            response.setStatus(false);
            response.setMessage(USER_NOT_EXISTS);
            LOGGER.warn(Utils.wrap(EVENTS_GET_LIST_FAIL,
                    USER_NOT_EXISTS, "",
                    AUTH_TOKEN, authToken,
                    CALENDAR_ID, calendarId,
                    RANGE_START, rangeStart,
                    RANGE_END, rangeEnd,
                    REQUEST_TZ, requestTimezone));
        } catch (CalendarNotExistsException ignored) {
            response.setStatus(false);
            response.setMessage(CALENDAR_NOT_EXISTS);
            LOGGER.warn(Utils.wrap(EVENTS_GET_LIST_FAIL,
                    CALENDAR_NOT_EXISTS, "",
                    AUTH_TOKEN, authToken,
                    CALENDAR_ID, calendarId,
                    RANGE_START, rangeStart,
                    RANGE_END, rangeEnd,
                    REQUEST_TZ, requestTimezone));
        } catch (InternalServerException e) {
            response.setStatus(false);
            response.setMessage(OPERATION_UNAVAILABLE);
            LOGGER.error(Utils.wrap(EVENTS_GET_LIST_FAIL,
                    OPERATION_UNAVAILABLE, "",
                    AUTH_TOKEN, authToken,
                    CALENDAR_ID, calendarId,
                    RANGE_START, rangeStart,
                    RANGE_END, rangeEnd,
                    REQUEST_TZ, requestTimezone,
                    INTERNAL_EXCEPTION, ExceptionUtils.getStackTrace(e)));
        }
        return response;
    }

    @Override
    public GenericResponseEntity<GetEventInfoResponseData> getEventInfo(
            final String authToken,
            final String userId,
            final String calendarId,
            final String eventId) {
        final GenericResponseEntity<GetEventInfoResponseData> response =
                new GenericResponseEntity<>();
        try {
            response.setData(new GetEventInfoResponseData(
                    eventService.getEventInfo(authToken, userId, calendarId, eventId)));
            LOGGER.info(Utils.wrap(EVENT_GET_INFO_OK,
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    CALENDAR_ID, calendarId,
                    EVENT_ID, eventId));
        } catch (ModelException e) {
            response.setStatus(false);
            response.setMessage(Utils.prepareInvalidFieldMessage(e));
            LOGGER.warn(Utils.wrap(EVENT_GET_INFO_FAIL,
                    INVALID_FIELD_FORMAT, Utils.prepareInvalidFieldMessage(e),
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    CALENDAR_ID, calendarId,
                    EVENT_ID, eventId));
        } catch (UserNotExistsException ignored) {
            response.setStatus(false);
            response.setMessage(USER_NOT_EXISTS);
            LOGGER.warn(Utils.wrap(EVENT_GET_INFO_FAIL,
                    USER_NOT_EXISTS, "",
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    CALENDAR_ID, calendarId,
                    EVENT_ID, eventId));
        } catch (CalendarNotExistsException ignored) {
            response.setStatus(false);
            response.setMessage(CALENDAR_NOT_EXISTS);
            LOGGER.warn(Utils.wrap(EVENT_GET_INFO_FAIL,
                    CALENDAR_NOT_EXISTS, "",
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    CALENDAR_ID, calendarId,
                    EVENT_ID, eventId));
        } catch (EventNotExistsException ignored) {
            response.setStatus(false);
            response.setMessage(EVENT_NOT_EXISTS);
            LOGGER.warn(Utils.wrap(EVENT_GET_INFO_FAIL,
                    EVENT_NOT_EXISTS, "",
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    CALENDAR_ID, calendarId,
                    EVENT_ID, eventId));
        } catch (InternalServerException e) {
            response.setStatus(false);
            response.setMessage(OPERATION_UNAVAILABLE);
            LOGGER.error(Utils.wrap(EVENT_GET_INFO_FAIL,
                    OPERATION_UNAVAILABLE, "",
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    CALENDAR_ID, calendarId,
                    EVENT_ID, eventId,
                    INTERNAL_EXCEPTION, ExceptionUtils.getStackTrace(e)));
        }
        return response;
    }

    @Override
    public GenericResponseEntity<DeleteEventResponseData> deleteEntireEventById(
            final String authToken,
            final String userId,
            final String calendarId,
            final String eventId) {
        final GenericResponseEntity<DeleteEventResponseData> response =
                new GenericResponseEntity<>();
        try {
            eventService.deleteEntireEventById(authToken, userId, calendarId, eventId);
            LOGGER.info(Utils.wrap(EVENT_DELETE_ENTIRE_OK,
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    CALENDAR_ID, calendarId,
                    EVENT_ID, eventId));
        } catch (ModelException e) {
            response.setStatus(false);
            response.setMessage(Utils.prepareInvalidFieldMessage(e));
            LOGGER.warn(Utils.wrap(EVENT_DELETE_ENTIRE_FAIL,
                    INVALID_FIELD_FORMAT, Utils.prepareInvalidFieldMessage(e),
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    CALENDAR_ID, calendarId,
                    EVENT_ID, eventId));
        } catch (UserNotExistsException ignored) {
            response.setStatus(false);
            response.setMessage(USER_NOT_EXISTS);
            LOGGER.warn(Utils.wrap(EVENT_DELETE_ENTIRE_FAIL,
                    USER_NOT_EXISTS, "",
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    CALENDAR_ID, calendarId,
                    EVENT_ID, eventId));
        } catch (CalendarNotExistsException ignored) {
            response.setStatus(false);
            response.setMessage(CALENDAR_NOT_EXISTS);
            LOGGER.warn(Utils.wrap(EVENT_DELETE_ENTIRE_FAIL,
                    CALENDAR_NOT_EXISTS, "",
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    CALENDAR_ID, calendarId,
                    EVENT_ID, eventId));
        } catch (EventNotExistsException ignored) {
            response.setStatus(false);
            response.setMessage(EVENT_NOT_EXISTS);
            LOGGER.warn(Utils.wrap(EVENT_DELETE_ENTIRE_FAIL,
                    EVENT_NOT_EXISTS, "",
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    CALENDAR_ID, calendarId,
                    EVENT_ID, eventId));
        } catch (InternalServerException e) {
            response.setStatus(false);
            response.setMessage(OPERATION_UNAVAILABLE);
            LOGGER.error(Utils.wrap(EVENT_DELETE_ENTIRE_FAIL,
                    OPERATION_UNAVAILABLE, "",
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    CALENDAR_ID, calendarId,
                    EVENT_ID, eventId,
                    INTERNAL_EXCEPTION, ExceptionUtils.getStackTrace(e)));
        }
        return response;
    }

    @Override
    public GenericResponseEntity<UpdateEntireEventResponseData> updateEntireEventById(
            final String authToken,
            final String userId,
            final String calendarId,
            final String eventId,
            final UpdateEntireEventRequestBody request) {
        final GenericResponseEntity<UpdateEntireEventResponseData> response =
                new GenericResponseEntity<>();
        try {
            eventService.updateEntireEventById(authToken, userId,
                    calendarId, eventId, request);
            LOGGER.info(Utils.wrap(EVENT_UPDATE_ENTIRE_OK,
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    CALENDAR_ID, calendarId,
                    EVENT_ID, eventId,
                    NAME, request.getName(),
                    DESCRIPTION, request.getDescription(),
                    LOCATION, request.getLocation(),
                    COLOR, request.getColor(),
                    START_TIME, request.getStartTimeUTC(),
                    START_TIME_TZ, request.getStartTimeTZ(),
                    END_TIME, request.getEndTimeUTC(),
                    END_TIME_TZ, request.getEndTimeTZ(),
                    REC_END_TIME, request.getRecEndTimeUTC(),
                    REC_END_TIME_TZ, request.getRecEndTimeTZ(),
                    REC_PERIOD, request.getRecPeriod(),
                    REC_TYPE_ID, request.getRecTypeId()));
        } catch (ModelException e) {
            response.setStatus(false);
            response.setMessage(Utils.prepareInvalidFieldMessage(e));
            LOGGER.info(Utils.wrap(EVENT_UPDATE_ENTIRE_FAIL,
                    INVALID_FIELD_FORMAT, Utils.prepareInvalidFieldMessage(e),
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    CALENDAR_ID, calendarId,
                    EVENT_ID, eventId,
                    NAME, request.getName(),
                    DESCRIPTION, request.getDescription(),
                    LOCATION, request.getLocation(),
                    COLOR, request.getColor(),
                    START_TIME, request.getStartTimeUTC(),
                    START_TIME_TZ, request.getStartTimeTZ(),
                    END_TIME, request.getEndTimeUTC(),
                    END_TIME_TZ, request.getEndTimeTZ(),
                    REC_END_TIME, request.getRecEndTimeUTC(),
                    REC_END_TIME_TZ, request.getRecEndTimeTZ(),
                    REC_PERIOD, request.getRecPeriod(),
                    REC_TYPE_ID, request.getRecTypeId()));
        } catch (UserNotExistsException ignored) {
            response.setStatus(false);
            response.setMessage(USER_NOT_EXISTS);
            LOGGER.info(Utils.wrap(EVENT_UPDATE_ENTIRE_FAIL,
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    CALENDAR_ID, calendarId,
                    EVENT_ID, eventId,
                    NAME, request.getName(),
                    DESCRIPTION, request.getDescription(),
                    LOCATION, request.getLocation(),
                    COLOR, request.getColor(),
                    START_TIME, request.getStartTimeUTC(),
                    START_TIME_TZ, request.getStartTimeTZ(),
                    END_TIME, request.getEndTimeUTC(),
                    END_TIME_TZ, request.getEndTimeTZ(),
                    REC_END_TIME, request.getRecEndTimeUTC(),
                    REC_END_TIME_TZ, request.getRecEndTimeTZ(),
                    REC_PERIOD, request.getRecPeriod(),
                    REC_TYPE_ID, request.getRecTypeId()));
        } catch (CalendarNotExistsException ignored) {
            response.setStatus(false);
            response.setMessage(CALENDAR_NOT_EXISTS);
            LOGGER.info(Utils.wrap(EVENT_UPDATE_ENTIRE_FAIL,
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    CALENDAR_ID, calendarId,
                    EVENT_ID, eventId,
                    NAME, request.getName(),
                    DESCRIPTION, request.getDescription(),
                    LOCATION, request.getLocation(),
                    COLOR, request.getColor(),
                    START_TIME, request.getStartTimeUTC(),
                    START_TIME_TZ, request.getStartTimeTZ(),
                    END_TIME, request.getEndTimeUTC(),
                    END_TIME_TZ, request.getEndTimeTZ(),
                    REC_END_TIME, request.getRecEndTimeUTC(),
                    REC_END_TIME_TZ, request.getRecEndTimeTZ(),
                    REC_PERIOD, request.getRecPeriod(),
                    REC_TYPE_ID, request.getRecTypeId()));
        } catch (EventNotExistsException ignored) {
            response.setStatus(false);
            response.setMessage(EVENT_NOT_EXISTS);
            LOGGER.info(Utils.wrap(EVENT_UPDATE_ENTIRE_FAIL,
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    CALENDAR_ID, calendarId,
                    EVENT_ID, eventId,
                    NAME, request.getName(),
                    DESCRIPTION, request.getDescription(),
                    LOCATION, request.getLocation(),
                    COLOR, request.getColor(),
                    START_TIME, request.getStartTimeUTC(),
                    START_TIME_TZ, request.getStartTimeTZ(),
                    END_TIME, request.getEndTimeUTC(),
                    END_TIME_TZ, request.getEndTimeTZ(),
                    REC_END_TIME, request.getRecEndTimeUTC(),
                    REC_END_TIME_TZ, request.getRecEndTimeTZ(),
                    REC_PERIOD, request.getRecPeriod(),
                    REC_TYPE_ID, request.getRecTypeId()));
        } catch (InternalServerException e) {
            response.setStatus(false);
            response.setMessage(OPERATION_UNAVAILABLE);
            LOGGER.info(Utils.wrap(EVENT_UPDATE_ENTIRE_FAIL,
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    CALENDAR_ID, calendarId,
                    EVENT_ID, eventId,
                    NAME, request.getName(),
                    DESCRIPTION, request.getDescription(),
                    LOCATION, request.getLocation(),
                    COLOR, request.getColor(),
                    START_TIME, request.getStartTimeUTC(),
                    START_TIME_TZ, request.getStartTimeTZ(),
                    END_TIME, request.getEndTimeUTC(),
                    END_TIME_TZ, request.getEndTimeTZ(),
                    REC_END_TIME, request.getRecEndTimeUTC(),
                    REC_END_TIME_TZ, request.getRecEndTimeTZ(),
                    REC_PERIOD, request.getRecPeriod(),
                    REC_TYPE_ID, request.getRecTypeId(),
                    INTERNAL_EXCEPTION, ExceptionUtils.getStackTrace(e)));
        }
        return response;
    }
}