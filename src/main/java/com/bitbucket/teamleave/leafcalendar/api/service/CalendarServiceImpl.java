package com.bitbucket.teamleave.leafcalendar.api.service;

import com.bitbucket.teamleave.leafcalendar.api.dao.CalendarDao;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.InternalServerException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.data.calendars.CalendarNotExistsException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.data.calendars.RequiredCalendarException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.data.users.UserNotExistsException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.AuthTokenModelException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.CalendarModelException;
import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.UserModelException;
import com.bitbucket.teamleave.leafcalendar.api.model.CalendarModel;
import com.bitbucket.teamleave.leafcalendar.api.model.UserModel;
import com.bitbucket.teamleave.leafcalendar.api.service.model.CalendarServiceCreateCalendarModel;
import com.bitbucket.teamleave.leafcalendar.api.service.model.CalendarServiceGetAllCalendarsModel;
import com.bitbucket.teamleave.leafcalendar.api.service.model.CalendarServiceGetCalendarInfoModel;
import com.bitbucket.teamleave.leafcalendar.api.transfer.calendars.request.CreateCalendarRequestBody;
import com.bitbucket.teamleave.leafcalendar.api.transfer.calendars.request.UpdateCalendarRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.bitbucket.teamleave.leafcalendar.api.utils.Utils.NOT_DELETED;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings({"LawOfDemeter", "OverlyCoupledClass"})
@Service("CalendarService")
@Transactional(propagation = Propagation.REQUIRED,
        isolation = Isolation.SERIALIZABLE,
        rollbackFor = Exception.class)
public class CalendarServiceImpl implements CalendarService {
    @Autowired
    CalendarDao calendarDao;
    @Autowired
    AuthTokenService authTokenService;
    @Autowired
    UserService userService;


    @Override
    public CalendarServiceCreateCalendarModel createDefaultCalendar(
            final String authToken,
            final String userId,
            final String calendarName) throws
            AuthTokenModelException, CalendarModelException,
            UserNotExistsException, InternalServerException,
            UserModelException {
        final UserModel model = new UserModel(userId);
        userService.fetchModelByTokenIdAndCheckId(userId, authToken, model);
        final CalendarModel calendarModel = new CalendarModel();
        try {
            calendarModel.setName(calendarName);
            calendarModel.setUserId(userId);
            calendarModel.setRequired(true);
            final String calendarId = calendarDao.createCalendar(calendarModel);
            return new CalendarServiceCreateCalendarModel(calendarId);
        } catch (UserModelException e) {
            throw new InternalServerException(e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            rollbackFor = Exception.class)
    public CalendarServiceCreateCalendarModel createCalendar(
            final String authToken,
            final String userId,
            final CreateCalendarRequestBody calendarData) throws
            AuthTokenModelException,
            CalendarModelException, UserNotExistsException,
            InternalServerException, UserModelException {
        final UserModel model = new UserModel(userId);
        userService.fetchModelByTokenIdAndCheckId(userId, authToken, model);
        final CalendarModel calendarModel = new CalendarModel();
        try {
            calendarModel.populateWithCreateInfo(userId, calendarData);
            final String calendarId = calendarDao.createCalendar(calendarModel);
            return new CalendarServiceCreateCalendarModel(calendarId);
        } catch (UserModelException e) {
            throw new InternalServerException(e);
        }
    }

    @Override
    public CalendarServiceGetAllCalendarsModel getAllCalendars(
            final String authToken,
            final String userId) throws
            AuthTokenModelException,
            UserNotExistsException, InternalServerException,
            UserModelException {
        final UserModel model = new UserModel(userId);
        userService.fetchModelByTokenIdAndCheckId(userId, authToken, model);
        final CalendarModel calendarModel;
        try {
            calendarModel = new CalendarModel(userId);
        } catch (UserModelException e) {
            throw new InternalServerException(e);
        }
        return new CalendarServiceGetAllCalendarsModel(
                calendarDao.getAllCalendarIdsByUserId(calendarModel, NOT_DELETED));
    }

    @Override
    public CalendarServiceGetCalendarInfoModel getCalendarInfo(
            final String authToken,
            final String userId,
            final String calendarId) throws
            AuthTokenModelException, InternalServerException,
            CalendarModelException, UserNotExistsException,
            CalendarNotExistsException, UserModelException {
        final UserModel model = new UserModel(userId);
        userService.fetchModelByTokenIdAndCheckId(userId, authToken, model);
        try {
            CalendarModel calendarModel = new CalendarModel(calendarId, userId);
            calendarModel = calendarDao.getCalendarByCalendarAndUserId(
                    calendarModel, NOT_DELETED);
            return new CalendarServiceGetCalendarInfoModel(calendarModel);
        } catch (UserModelException e) {
            throw new InternalServerException(e);
        } catch (EmptyResultDataAccessException e) {
            throw new CalendarNotExistsException(e);
        }
    }

    @SuppressWarnings("LawOfDemeter")
    @Override
    public void updateCalendar(
            final String authToken,
            final String userId,
            final String calendarId,
            final UpdateCalendarRequestBody updatedData) throws
            AuthTokenModelException, InternalServerException,
            UserNotExistsException, CalendarModelException,
            CalendarNotExistsException, UserModelException {
        final UserModel model = new UserModel(userId);
        userService.fetchModelByTokenIdAndCheckId(userId, authToken, model);
        CalendarModel calendarModel;
        try {
            calendarModel = new CalendarModel(calendarId, userId);
            calendarModel = calendarDao.getCalendarByCalendarAndUserId(
                    calendarModel, NOT_DELETED);
        } catch (UserModelException e) {
            throw new InternalServerException(e);
        } catch (EmptyResultDataAccessException e) {
            throw new CalendarNotExistsException(e);
        }
        try {
            calendarModel.populateWithUpdateInfo(updatedData);
            calendarDao.updateCalendarByCalendarAndUserId(
                    calendarModel, NOT_DELETED);
        } catch (EmptyResultDataAccessException e) {
            throw new CalendarNotExistsException(e);
        }
    }

    @SuppressWarnings("LawOfDemeter")
    @Override
    public void deleteCalendar(
            final String authToken,
            final String userId,
            final String calendarId) throws
            CalendarNotExistsException, AuthTokenModelException,
            InternalServerException, UserNotExistsException,
            CalendarModelException, UserModelException,
            RequiredCalendarException {
        final UserModel model = new UserModel(userId);
        userService.fetchModelByTokenIdAndCheckId(userId, authToken, model);
        CalendarModel calendarModel;
        try {
            calendarModel = new CalendarModel(calendarId, userId);
            calendarModel = calendarDao.getCalendarByCalendarAndUserId(
                    calendarModel, NOT_DELETED);
            if (calendarModel.isRequired()) {
                throw new RequiredCalendarException("Required calendar can not be deleted");
            }
        } catch (UserModelException e) {
            throw new InternalServerException(e);
        } catch (EmptyResultDataAccessException e) {
            throw new CalendarNotExistsException(e);
        }
        try {
            calendarModel.delete();
            calendarDao.updateCalendarByCalendarAndUserId(
                    calendarModel, NOT_DELETED);
        } catch (CalendarModelException | EmptyResultDataAccessException e) {
            throw new InternalServerException(e);
        }
    }
}