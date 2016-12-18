package com.bitbucket.teamleave.leafcalendar.api.facades;

import com.bitbucket.teamleave.leafcalendar.api.transfer.GenericResponseEntity;
import com.bitbucket.teamleave.leafcalendar.api.transfer.events.request.CreateEventRequestBody;
import com.bitbucket.teamleave.leafcalendar.api.transfer.events.request.UpdateEntireEventRequestBody;
import com.bitbucket.teamleave.leafcalendar.api.transfer.events.response.*;

/**
 * @author vladislav.trofimov@emc.com
 */
public interface EventFacade {
    GenericResponseEntity<CreateEventResponseData> createEvent(
            final String authToken,
            final String userId,
            final String calendarId,
            final CreateEventRequestBody eventData);

    GenericResponseEntity<GetEventsResponseData> getEventsByRangeAndTZ(
            final String authToken,
            final String userId,
            final String calendarId,
            final String rangeStart,
            final String rangeEnd,
            final String requestTimezone);

    GenericResponseEntity<GetEventInfoResponseData> getEventInfo(
            final String authToken,
            final String userId,
            final String calendarId,
            final String eventId);

    GenericResponseEntity<DeleteEventResponseData> deleteEntireEventById(
            final String authToken,
            final String userId,
            final String calendarId,
            final String eventId);

    GenericResponseEntity<UpdateEntireEventResponseData> updateEntireEventById(
            final String authToken,
            final String userId,
            final String calendarId,
            final String eventId,
            final UpdateEntireEventRequestBody request);
}