package com.bitbucket.teamleave.leafcalendar.api.dao;

import com.bitbucket.teamleave.leafcalendar.api.model.EventModel;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author vladislav.trofimov@emc.com
 */
public interface EventDao {
    void setDataSource(final DataSource dataSource);

    String createEvent(final EventModel eventModel);

    List<EventModel> getAllSingleEvents(
            final EventModel model,
            final boolean deletedStatus);

    List<EventModel> getAllRecurrentEvents(
            final EventModel model,
            final boolean deletedStatus);

    List<EventModel> getAllMovedRecurrentEvents(
            final EventModel model,
            final boolean deletedStatus);

    List<EventModel> getAllDeletedRecurrentEvents(
            final EventModel model,
            final boolean deletedStatus);

    void updateEventById(
            final EventModel eventModel,
            final boolean deletedStatus);

    EventModel getEventByEventAndCalendarId(
            final EventModel model,
            final boolean deletedStatus);
}