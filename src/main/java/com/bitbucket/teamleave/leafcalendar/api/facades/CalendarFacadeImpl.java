package com.bitbucket.teamleave.leafcalendar.api.facades;

import com.bitbucket.teamleave.leafcalendar.api.exceptions.data.calendars.CalendarNotExistsException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.data.calendars.RequiredCalendarException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.data.users.UserNotExistsException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.AuthTokenModelException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.CalendarModelException;
import com.bitbucket.teamleave.leafcalendar.api.service.CalendarService;
import com.bitbucket.teamleave.leafcalendar.api.transfer.GenericResponseEntity;
import com.bitbucket.teamleave.leafcalendar.api.transfer.calendars.request.CreateCalendarRequestBody;
import com.bitbucket.teamleave.leafcalendar.api.transfer.calendars.request.UpdateCalendarRequestBody;
import com.bitbucket.teamleave.leafcalendar.api.transfer.calendars.response.*;
import com.bitbucket.teamleave.leafcalendar.api.utils.Utils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.bitbucket.teamleave.leafcalendar.api.facades.ResponseConstants.Calendars.CALENDAR_IS_REQUIRED;
import static com.bitbucket.teamleave.leafcalendar.api.facades.ResponseConstants.Calendars.CALENDAR_NOT_EXISTS;
import static com.bitbucket.teamleave.leafcalendar.api.facades.ResponseConstants.INVALID_FIELD_FORMAT;
import static com.bitbucket.teamleave.leafcalendar.api.facades.ResponseConstants.Internal.INTERNAL_EXCEPTION;
import static com.bitbucket.teamleave.leafcalendar.api.facades.ResponseConstants.Internal.OPERATION_UNAVAILABLE;
import static com.bitbucket.teamleave.leafcalendar.api.facades.ResponseConstants.Users.USER_NOT_EXISTS;
import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Calendars.*;
import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Users.AUTH_TOKEN;
import static com.bitbucket.teamleave.leafcalendar.api.transfer.TransferConstants.Users.USER_ID;
import static com.bitbucket.teamleave.leafcalendar.api.utils.LoggerConstants.Calendars.*;

/**
 * @author vladislav.trofimov@emc.com
 */
