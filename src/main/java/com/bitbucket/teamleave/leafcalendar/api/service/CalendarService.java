package com.bitbucket.teamleave.leafcalendar.api.service;

import com.bitbucket.teamleave.leafcalendar.api.exceptions.InternalServerException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.data.calendars.CalendarNotExistsException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.data.calendars.RequiredCalendarException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.data.users.UserNotExistsException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.AuthTokenModelException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.CalendarModelException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.UserModelException;
import com.bitbucket.teamleave.leafcalendar.api.service.model.CalendarServiceCreateCalendarModel;
import com.bitbucket.teamleave.leafcalendar.api.service.model.CalendarServiceGetAllCalendarsModel;
import com.bitbucket.teamleave.leafcalendar.api.service.model.CalendarServiceGetCalendarInfoModel;
import com.bitbucket.teamleave.leafcalendar.api.transfer.calendars.request.CreateCalendarRequestBody;
import com.bitbucket.teamleave.leafcalendar.api.transfer.calendars.request.UpdateCalendarRequestBody;

/**
 * @author vladislav.trofimov@emc.com
 */
public interface CalendarService {
    CalendarServiceCreateCalendarModel createDefaultCalendar(
            final String authToken,
            final String userId,
            final String calendarName) throws
            AuthTokenModelException, CalendarModelException,
            UserNotExistsException, InternalServerException,
            UserModelException;

    CalendarServiceCreateCalendarModel createCalendar(
            final String authToken,
            final String userId,
            final CreateCalendarRequestBody calendarData) throws
            AuthTokenModelException, CalendarModelException,
            UserNotExistsException, InternalServerException,
            UserModelException;

    CalendarServiceGetAllCalendarsModel getAllCalendars(
            final String authToken,
            final String userId) throws
            AuthTokenModelException, UserNotExistsException,
            InternalServerException, UserModelException;

    CalendarServiceGetCalendarInfoModel getCalendarInfo(
            final String authToken,
            final String userId,
            final String calendarId) throws
            AuthTokenModelException, CalendarModelException,
            UserNotExistsException, CalendarNotExistsException,
            InternalServerException, UserModelException;

    void updateCalendar(
            final String authToken,
            final String userId,
            final String calendarId,
            final UpdateCalendarRequestBody updatedData) throws
            AuthTokenModelException, CalendarModelException,
            UserNotExistsException, CalendarNotExistsException,
            InternalServerException, UserModelException;

    void deleteCalendar(
            final String authToken,
            final String userId,
            final String calendarId) throws
            AuthTokenModelException, CalendarModelException,
            UserNotExistsException, CalendarNotExistsException,
            InternalServerException, UserModelException,
            RequiredCalendarException;
}