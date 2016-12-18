package com.bitbucket.teamleave.leafcalendar.api.dao;

import com.bitbucket.teamleave.leafcalendar.api.utils.Utils;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * @author vladislav.trofimov@emc.com
 */
public class TimeZoneTest {

    @SuppressWarnings("JUnitTestMethodWithNoAssertions")
    @Test
    public void test() throws Exception {
        final ZoneOffset zoneOffset = ZoneOffset.of("+08:45");
        System.out.println(zoneOffset.getId());
        System.out.println(zoneOffset);
        final ZonedDateTime zonedDateTime = ZonedDateTime.parse("2007-12-03T03:15:30+08:49");

        System.out.println(zonedDateTime.toOffsetDateTime().atZoneSameInstant(ZoneId.of("Z")));
        System.out.println(zonedDateTime.getZone());

        final Timestamp timestamp = Timestamp.from(Instant.now());
        final ZonedDateTime t1 = Utils.representAsUTCZoned(timestamp);
        final ZonedDateTime t2 = Utils.convertToZoned(timestamp, ZoneOffset.of("+03:00"));
        System.currentTimeMillis();
//        EventModel eventModel = new EventModel();
//        eventModel.setStartTimeUTCFromString("2015-05-22T02:05:52Z");
//        eventModel.setStartTimeTZ("+03:00");
//        eventModel.setEndTimeUTCFromString("2015-05-25T03:05:52Z");
//        eventModel.setEndTimeTZ("+03:00");
//        eventModel.checkValidSingleEvent();
    }

}
