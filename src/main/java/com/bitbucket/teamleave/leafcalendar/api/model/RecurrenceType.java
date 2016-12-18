package com.bitbucket.teamleave.leafcalendar.api.model;

/**
 * @author vladislav.trofimov@emc.com
 */
public enum RecurrenceType {
    DAY(0),
    WEEK(1),
    MONTH(2),
    YEAR(3);

    private final int value;

    RecurrenceType(final int value) {
        this.value = value;
    }

    @SuppressWarnings("ReturnOfNull")
    public static RecurrenceType valueOf(final Integer value) {
        if (value == null) {
            return null;
        } else {
            return RecurrenceType.values().length > value && value >= 0 ?
                    RecurrenceType.values()[value] : null;
        }
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}