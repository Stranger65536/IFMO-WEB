package com.bitbucket.teamleave.leafcalendar.api.dao;

import com.bitbucket.teamleave.leafcalendar.api.model.CalendarModel;
import com.bitbucket.teamleave.leafcalendar.api.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;

import static com.bitbucket.teamleave.leafcalendar.api.dao.DaoConstants.Calendars.*;
import static com.bitbucket.teamleave.leafcalendar.api.dao.DaoConstants.Users.USER_ID;

/**
 * @author vladislav.trofimov@emc.com
 */
@Repository("CalendarDao")
@SuppressWarnings({"DuplicateStringLiteralInspection", "MagicCharacter"})
public class CalendarDaoImpl extends JdbcDaoSupport implements CalendarDao {
    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public String createCalendar(final CalendarModel calendarModel) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(
                connection -> {
                    final String query = "INSERT INTO " + CALENDARS_TABLE + " (" +
                            NAME + ", " +
                            DESCRIPTION + ", " +
                            COLOR + ", " +
                            VISIBLE + ", " +
                            DELETED + ", " +
                            REQUIRED + ", " +
                            TIMEZONE + ", " +
                            USER_ID +
                            ") VALUES (?,?,?,?,?,?,?,?)";
                    PreparedStatement ps = connection.prepareStatement(query,
                            new String[]{CALENDAR_ID});
                    DaoUtils.setString(1, ps, calendarModel.getName());
                    DaoUtils.setString(2, ps, calendarModel.getDescription());
                    DaoUtils.setString(3, ps, calendarModel.getColor(), Utils.getRandomHexColor());
                    DaoUtils.setBoolean(4, ps, calendarModel.isVisible(), true);
                    DaoUtils.setBoolean(5, ps, calendarModel.isDeleted(), false);
                    DaoUtils.setBoolean(6, ps, calendarModel.isRequired(), false);
                    DaoUtils.setString(7, ps, calendarModel.getTimezone());
                    DaoUtils.setString(8, ps, calendarModel.getUserId());
                    return ps;
                }, keyHolder);
        return Utils.rawToHex((byte[]) keyHolder.getKeys().get(CALENDAR_ID));
    }

    @Override
    public List<String> getAllCalendarIdsByUserId(
            final CalendarModel calendarModel,
            final boolean deletedStatus) {
        final String query = "SELECT DISTINCT " + CALENDAR_ID +
                " FROM " + CALENDARS_TABLE + ' ' +
                "WHERE " + USER_ID + " = ? " +
                "AND " + DELETED + " = " + (deletedStatus ? '1' : '0');
        return getJdbcTemplate().queryForList(query, new Object[]{
                calendarModel.getUserId()
        }, String.class);
    }

    @Override
    public CalendarModel getCalendarByCalendarAndUserId(
            final CalendarModel calendarModel,
            final boolean deletedStatus) {
        final String query = "SELECT * FROM " + CALENDARS_TABLE + ' ' +
                "WHERE " + CALENDAR_ID + " = ? " +
                "AND " + USER_ID + " = ? " +
                "AND " + DELETED + " = " + (deletedStatus ? '1' : '0');
        return getJdbcTemplate().queryForObject(query,
                new Object[]{
                        calendarModel.getCalendarId(),
                        calendarModel.getUserId()
                }, new BeanPropertyRowMapper<>(CalendarModel.class));
    }

    @Override
    public void updateCalendarByCalendarAndUserId(
            final CalendarModel calendarModel,
            final boolean deletedStatus) {
        final String query = "UPDATE " + CALENDARS_TABLE + ' ' +
                "SET " +
                NAME + " = ?," +
                DESCRIPTION + " = ?," +
                COLOR + " = ?," +
                VISIBLE + " = ?," +
                DELETED + " = ?," +
                REQUIRED + " = ?," +
                TIMEZONE + " = ? " +
                "WHERE " + CALENDAR_ID + " = ? " +
                "AND " + USER_ID + " = ? " +
                "AND " + DELETED + " = " + (deletedStatus ? '1' : '0');
        final int affectedRows = getJdbcTemplate().execute(query,
                (PreparedStatement ps) -> {
                    DaoUtils.setString(1, ps, calendarModel.getName());
                    DaoUtils.setString(2, ps, calendarModel.getDescription());
                    DaoUtils.setString(3, ps, calendarModel.getColor(), Utils.getRandomHexColor());
                    DaoUtils.setBoolean(4, ps, calendarModel.isVisible(), true);
                    DaoUtils.setBoolean(5, ps, calendarModel.isDeleted(), false);
                    DaoUtils.setBoolean(6, ps, calendarModel.isRequired(), false);
                    DaoUtils.setString(7, ps, calendarModel.getTimezone());
                    DaoUtils.setString(8, ps, calendarModel.getCalendarId());
                    DaoUtils.setString(9, ps, calendarModel.getUserId());
                    return ps.executeUpdate();
                });
        if (affectedRows == 0) {
            throw new EmptyResultDataAccessException(0);
        }
    }
}