@Service("CalendarFacade")
public class CalendarFacadeImpl implements CalendarFacade {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalendarFacadeImpl.class);
    @Autowired
    CalendarService calendarService;

    @Override
    public GenericResponseEntity<CreateCalendarResponseData> createCalendar(
            final String authToken,
            final String userId,
            final CreateCalendarRequestBody calendarData) {
        final GenericResponseEntity<CreateCalendarResponseData> response =
                new GenericResponseEntity<>();
        try {
            response.setData(new CreateCalendarResponseData(
                    calendarService.createCalendar(authToken, userId, calendarData)));
            LOGGER.info(Utils.wrap(
                    CALENDAR_CREATION_OK,
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    NAME, calendarData.getName(),
                    DESCRIPTION, calendarData.getDescription(),
                    COLOR, calendarData.getColor(),
                    VISIBLE, calendarData.getVisible(),
                    TIMEZONE, calendarData.getTimezone()));
        } catch (CalendarModelException | AuthTokenModelException e) {
            response.setStatus(false);
            response.setMessage(Utils.prepareInvalidFieldMessage(e));
            LOGGER.warn(Utils.wrap(CALENDAR_CREATION_FAIL,
                    INVALID_FIELD_FORMAT, Utils.prepareInvalidFieldMessage(e),
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    NAME, calendarData.getName(),
                    DESCRIPTION, calendarData.getDescription(),
                    COLOR, calendarData.getColor(),
                    VISIBLE, calendarData.getVisible(),
                    TIMEZONE, calendarData.getTimezone()));
        } catch (UserNotExistsException ignored) {
            response.setStatus(false);
            response.setMessage(USER_NOT_EXISTS);
            LOGGER.warn(Utils.wrap(CALENDAR_CREATION_FAIL,
                    USER_NOT_EXISTS, "",
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    NAME, calendarData.getName(),
                    DESCRIPTION, calendarData.getDescription(),
                    COLOR, calendarData.getColor(),
                    VISIBLE, calendarData.getVisible(),
                    TIMEZONE, calendarData.getTimezone()));
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage(OPERATION_UNAVAILABLE);
            LOGGER.error(Utils.wrap(CALENDAR_CREATION_FAIL,
                    OPERATION_UNAVAILABLE, "",
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    NAME, calendarData.getName(),
                    DESCRIPTION, calendarData.getDescription(),
                    COLOR, calendarData.getColor(),
                    VISIBLE, calendarData.getVisible(),
                    TIMEZONE, calendarData.getTimezone(),
                    INTERNAL_EXCEPTION, ExceptionUtils.getStackTrace(e)));
        }
        return response;
    }

    @Override
    public GenericResponseEntity<GetAllCalendarsResponseData> getAllCalendars(
            final String authToken,
            final String userId) {
        final GenericResponseEntity<GetAllCalendarsResponseData> response =
                new GenericResponseEntity<>();
        try {
            response.setData(new GetAllCalendarsResponseData(
                    calendarService.getAllCalendars(authToken, userId)));
            LOGGER.info(Utils.wrap(CALENDAR_GET_LIST_OK,
                    AUTH_TOKEN, authToken,
                    USER_ID, userId));
        } catch (AuthTokenModelException e) {
            response.setStatus(false);
            response.setMessage(Utils.prepareInvalidFieldMessage(e));
            LOGGER.warn(Utils.wrap(CALENDAR_GET_LIST_FAIL,
                    INVALID_FIELD_FORMAT, Utils.prepareInvalidFieldMessage(e),
                    AUTH_TOKEN, authToken,
                    USER_ID, userId));
        } catch (UserNotExistsException ignored) {
            response.setStatus(false);
            response.setMessage(USER_NOT_EXISTS);
            LOGGER.warn(Utils.wrap(CALENDAR_GET_LIST_FAIL,
                    USER_NOT_EXISTS, "",
                    AUTH_TOKEN, authToken,
                    USER_ID, userId));
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage(OPERATION_UNAVAILABLE);
            LOGGER.error(Utils.wrap(CALENDAR_GET_LIST_FAIL,
                    OPERATION_UNAVAILABLE, "",
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    INTERNAL_EXCEPTION, ExceptionUtils.getStackTrace(e)));
        }
        return response;
    }

    @Override
    public GenericResponseEntity<GetCalendarInfoResponseData> getCalendarInfo(
            final String authToken,
            final String userId,
            final String calendarId) {
        final GenericResponseEntity<GetCalendarInfoResponseData> response =
                new GenericResponseEntity<>();
        try {
            response.setData(new GetCalendarInfoResponseData(
                    calendarService.getCalendarInfo(authToken, userId, calendarId)));
            LOGGER.info(Utils.wrap(CALENDAR_GET_INFO_OK,
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    CALENDAR_ID, calendarId));
        } catch (AuthTokenModelException | CalendarModelException e) {
            response.setStatus(false);
            response.setMessage(Utils.prepareInvalidFieldMessage(e));
            LOGGER.warn(Utils.wrap(CALENDAR_GET_INFO_FAIL,
                    INVALID_FIELD_FORMAT, Utils.prepareInvalidFieldMessage(e),
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    CALENDAR_ID, calendarId));
        } catch (UserNotExistsException ignored) {
            response.setStatus(false);
            response.setMessage(USER_NOT_EXISTS);
            LOGGER.warn(Utils.wrap(CALENDAR_GET_INFO_FAIL,
                    USER_NOT_EXISTS, "",
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    CALENDAR_ID, calendarId));
        } catch (CalendarNotExistsException ignored) {
            response.setStatus(false);
            response.setMessage(CALENDAR_NOT_EXISTS);
            LOGGER.warn(Utils.wrap(CALENDAR_GET_INFO_FAIL,
                    CALENDAR_NOT_EXISTS, "",
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    CALENDAR_ID, calendarId));
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage(OPERATION_UNAVAILABLE);
            LOGGER.error(Utils.wrap(CALENDAR_GET_INFO_FAIL,
                    OPERATION_UNAVAILABLE, "",
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    CALENDAR_ID, calendarId,
                    INTERNAL_EXCEPTION, ExceptionUtils.getStackTrace(e)));
        }
        return response;
    }

    @Override
    public GenericResponseEntity<UpdateCalendarResponseData> updateCalendar(
            final String authToken,
            final String userId,
            final String calendarId,
            final UpdateCalendarRequestBody updatedData) {
        final GenericResponseEntity<UpdateCalendarResponseData> response =
                new GenericResponseEntity<>();
        try {
            calendarService.updateCalendar(authToken, userId, calendarId, updatedData);
            LOGGER.info(Utils.wrap(CALENDAR_UPDATE_INFO_OK,
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    NAME, updatedData.getName(),
                    DESCRIPTION, updatedData.getDescription(),
                    COLOR, updatedData.getColor(),
                    VISIBLE, updatedData.isVisible(),
                    TIMEZONE, updatedData.getTimezone()));
        } catch (AuthTokenModelException | CalendarModelException e) {
            response.setStatus(false);
            response.setMessage(Utils.prepareInvalidFieldMessage(e));
            LOGGER.warn(Utils.wrap(CALENDAR_UPDATE_INFO_FAIL,
                    INVALID_FIELD_FORMAT, Utils.prepareInvalidFieldMessage(e),
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    NAME, updatedData.getName(),
                    DESCRIPTION, updatedData.getDescription(),
                    COLOR, updatedData.getColor(),
                    VISIBLE, updatedData.isVisible(),
                    TIMEZONE, updatedData.getTimezone()));
        } catch (UserNotExistsException ignored) {
            response.setStatus(false);
            response.setMessage(USER_NOT_EXISTS);
            LOGGER.warn(Utils.wrap(CALENDAR_UPDATE_INFO_FAIL,
                    USER_NOT_EXISTS, "",
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    NAME, updatedData.getName(),
                    DESCRIPTION, updatedData.getDescription(),
                    COLOR, updatedData.getColor(),
                    VISIBLE, updatedData.isVisible(),
                    TIMEZONE, updatedData.getTimezone()));
        } catch (CalendarNotExistsException ignored) {
            response.setStatus(false);
            response.setMessage(CALENDAR_NOT_EXISTS);
            LOGGER.warn(Utils.wrap(CALENDAR_UPDATE_INFO_FAIL,
                    CALENDAR_NOT_EXISTS, "",
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    NAME, updatedData.getName(),
                    DESCRIPTION, updatedData.getDescription(),
                    COLOR, updatedData.getColor(),
                    VISIBLE, updatedData.isVisible(),
                    TIMEZONE, updatedData.getTimezone()));
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage(OPERATION_UNAVAILABLE);
            LOGGER.error(Utils.wrap(CALENDAR_UPDATE_INFO_FAIL,
                    OPERATION_UNAVAILABLE, "",
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    NAME, updatedData.getName(),
                    DESCRIPTION, updatedData.getDescription(),
                    COLOR, updatedData.getColor(),
                    VISIBLE, updatedData.isVisible(),
                    TIMEZONE, updatedData.getTimezone(),
                    INTERNAL_EXCEPTION, ExceptionUtils.getStackTrace(e)));
        }
        return response;
    }

    @Override
    public GenericResponseEntity<DeleteCalendarResponseData> deleteCalendar(
            final String authToken,
            final String userId,
            final String calendarId) {
        final GenericResponseEntity<DeleteCalendarResponseData> response =
                new GenericResponseEntity<>();
        try {
            calendarService.deleteCalendar(authToken, userId, calendarId);
            LOGGER.info(Utils.wrap(CALENDAR_DELETE_OK,
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    CALENDAR_ID, calendarId));
        } catch (AuthTokenModelException | CalendarModelException e) {
            response.setStatus(false);
            response.setMessage(Utils.prepareInvalidFieldMessage(e));
            LOGGER.warn(Utils.wrap(CALENDAR_DELETE_FAIL,
                    INVALID_FIELD_FORMAT, Utils.prepareInvalidFieldMessage(e),
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    CALENDAR_ID, calendarId));
        } catch (UserNotExistsException ignored) {
            response.setStatus(false);
            response.setMessage(USER_NOT_EXISTS);
            LOGGER.warn(Utils.wrap(CALENDAR_DELETE_FAIL,
                    USER_NOT_EXISTS, "",
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    CALENDAR_ID, calendarId));
        } catch (CalendarNotExistsException ignored) {
            response.setStatus(false);
            response.setMessage(CALENDAR_NOT_EXISTS);
            LOGGER.warn(Utils.wrap(CALENDAR_DELETE_FAIL,
                    CALENDAR_NOT_EXISTS, "",
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    CALENDAR_ID, calendarId));
        } catch (RequiredCalendarException ignored) {
            response.setStatus(false);
            response.setMessage(CALENDAR_IS_REQUIRED);
            LOGGER.warn(Utils.wrap(CALENDAR_DELETE_FAIL,
                    CALENDAR_IS_REQUIRED, "",
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    CALENDAR_ID, calendarId));
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage(OPERATION_UNAVAILABLE);
            LOGGER.error(Utils.wrap(CALENDAR_DELETE_FAIL,
                    OPERATION_UNAVAILABLE, "",
                    AUTH_TOKEN, authToken,
                    USER_ID, userId,
                    CALENDAR_ID, calendarId,
                    INTERNAL_EXCEPTION, ExceptionUtils.getStackTrace(e)));
        }
        return response;
    }
}