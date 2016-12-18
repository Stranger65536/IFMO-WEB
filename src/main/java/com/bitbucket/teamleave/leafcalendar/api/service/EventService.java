package com.bitbucket.teamleave.leafcalendar.api.service;

import com.bitbucket.teamleave.leafcalendar.api.exceptions.InternalServerException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.data.calendars.CalendarNotExistsException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.data.events.EventNotExistsException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.data.users.UserNotExistsException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.AuthTokenModelException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.CalendarModelException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.EventModelException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.UserModelException;
import com.bitbucket.teamleave.leafcalendar.api.service.model.EventServiceCreateEventModel;
import com.bitbucket.teamleave.leafcalendar.api.service.model.EventServiceGetEventInfoModel;
import com.bitbucket.teamleave.leafcalendar.api.service.model.EventServiceGetEventsModel;
import com.bitbucket.teamleave.leafcalendar.api.transfer.events.request.CreateEventRequestBody;
import com.bitbucket.teamleave.leafcalendar.api.transfer.events.request.UpdateEntireEventRequestBody;

/**
 * @author vladislav.trofimov@emc.com
 */
public interface EventService {
    EventServiceCreateEventModel createEvent(
            final String authToken,
            final String userId,
            final String calendarId,
            final CreateEventRequestBody eventData) throws
            CalendarNotExistsException, UserNotExistsException,
            EventModelException, AuthTokenModelException,
            InternalServerException, CalendarModelException,
            UserModelException;

    EventServiceGetEventsModel getEventsByRangeAndTZ(
            final String authToken,
            final String userId,
            final String calendarId,
            final String rangeStart,
            final String rangeEnd,
            final String requestTimezone) throws
            AuthTokenModelException, CalendarModelException,
            EventModelException, UserNotExistsException,
            CalendarNotExistsException, InternalServerException,
            UserModelException;

        EventServiceGetEventInfoModel getEventInfo(
                final String authToken,
                final String userId,
                final String calendarId,
                final String eventId) throws
                InternalServerException, CalendarModelException,
                UserNotExistsException, CalendarNotExistsException,
                AuthTokenModelException, EventNotExistsException,
                EventModelException, UserModelException;

        void deleteEntireEventById(
                final String authToken,
                final String userId,
                final String calendarId,
                final String eventId) throws
                EventNotExistsException, InternalServerException,
                CalendarModelException, UserNotExistsException,
                CalendarNotExistsException, AuthTokenModelException,
                EventModelException, UserModelException;

        void updateEntireEventById(
                final String authToken,
                final String userId,
                final String calendarId,
                final String eventId,
                final UpdateEntireEventRequestBody request) throws
                InternalServerException, CalendarModelException,
                UserNotExistsException, CalendarNotExistsException,
                AuthTokenModelException, EventModelException,
                EventNotExistsException, UserModelException;
}
