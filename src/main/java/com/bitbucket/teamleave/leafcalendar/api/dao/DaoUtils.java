package com.bitbucket.teamleave.leafcalendar.api.dao;

import com.bitbucket.teamleave.leafcalendar.api.utils.Utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

/**
 * @author vladislav.trofimov@emc.com
 */
public class DaoUtils {
    public static void setBoolean(
            final int number,
            final PreparedStatement ps,
            final Boolean value) throws
            SQLException {
        if (value == null) {
            //ORACLE does not have BIT sql type,
            //using NUMBER(1) instead
            ps.setNull(number, Types.INTEGER);
        } else {
            ps.setBoolean(number, value);
        }
    }

    public static void setBoolean(
            final int number,
            final PreparedStatement ps,
            final Boolean value,
            final Boolean defaultValue) throws
            SQLException {
        if (value == null && defaultValue == null) {
            //ORACLE does not have BIT sql type,
            //using NUMBER(1) instead
            ps.setNull(number, Types.INTEGER);
        } else if (value == null) {
            ps.setBoolean(number, defaultValue);
        } else {
            ps.setBoolean(number, value);
        }
    }

    public static void setInt(
            final int number,
            final PreparedStatement ps,
            final Integer value) throws
            SQLException {
        if (value == null) {
            ps.setNull(number, Types.INTEGER);
        } else {
            ps.setInt(number, value);
        }
    }

    public static void setString(
            final int number,
            final PreparedStatement ps,
            final String value) throws
            SQLException {
        if (value == null) {
            ps.setNull(number, Types.VARCHAR);
        } else {
            ps.setString(number, value);
        }
    }

    public static void setString(
            final int number,
            final PreparedStatement ps,
            final String value,
            final String defaultValue) throws
            SQLException {
        if (value == null && defaultValue == null) {
            ps.setNull(number, Types.VARCHAR);
        } else if (value == null) {
            ps.setString(number, defaultValue);
        } else {
            ps.setString(number, value);
        }
    }

    public static void setDate(
            final int number,
            final PreparedStatement ps,
            final Date value) throws
            SQLException {
        if (value == null) {
            ps.setNull(number, Types.DATE);
        } else {
            ps.setDate(number, Utils.getDate(value));
        }
    }

    public static void setDate(
            final int number,
            final PreparedStatement ps,
            final Date value,
            final Date defaultValue) throws
            SQLException {
        if (value == null && defaultValue == null) {
            ps.setNull(number, Types.DATE);
        } else if (value == null) {
            ps.setDate(number, Utils.getDate(defaultValue));
        } else {
            ps.setDate(number, Utils.getDate(value));
        }
    }
}