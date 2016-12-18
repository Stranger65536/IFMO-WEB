package com.bitbucket.teamleave.leafcalendar.api.controller;

import com.bitbucket.teamleave.leafcalendar.api.facades.CalendarFacade;
import com.bitbucket.teamleave.leafcalendar.api.transfer.GenericResponseEntity;
import com.bitbucket.teamleave.leafcalendar.api.transfer.calendars.request.CreateCalendarRequestBody;
import com.bitbucket.teamleave.leafcalendar.api.transfer.calendars.request.UpdateCalendarRequestBody;
import com.bitbucket.teamleave.leafcalendar.api.transfer.calendars.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.bitbucket.teamleave.leafcalendar.api.controller.ContextPaths.*;
import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Users.AUTH_TOKEN;

/**
 * @author vladislav.trofimov@emc.com
 */
@RestController
@SuppressWarnings("StringConcatenationMissingWhitespace")
@RequestMapping(
        value = USERS + USER_ID_PATH + CALENDARS,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class CalendarsController {
    @Autowired
    private CalendarFacade calendarFacade;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseEntity<CreateCalendarResponseData> createCalendar(
            @RequestHeader(AUTH_TOKEN) final String authToken,
            @PathVariable(USER_ID) final String userId,
            @RequestBody final CreateCalendarRequestBody request) {
        return calendarFacade.createCalendar(authToken, userId, request);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            consumes = MediaType.ALL_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseEntity<GetAllCalendarsResponseData> getAllCalendars(
            @RequestHeader(AUTH_TOKEN) final String authToken,
            @PathVariable(USER_ID) final String userId) {
        return calendarFacade.getAllCalendars(authToken, userId);
    }

    @RequestMapping(
            value = CALENDAR_ID_PATH,
            method = RequestMethod.GET,
            consumes = MediaType.ALL_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseEntity<GetCalendarInfoResponseData> getCalendarInfo(
            @RequestHeader(AUTH_TOKEN) final String authToken,
            @PathVariable(USER_ID) final String userId,
            @PathVariable(CALENDAR_ID) final String calendarId) {
        return calendarFacade.getCalendarInfo(authToken, userId, calendarId);
    }

    @RequestMapping(
            value = CALENDAR_ID_PATH + UPDATE,
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseEntity<UpdateCalendarResponseData> updateCalendarInfo(
            @RequestHeader(AUTH_TOKEN) final String authToken,
            @PathVariable(USER_ID) final String userId,
            @PathVariable(CALENDAR_ID) final String calendarId,
            @RequestBody final UpdateCalendarRequestBody request) {
        return calendarFacade.updateCalendar(authToken, userId, calendarId, request);
    }

    @RequestMapping(
            value = CALENDAR_ID_PATH,
            method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseEntity<DeleteCalendarResponseData> deleteCalendar(
            @RequestHeader(AUTH_TOKEN) final String authToken,
            @PathVariable(USER_ID) final String userId,
            @PathVariable(CALENDAR_ID) final String calendarId) {
        return calendarFacade.deleteCalendar(authToken, userId, calendarId);
    }
}