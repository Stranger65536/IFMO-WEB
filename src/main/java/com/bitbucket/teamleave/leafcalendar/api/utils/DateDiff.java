package com.bitbucket.teamleave.leafcalendar.api.utils;

import com.bitbucket.teamleave.leafcalendar.api.model.RecurrenceType;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author vladislav.trofimov@emc.com
 */
public class DateDiff {
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final long years;
    private final long months;
    private final long weeks;
    private final long days;
    private final long hours;
    private final long minutes;
    private final long seconds;

    @SuppressWarnings("TypeMayBeWeakened")
    public DateDiff(
            final LocalDateTime start,
            final LocalDateTime end) {
        this.start = start;
        this.end = end;
        this.years = start.until(end, ChronoUnit.YEARS);
        this.months = start.until(end, ChronoUnit.MONTHS);
        this.days = start.until(end, ChronoUnit.DAYS);
        this.hours = start.until(end, ChronoUnit.HOURS);
        this.minutes = start.until(end, ChronoUnit.MINUTES);
        this.seconds = start.until(end, ChronoUnit.SECONDS);
        this.weeks = start.until(end, ChronoUnit.WEEKS);
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public long getYears() {
        return years;
    }

    public long getMonths() {
        return months;
    }

    public long getWeeks() {
        return weeks;
    }

    public long getDays() {
        return days;
    }

    public long getHours() {
        return hours;
    }

    public long getMinutes() {
        return minutes;
    }

    public long getSeconds() {
        return seconds;
    }

    public boolean isValidRecurrenceRealization(
            final RecurrenceType type,
            final int period) {
        switch (type) {
            case DAY:
                return this.days % period == 0;
            case WEEK:
                return this.weeks % period == 0 &&
                        start.getDayOfWeek() == end.getDayOfWeek();
            case MONTH:
                return this.months % period == 0 &&
                        start.getDayOfMonth() == end.getDayOfMonth();
            case YEAR:
                return this.years % period == 0 &&
                        start.getMonth() == end.getMonth() &&
                        start.getDayOfMonth() == end.getDayOfMonth();
            default:
                throw new IllegalArgumentException(
                        "One more recurrent type must be handled: " + type);
        }
    }
}