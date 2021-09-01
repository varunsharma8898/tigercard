package com.nepu.metro.dao;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.nepu.metro.vo.PeakHour;

public class PeakHourTimingDaoImplTest {

    PeakHourTimingDao dao;

    List<PeakHour> peakHourList;

    @Before
    public void setUp() {
        dao = new PeakHourTimingDaoImpl();
        peakHourList = new ArrayList<>();
        peakHourList.add(new PeakHour(LocalTime.of(7, 0), LocalTime.of(10, 30)));
        peakHourList.add(new PeakHour(LocalTime.of(17, 0), LocalTime.of(20, 30)));
    }

    @Test
    public void testGetPeakHourMapping() {
        dao.addPeakHourForDayOfWeek(DayOfWeek.MONDAY, peakHourList);
        List<PeakHour> peakHours = dao.getPeakHourMapping(DayOfWeek.MONDAY);
        Assert.assertNotNull(peakHours);
        Assert.assertEquals(2, peakHours.size());
    }

    @Test
    public void testAddPeakHourForDayOfWeek() {
        dao.addPeakHourForDayOfWeek(DayOfWeek.MONDAY, peakHourList);
        List<PeakHour> peakHours = dao.getPeakHourMapping(DayOfWeek.MONDAY);
        Assert.assertNotNull(peakHours);
        Assert.assertEquals(2, peakHours.size());
    }

    @Test
    public void testAddPeakHourForDayOfWeek_EmptyMap() {
        Assert.assertNull(dao.getPeakHourMapping(DayOfWeek.MONDAY));
    }

    @Test
    public void testAddPeakHourForDayOfWeek_KeyNotPresent() {
        dao.addPeakHourForDayOfWeek(DayOfWeek.MONDAY, peakHourList);
        Assert.assertNull(dao.getPeakHourMapping(DayOfWeek.TUESDAY));
    }

    @Test
    public void testDeletePeakHourForDayOfWeek() {
        dao.addPeakHourForDayOfWeek(DayOfWeek.MONDAY, peakHourList);
        dao.deletePeakHourForDayOfWeek(DayOfWeek.MONDAY, peakHourList.get(0));
        List<PeakHour> peakHours = dao.getPeakHourMapping(DayOfWeek.MONDAY);
        Assert.assertNotNull(peakHours);
        Assert.assertEquals(2, peakHours.size());
    }
}