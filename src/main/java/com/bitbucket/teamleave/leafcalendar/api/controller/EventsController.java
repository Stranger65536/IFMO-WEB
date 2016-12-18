package com.bitbucket.teamleave.leafcalendar.api.controller;

import com.bitbucket.teamleave.leafcalendar.api.facades.EventFacade;
import com.bitbucket.teamleave.leafcalendar.api.transfer.GenericResponseEntity;
import com.bitbucket.teamleave.leafcalendar.api.transfer.events.request.*;
import com.bitbucket.teamleave.leafcalendar.api.transfer.events.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.bitbucket.teamleave.leafcalendar.api.controller.ContextPaths.*;
import static com.bitbucket.teamleave.leafcalendar.api.controller.ContextPaths.EVENTS;
import static com.bitbucket.teamleave.leafcalendar.api.controller.ContextPaths.EVENT_ID;
import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Events.*;
import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Users.AUTH_TOKEN;

/**
 * @author vladislav.trofimov@emc.com
 */
@RestController
@SuppressWarnings("StringConcatenationMissingWhitespace")
@RequestMapping(
        value = USERS + USER_ID_PATH + CALENDARS + CALENDAR_ID_PATH + EVENTS,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class EventsController {
    @Autowired
    private EventFacade eventFacade;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseEntity<CreateEventResponseData> createEvent(
            @RequestHeader(AUTH_TOKEN) final String authToken,
            @PathVariable(USER_ID) final String userId,
            @PathVariable(CALENDAR_ID) final String calendarId,
            @RequestBody final CreateEventRequestBody request) {
        return eventFacade.createEvent(authToken, userId, calendarId, request);
    }

    @RequestMapping(
            value = EVENT_ID_PATH,
            method = RequestMethod.GET,
            consumes = MediaType.ALL_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseEntity<GetEventInfoResponseData> getEventById(
            @RequestHeader(AUTH_TOKEN) final String authToken,
            @PathVariable(USER_ID) final String userId,
            @PathVariable(CALENDAR_ID) final String calendarId,
            @PathVariable(EVENT_ID) final String eventId) {
        return eventFacade.getEventInfo(authToken, userId, calendarId, eventId);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            consumes = MediaType.ALL_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseEntity<GetEventsResponseData> getAllEvents(
            @RequestHeader(AUTH_TOKEN) final String authToken,
            @PathVariable(USER_ID) final String userId,
            @PathVariable(CALENDAR_ID) final String calendarId,
            @RequestParam(RANGE_START) final String rangeStart,
            @RequestParam(RANGE_END) final String rangeEnd,
            @RequestParam(REQUEST_TZ) final String requestTimezone) {
        return eventFacade.getEventsByRangeAndTZ(authToken, userId,
                calendarId, rangeStart, rangeEnd, requestTimezone);
    }

    @RequestMapping(
            value = EVENT_ID_PATH,
            method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseEntity<DeleteEventResponseData> deleteEntireEventById(
            @RequestHeader(AUTH_TOKEN) final String authToken,
            @PathVariable(USER_ID) final String userId,
            @PathVariable(CALENDAR_ID) final String calendarId,
            @PathVariable(EVENT_ID) final String eventId) {
        return eventFacade.deleteEntireEventById(authToken, userId,
                calendarId, eventId);
    }

    @RequestMapping(
            value = EVENT_ID_PATH + ONE,
            method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseEntity<DeleteEventResponseData> deleteOneEventFromSequence(
            @RequestHeader(AUTH_TOKEN) final String authToken,
            @PathVariable(USER_ID) final String userId,
            @PathVariable(CALENDAR_ID) final String calendarId,
            @PathVariable(EVENT_ID) final String eventId,
            @RequestBody final DeleteOneEventRequestBody request) {
        return null;
    }

    @RequestMapping(
            value = EVENT_ID_PATH + AFTER,
            method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseEntity<DeleteEventResponseData> deleteAllEventsAfterFromSequence(
            @RequestHeader(AUTH_TOKEN) final String authToken,
            @PathVariable(USER_ID) final String userId,
            @PathVariable(CALENDAR_ID) final String calendarId,
            @PathVariable(EVENT_ID) final String eventId,
            @RequestBody final DeleteAfterEventsRequestBody request) {
        return null;
    }

    @RequestMapping(
            value = EVENT_ID_PATH + UPDATE,
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseEntity<UpdateEntireEventResponseData> updateEntireEventById(
            @RequestHeader(AUTH_TOKEN) final String authToken,
            @PathVariable(USER_ID) final String userId,
            @PathVariable(CALENDAR_ID) final String calendarId,
            @PathVariable(EVENT_ID) final String eventId,
            @RequestBody final UpdateEntireEventRequestBody request) {
        return eventFacade.updateEntireEventById(authToken, userId,
                calendarId, eventId, request);
    }

    @RequestMapping(
            value = EVENT_ID_PATH + UPDATE + ONE,
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseEntity<UpdateEventsPartResponseData> updateOneEventFromSequence(
            @RequestHeader(AUTH_TOKEN) final String authToken,
            @PathVariable(USER_ID) final String userId,
            @PathVariable(CALENDAR_ID) final String calendarId,
            @PathVariable(EVENT_ID) final String eventId,
            @RequestBody final UpdateOneEventRequestBody request) {
        return null;
    }

    @RequestMapping(
            value = EVENT_ID_PATH + UPDATE + AFTER,
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseEntity<UpdateEventsPartResponseData> updateAllEventsAfterFromSequence(
            @RequestHeader(AUTH_TOKEN) final String authToken,
            @PathVariable(USER_ID) final String userId,
            @PathVariable(CALENDAR_ID) final String calendarId,
            @PathVariable(EVENT_ID) final String eventId,
            @RequestBody final UpdateAfterEventsRequestBody request) {
        return null;
    }
}