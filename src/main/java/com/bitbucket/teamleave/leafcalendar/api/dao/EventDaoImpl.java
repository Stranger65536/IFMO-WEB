package com.bitbucket.teamleave.leafcalendar.api.dao;

import com.bitbucket.teamleave.leafcalendar.api.exceptions.model.ModelException;
import com.bitbucket.teamleave.leafcalendar.api.model.EventModel;
import com.bitbucket.teamleave.leafcalendar.api.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;

import static com.bitbucket.teamleave.leafcalendar.api.dao.DaoConstants.Calendars.CALENDAR_ID;
import static com.bitbucket.teamleave.leafcalendar.api.dao.DaoConstants.Calendars.DELETED;
import static com.bitbucket.teamleave.leafcalendar.api.dao.DaoConstants.Events.*;

/**
 * @author vladislav.trofimov@emc.com
 */
@Repository("EventDao")
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicCharacter"})
public class EventDaoImpl extends JdbcDaoSupport implements EventDao {
    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    /**
     * Insert information about specified event into database.
     * No logical validation will be performed before insert.
     * {@code deleted} status will be set up to {@code false}
     * if it was not specified.
     *
     * @param eventModel specified event information
     * @return Generated {@code event_id} of inserted row
     */
    @SuppressWarnings("MagicNumber")
    @Override
    public String createEvent(final EventModel eventModel) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(
                connection -> {
                    final String query = "INSERT INTO " + EVENTS_TABLE + " (" +
                            NAME + ", " +
                            DESCRIPTION + ", " +
                            LOCATION + ", " +
                            DELETED + ", " +
                            COLOR + ", " +
                            PARENT_EVENT_ID + ", " +
                            START_TIME_UTC + ", " +
                            START_TIME_TZ + ", " +
                            END_TIME_UTC + ", " +
                            END_TIME_TZ + ", " +
                            REC_END_TIME_UTC + ", " +
                            REC_END_TIME_TZ + ", " +
                            REC_PERIOD + ", " +
                            REC_TYPE_ID + ", " +
                            SHOW_IN_SERIES + ", " +
                            CALENDAR_ID +
                            ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    PreparedStatement ps = connection.prepareStatement(
                            query, new String[]{EVENT_ID});
                    DaoUtils.setString(1, ps, eventModel.getName());
                    DaoUtils.setString(2, ps, eventModel.getDescription());
                    DaoUtils.setString(3, ps, eventModel.getLocation());
                    DaoUtils.setBoolean(4, ps, eventModel.getDeleted(), false);
                    DaoUtils.setString(5, ps, eventModel.getColor());
                    DaoUtils.setString(6, ps, eventModel.getParentEventId());
                    DaoUtils.setDate(7, ps, eventModel.getStartTimeUTC());
                    DaoUtils.setString(8, ps, eventModel.getStartTimeTZ());
                    DaoUtils.setDate(9, ps, eventModel.getEndTimeUTC());
                    DaoUtils.setString(10, ps, eventModel.getEndTimeTZ());
                    DaoUtils.setDate(11, ps, eventModel.getRecEndTimeUTC());
                    DaoUtils.setString(12, ps, eventModel.getRecEndTimeTZ());
                    DaoUtils.setInt(13, ps, eventModel.getRecPeriod());
                    DaoUtils.setInt(14, ps, eventModel.getRecTypeId());
                    DaoUtils.setBoolean(15, ps, eventModel.getShowInSeries());
                    DaoUtils.setString(16, ps, eventModel.getCalendarId());
                    return ps;
                }, keyHolder);
        return Utils.rawToHex((byte[]) keyHolder.getKeys().get(EVENT_ID));
    }

    /**
     * Retrieve information about all single events from database.
     * Events that starts before UTC end and finish after UTC start
     * will be selected.
     *
     * @param model         specified UTC time range and {@code calendar_id}.
     * @param deletedStatus specifies which rows have to be selected
     *                      ({@code deleted} column specification)
     * @return List of matching rows.
     */
    @SuppressWarnings("MagicNumber")
    @Override
    public List<EventModel> getAllSingleEvents(
            final EventModel model,
            final boolean deletedStatus) {
        final String query = "SELECT * FROM " + EVENTS_TABLE + ' ' +
                "WHERE " + CALENDAR_ID + " = ? " +
                "AND " + DELETED + " = " + (deletedStatus ? '1' : '0') + ' ' +
                "AND " + PARENT_EVENT_ID + " IS NULL " +
                "AND " + REC_TYPE_ID + " IS NULL " +
                "AND " + REC_PERIOD + " IS NULL " +
                "AND " + SHOW_IN_SERIES + " IS NULL " +
                "AND " + REC_END_TIME_UTC + " IS NULL " +
                "AND " + REC_END_TIME_TZ + " IS NULL " +
                "AND " + START_TIME_UTC + " IS NOT NULL " +
                "AND " + END_TIME_UTC + " IS NOT NULL " +
                "AND " + START_TIME_UTC + " < ? " +
                "AND " + END_TIME_UTC + " >= ? ";
        return getJdbcTemplate().query(query, new Object[]{
                model.getCalendarId(),
                model.getEndTimeUTC(),
                model.getStartTimeUTC()
        }, (rs, rowNum) -> {
            final EventModel model1 = new EventModel();
            try {
                model1.setEventId(rs.getString(1));
                model1.setName(rs.getNString(2));
                model1.setDescription(rs.getNString(3));
                model1.setLocation(rs.getNString(4));
                model1.setDeleted(rs.getObject(5, Boolean.class));
                model1.setColor(rs.getString(6));
                model1.setParentEventId(rs.getString(7));
                model1.setStartTimeUTC(rs.getTimestamp(8));
                model1.setStartTimeTZ(rs.getString(9));
                model1.setEndTimeUTC(rs.getTimestamp(10));
                model1.setEndTimeTZ(rs.getString(11));
                model1.setRecEndTimeUTC(rs.getTimestamp(12));
                model1.setRecEndTimeTZ(rs.getString(13));
                model1.setRecPeriod(rs.getObject(14, Integer.class));
                model1.setRecTypeId(rs.getObject(15, Integer.class));
                model1.setShowInSeries(rs.getObject(16, Boolean.class));
                model1.setCalendarId(rs.getString(17));
            } catch (ModelException e) {
                e.printStackTrace();
            }
            return model1;
        });
    }

    /**
     * Retrieve information about all recurrent events from database.
     * Events that recurrence end is not specified or finish after UTC start
     * and that first realization starts before UTC end will be selected.
     *
     * @param model         specified UTC time range and {@code calendar_id}.
     * @param deletedStatus specifies which rows have to be selected
     *                      ({@code deleted} column specification)
     * @return List of matching rows.
     */
    @SuppressWarnings("MagicNumber")
    @Override
    public List<EventModel> getAllRecurrentEvents(
            final EventModel model,
            final boolean deletedStatus) {
        final String query = "SELECT * FROM " + EVENTS_TABLE + ' ' +
                "WHERE " + CALENDAR_ID + " = ? " +
                "AND " + DELETED + " = " + (deletedStatus ? '1' : '0') + ' ' +
                "AND " + PARENT_EVENT_ID + " IS NULL " +
                "AND " + REC_TYPE_ID + " IS NOT NULL " +
                "AND " + REC_PERIOD + " IS NOT NULL " +
                "AND " + SHOW_IN_SERIES + " IS NULL " +
                "AND (" + REC_END_TIME_UTC + " IS NULL " +
                "AND " + REC_END_TIME_TZ + " IS NULL " +
                "OR " + REC_END_TIME_UTC + " >= ? " +
                "AND " + REC_END_TIME_TZ + " IS NOT NULL) " +
                "AND " + START_TIME_UTC + " IS NOT NULL " +
                "AND " + END_TIME_UTC + " IS NOT NULL " +
                "AND " + START_TIME_UTC + " < ? ";
        return getJdbcTemplate().query(query, new Object[]{
                model.getCalendarId(),
                model.getStartTimeUTC(),
                model.getEndTimeUTC()
        }, (rs, rowNum) -> {
            final EventModel model1 = new EventModel();
            try {
                model1.setEventId(rs.getString(1));
                model1.setName(rs.getNString(2));
                model1.setDescription(rs.getNString(3));
                model1.setLocation(rs.getNString(4));
                model1.setDeleted(rs.getObject(5, Boolean.class));
                model1.setColor(rs.getString(6));
                model1.setParentEventId(rs.getString(7));
                model1.setStartTimeUTC(rs.getTimestamp(8));
                model1.setStartTimeTZ(rs.getString(9));
                model1.setEndTimeUTC(rs.getTimestamp(10));
                model1.setEndTimeTZ(rs.getString(11));
                model1.setRecEndTimeUTC(rs.getTimestamp(12));
                model1.setRecEndTimeTZ(rs.getString(13));
                model1.setRecPeriod(rs.getObject(14, Integer.class));
                model1.setRecTypeId(rs.getObject(15, Integer.class));
                model1.setShowInSeries(rs.getObject(16, Boolean.class));
                model1.setCalendarId(rs.getString(17));
            } catch (ModelException e) {
                e.printStackTrace();
            }
            return model1;
        });
    }

    @SuppressWarnings("MagicNumber")
    @Override
    public List<EventModel> getAllMovedRecurrentEvents(
            final EventModel model,
            final boolean deletedStatus) {
        final String query = "SELECT * FROM " + EVENTS_TABLE + ' ' +
                "WHERE " + CALENDAR_ID + " = ? " +
                "AND " + DELETED + " = " + (deletedStatus ? '1' : '0') + ' ' +
                "AND " + PARENT_EVENT_ID + " IS NOT NULL " +
                "AND " + REC_TYPE_ID + " IS NULL " +
                "AND " + REC_PERIOD + " IS NULL " +
                "AND " + SHOW_IN_SERIES + " = 1 " +
                "AND " + REC_END_TIME_UTC + " IS NULL " +
                "AND " + REC_END_TIME_TZ + " IS NULL " +
                "AND " + START_TIME_UTC + " IS NOT NULL " +
                "AND " + END_TIME_UTC + " IS NOT NULL " +
                "AND " + START_TIME_UTC + " < ? " +
                "AND " + END_TIME_UTC + " >= ? ";
        return getJdbcTemplate().query(query, new Object[]{
                model.getCalendarId(),
                model.getEndTimeUTC(),
                model.getStartTimeUTC()
        }, (rs, rowNum) -> {
            final EventModel model1 = new EventModel();
            try {
                model1.setEventId(rs.getString(1));
                model1.setName(rs.getNString(2));
                model1.setDescription(rs.getNString(3));
                model1.setLocation(rs.getNString(4));
                model1.setDeleted(rs.getObject(5, Boolean.class));
                model1.setColor(rs.getString(6));
                model1.setParentEventId(rs.getString(7));
                model1.setStartTimeUTC(rs.getTimestamp(8));
                model1.setStartTimeTZ(rs.getString(9));
                model1.setEndTimeUTC(rs.getTimestamp(10));
                model1.setEndTimeTZ(rs.getString(11));
                model1.setRecEndTimeUTC(rs.getTimestamp(12));
                model1.setRecEndTimeTZ(rs.getString(13));
                model1.setRecPeriod(rs.getObject(14, Integer.class));
                model1.setRecTypeId(rs.getObject(15, Integer.class));
                model1.setShowInSeries(rs.getObject(16, Boolean.class));
                model1.setCalendarId(rs.getString(17));
            } catch (ModelException e) {
                e.printStackTrace();
            }
            return model1;
        });
    }

    @SuppressWarnings("MagicNumber")
    @Override
    public List<EventModel> getAllDeletedRecurrentEvents(
            final EventModel model,
            final boolean deletedStatus) {
        final String query = "SELECT * FROM " + EVENTS_TABLE + ' ' +
                "WHERE " + CALENDAR_ID + " = ? " +
                "AND " + DELETED + " = " + (deletedStatus ? '1' : '0') + ' ' +
                "AND " + PARENT_EVENT_ID + " IS NOT NULL " +
                "AND " + REC_TYPE_ID + " IS NULL " +
                "AND " + REC_PERIOD + " IS NULL " +
                "AND " + SHOW_IN_SERIES + " = 0 " +
                "AND " + REC_END_TIME_UTC + " IS NULL " +
                "AND " + REC_END_TIME_TZ + " IS NULL " +
                "AND " + START_TIME_UTC + " IS NOT NULL " +
                "AND " + END_TIME_UTC + " IS NOT NULL " +
                "AND " + START_TIME_UTC + " < ? " +
                "AND " + END_TIME_UTC + " >= ? ";
        return getJdbcTemplate().query(query, new Object[]{
                model.getCalendarId(),
                model.getEndTimeUTC(),
                model.getStartTimeUTC()
        }, (rs, rowNum) -> {
            final EventModel model1 = new EventModel();
            try {
                model1.setEventId(rs.getString(1));
                model1.setName(rs.getNString(2));
                model1.setDescription(rs.getNString(3));
                model1.setLocation(rs.getNString(4));
                model1.setDeleted(rs.getObject(5, Boolean.class));
                model1.setColor(rs.getString(6));
                model1.setParentEventId(rs.getString(7));
                model1.setStartTimeUTC(rs.getTimestamp(8));
                model1.setStartTimeTZ(rs.getString(9));
                model1.setEndTimeUTC(rs.getTimestamp(10));
                model1.setEndTimeTZ(rs.getString(11));
                model1.setRecEndTimeUTC(rs.getTimestamp(12));
                model1.setRecEndTimeTZ(rs.getString(13));
                model1.setRecPeriod(rs.getObject(14, Integer.class));
                model1.setRecTypeId(rs.getObject(15, Integer.class));
                model1.setShowInSeries(rs.getObject(16, Boolean.class));
                model1.setCalendarId(rs.getString(17));
            } catch (ModelException e) {
                e.printStackTrace();
            }
            return model1;
        });
    }

    @SuppressWarnings("MagicNumber")
    @Override
    public void updateEventById(
            final EventModel eventModel,
            final boolean deletedStatus) {
        final String query = "UPDATE " + EVENTS_TABLE + ' ' +
                "SET " +
                NAME + " = ?," +
                DESCRIPTION + " = ?," +
                LOCATION + " = ?," +
                DELETED + " = ?," +
                COLOR + " = ?," +
                PARENT_EVENT_ID + " = ?," +
                START_TIME_UTC + " = ?," +
                START_TIME_TZ + " = ?," +
                END_TIME_UTC + " = ?," +
                END_TIME_TZ + " = ?," +
                REC_END_TIME_UTC + " = ?," +
                REC_END_TIME_TZ + " = ?," +
                REC_PERIOD + " = ?," +
                REC_TYPE_ID + " = ?," +
                SHOW_IN_SERIES + " = ? " +
                "WHERE " + EVENT_ID + " = ? " +
                "AND " + DELETED + " = " + (deletedStatus ? '1' : '0');
        final int affectedRows = getJdbcTemplate().execute(query,
                (PreparedStatement ps) -> {
                    DaoUtils.setString(1, ps, eventModel.getName());
                    DaoUtils.setString(2, ps, eventModel.getDescription());
                    DaoUtils.setString(3, ps, eventModel.getLocation());
                    DaoUtils.setBoolean(4, ps, eventModel.getDeleted());
                    DaoUtils.setString(5, ps, eventModel.getColor());
                    DaoUtils.setString(6, ps, eventModel.getParentEventId());
                    DaoUtils.setDate(7, ps, Utils.getDate(eventModel.getStartTimeUTC()));
                    DaoUtils.setString(8, ps, eventModel.getStartTimeTZ());
                    DaoUtils.setDate(9, ps, Utils.getDate(eventModel.getEndTimeUTC()));
                    DaoUtils.setString(10, ps, eventModel.getEndTimeTZ());
                    DaoUtils.setDate(11, ps, Utils.getDate(eventModel.getRecEndTimeUTC()));
                    DaoUtils.setString(12, ps, eventModel.getRecEndTimeTZ());
                    DaoUtils.setInt(13, ps, eventModel.getRecPeriod());
                    DaoUtils.setInt(14, ps, eventModel.getRecTypeId());
                    DaoUtils.setBoolean(15, ps, eventModel.getShowInSeries());
                    DaoUtils.setString(16, ps, eventModel.getEventId());
                    return ps.executeUpdate();
                });
        if (affectedRows == 0) {
            throw new EmptyResultDataAccessException(0);
        }
    }

    @SuppressWarnings("MagicNumber")
    @Override
    public EventModel getEventByEventAndCalendarId(
            final EventModel model,
            final boolean deletedStatus) {
        final String query = "SELECT * FROM " + EVENTS_TABLE + ' ' +
                "WHERE " + EVENT_ID + " = ? " +
                "AND " + CALENDAR_ID + " = ? " +
                "AND " + DELETED + " = " + (deletedStatus ? '1' : '0');
        return getJdbcTemplate().queryForObject(query,
                new Object[]{
                        model.getEventId(),
                        model.getCalendarId()
                }, (rs, rowNum) -> {
                    final EventModel model1 = new EventModel();
                    try {
                        model1.setEventId(rs.getString(1));
                        model1.setName(rs.getNString(2));
                        model1.setDescription(rs.getNString(3));
                        model1.setLocation(rs.getNString(4));
                        model1.setDeleted(rs.getObject(5, Boolean.class));
                        model1.setColor(rs.getString(6));
                        model1.setParentEventId(rs.getString(7));
                        model1.setStartTimeUTC(rs.getTimestamp(8));
                        model1.setStartTimeTZ(rs.getString(9));
                        model1.setEndTimeUTC(rs.getTimestamp(10));
                        model1.setEndTimeTZ(rs.getString(11));
                        model1.setRecEndTimeUTC(rs.getTimestamp(12));
                        model1.setRecEndTimeTZ(rs.getString(13));
                        model1.setRecPeriod(rs.getObject(14, Integer.class));
                        model1.setRecTypeId(rs.getObject(15, Integer.class));
                        model1.setShowInSeries(rs.getObject(16, Boolean.class));
                        model1.setCalendarId(rs.getString(17));
                    } catch (ModelException e) {
                        e.printStackTrace();
                    }
                    return model1;
                });
    }
}