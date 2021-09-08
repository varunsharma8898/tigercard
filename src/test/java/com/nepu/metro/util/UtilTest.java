package com.nepu.metro.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.nepu.metro.vo.Zone;

public class UtilTest {

    private Zone z1, z2;

    private DateTimeFormatter formatter;

    @Before
    public void setUp() {
        z1 = new Zone("1", "Zone-1");
        z2 = new Zone("2", "Zone-2");

        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    @Test
    public void testGetKeyFromZones() {
        Assert.assertEquals("1-2", Util.getKeyFromZones(z1, z2));
        Assert.assertEquals("1-2", Util.getKeyFromZones(z2, z1));
    }

    @Test
    public void testGetWeeklyKeyFromDateTime() {
        LocalDateTime dateTime = LocalDateTime.parse("2022-01-03 10:20:00", formatter);
        Assert.assertEquals("2022-1", Util.getWeeklyKeyFromDateTime(dateTime));

        dateTime = LocalDateTime.parse("2022-01-11 10:20:00", formatter);
        Assert.assertEquals("2022-2", Util.getWeeklyKeyFromDateTime(dateTime));

        dateTime = LocalDateTime.parse("2022-12-31 10:20:00", formatter);
        Assert.assertEquals("2022-52", Util.getWeeklyKeyFromDateTime(dateTime));
    }

    @Test
    public void testGetDailyKeyFromDateTime() {
        LocalDateTime dateTime = LocalDateTime.parse("2022-01-03 10:20:00", formatter);
        Assert.assertEquals("2022-01-03", Util.getDailyKeyFromDateTime(dateTime));

        dateTime = LocalDateTime.parse("2022-01-11 10:20:00", formatter);
        Assert.assertEquals("2022-01-11", Util.getDailyKeyFromDateTime(dateTime));

        dateTime = LocalDateTime.parse("2022-12-31 10:20:00", formatter);
        Assert.assertEquals("2022-12-31", Util.getDailyKeyFromDateTime(dateTime));
    }
}