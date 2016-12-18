package com.bitbucket.teamleave.leafcalendar.api.dao;

import com.bitbucket.teamleave.leafcalendar.api.model.CalendarModel;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author vladislav.trofimov@emc.com
 */
public interface CalendarDao {
    void setDataSource(final DataSource dataSource);

    String createCalendar(final CalendarModel calendarModel);

    List<String> getAllCalendarIdsByUserId(
            final CalendarModel calendarModel,
            final boolean deletedStatus);

    CalendarModel getCalendarByCalendarAndUserId(
            final CalendarModel calendarModel,
            final boolean deletedStatus);

    void updateCalendarByCalendarAndUserId(
            final CalendarModel calendarModel,
            final boolean deletedStatus);
}