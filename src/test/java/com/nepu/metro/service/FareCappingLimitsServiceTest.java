package com.nepu.metro.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.nepu.metro.dao.FareCappingLimitsDao;
import com.nepu.metro.util.Util;
import com.nepu.metro.vo.CappingCategory;
import com.nepu.metro.vo.DailyFare;
import com.nepu.metro.vo.Fare;
import com.nepu.metro.vo.Journey;
import com.nepu.metro.vo.WeeklyFare;
import com.nepu.metro.vo.Zone;

public class FareCappingLimitsServiceTest {

    @Mock
    private FareCappingLimitsDao fareCappingLimitsDao;

    private FareCappingLimitsService service;

    private Journey journey1, journey2;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new FareCappingLimitsService(fareCappingLimitsDao);

        Zone zone1 = new Zone("1", "Zone-1");
        Zone zone2 = new Zone("2", "Zone-2");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        journey1 = new Journey(LocalDateTime.parse("2022-01-07 10:20:00", formatter), zone1, zone1);
        journey2 = new Journey(LocalDateTime.parse("2022-02-07 10:20:00", formatter), zone2, zone2);

        String key = Util.getKeyFromZones(journey1.getFromZone(), journey1.getToZone());

        Map<CappingCategory, Fare> testMap = new HashMap<>();
        testMap.put(CappingCategory.DAILY, new Fare(1));
        testMap.put(CappingCategory.WEEKLY, new Fare(2));

        Mockito.when(fareCappingLimitsDao.getFareCappingLimitsForKey(key)).thenReturn(testMap);
    }

    @Test
    public void testGetDailyCappingLimit() {
        Fare fare = service.getDailyCappingLimit(journey1);
        Assert.assertNotNull(fare);
        Assert.assertEquals(1, fare.getAmount());
    }

    @Test
    public void testGetDailyCappingLimit_Null() {
        Fare fare = service.getDailyCappingLimit(journey2);
        Assert.assertNull(fare);
    }

    @Test
    public void testGetWeeklyCappingLimit() {
        Fare fare = service.getWeeklyCappingLimit(journey1);
        Assert.assertNotNull(fare);
        Assert.assertEquals(2, fare.getAmount());
    }

    @Test
    public void testGetWeeklyCappingLimit_Null() {
        Fare fare = service.getWeeklyCappingLimit(journey2);
        Assert.assertNull(fare);
    }

    @Test
    public void testCalculateDailyCapLimit() {
        DailyFare dailyFare = new DailyFare();
        dailyFare.setApplicableDailyCap(10);
        Assert.assertEquals(10, service.calculateDailyCappingLimit(journey1, dailyFare));
    }

    @Test
    public void testCalculateDailyCapLimit_fromCache() {
        DailyFare dailyFare = new DailyFare();
        dailyFare.setApplicableDailyCap(0);
        Assert.assertEquals(1, service.calculateDailyCappingLimit(journey1, dailyFare));
    }

    @Test
    public void testCalculateWeeklyCapLimit() {
        WeeklyFare weeklyFare = new WeeklyFare();
        weeklyFare.setApplicableWeeklyCap(100);
        Assert.assertEquals(100, service.calculateWeeklyCappingLimit(journey1, weeklyFare));
    }

    @Test
    public void testCalculateWeeklyCapLimit_fromCache() {
        WeeklyFare weeklyFare = new WeeklyFare();
        weeklyFare.setApplicableWeeklyCap(1);
        Assert.assertEquals(2, service.calculateWeeklyCappingLimit(journey1, weeklyFare));
    }
}