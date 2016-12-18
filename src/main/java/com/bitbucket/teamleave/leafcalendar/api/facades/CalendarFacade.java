package com.bitbucket.teamleave.leafcalendar.api.facades;

import com.bitbucket.teamleave.leafcalendar.api.transfer.GenericResponseEntity;
import com.bitbucket.teamleave.leafcalendar.api.transfer.calendars.request.CreateCalendarRequestBody;
import com.bitbucket.teamleave.leafcalendar.api.transfer.calendars.request.UpdateCalendarRequestBody;
import com.bitbucket.teamleave.leafcalendar.api.transfer.calendars.response.*;

/**
 * @author vladislav.trofimov@emc.com
 */
public interface CalendarFacade {
    GenericResponseEntity<CreateCalendarResponseData> createCalendar(
            final String authToken,
            final String userId,
            final CreateCalendarRequestBody calendarData);

    GenericResponseEntity<GetAllCalendarsResponseData> getAllCalendars(
            final String authToken,
            final String userId);

    GenericResponseEntity<GetCalendarInfoResponseData> getCalendarInfo(
            final String authToken,
            final String userId,
            final String calendarId);

    GenericResponseEntity<UpdateCalendarResponseData> updateCalendar(
            final String authToken,
            final String userId,
            final String calendarId,
            final UpdateCalendarRequestBody updatedData);

    GenericResponseEntity<DeleteCalendarResponseData> deleteCalendar(
            final String authToken,
            final String userId,
            final String calendarId);
}