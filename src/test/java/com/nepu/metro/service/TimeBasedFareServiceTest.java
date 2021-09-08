package com.nepu.metro.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.nepu.metro.dao.TimeBasedFareDao;
import com.nepu.metro.util.Util;
import com.nepu.metro.vo.Fare;
import com.nepu.metro.vo.FareCategory;
import com.nepu.metro.vo.Journey;
import com.nepu.metro.vo.PeakHour;
import com.nepu.metro.vo.Zone;

public class TimeBasedFareServiceTest {

    @Mock
    private TimeBasedFareDao timeBasedFareDao;

    @Mock
    private PeakHourTimingService peakHourTimingService;

    private TimeBasedFareService timeBasedFareService;

    private Journey journey1, journey2;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        timeBasedFareService = new TimeBasedFareService(timeBasedFareDao, peakHourTimingService);

        Zone zone1 = new Zone("1", "Zone-1");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        journey1 = new Journey(LocalDateTime.parse("2022-01-07 10:00:00", formatter), zone1, zone1);
        journey2 = new Journey(LocalDateTime.parse("2022-01-07 12:20:00", formatter), zone1, zone1);

        String key = Util.getKeyFromZones(journey1.getFromZone(), journey1.getToZone());
        Map<FareCategory, Fare> testMap = new HashMap<>();
        testMap.put(FareCategory.PEAK_HOURS, new Fare(1));
        testMap.put(FareCategory.OFF_PEAK_HOURS, new Fare(2));

        Mockito.when(timeBasedFareDao.getFareForKey(key)).thenReturn(testMap);

        List<PeakHour> peakHourList = new ArrayList<>();
        peakHourList.add(new PeakHour(LocalTime.of(9, 0), LocalTime.of(10, 30)));
        Mockito.when(peakHourTimingService.getPeakHourTimings(journey1.getDatetime().getDayOfWeek())).thenReturn(peakHourList);
    }

    @Test
    public void testGetTimeBasedFare_isPeakHour() {
        Fare fare = timeBasedFareService.getTimeBasedFare(journey1);
        Assert.assertNotNull(fare);
        Assert.assertEquals(1, fare.getAmount());
    }

    @Test
    public void testGetTimeBasedFare_isOffPeakHour() {
        Fare fare = timeBasedFareService.getTimeBasedFare(journey2);
        Assert.assertNotNull(fare);
        Assert.assertEquals(2, fare.getAmount());
    }

}