package com.nepu.metro.integ;

import java.time.DayOfWeek;
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

import com.nepu.metro.dao.FareCappingLimitsDao;
import com.nepu.metro.dao.FareCappingLimitsDapImpl;
import com.nepu.metro.dao.JourneyFareMappingDao;
import com.nepu.metro.dao.JourneyFareMappingDaoImpl;
import com.nepu.metro.dao.PeakHourTimingDao;
import com.nepu.metro.dao.PeakHourTimingDaoImpl;
import com.nepu.metro.dao.TimeBasedFareDao;
import com.nepu.metro.dao.TimeBasedFareDaoImpl;
import com.nepu.metro.service.FareCalculatorService;
import com.nepu.metro.service.FareCappingLimitsService;
import com.nepu.metro.service.JourneyFareMappingService;
import com.nepu.metro.service.PeakHourTimingService;
import com.nepu.metro.service.TimeBasedFareService;
import com.nepu.metro.util.Util;
import com.nepu.metro.vo.CappingCategory;
import com.nepu.metro.vo.Contact;
import com.nepu.metro.vo.Fare;
import com.nepu.metro.vo.FareCategory;
import com.nepu.metro.vo.Journey;
import com.nepu.metro.vo.PeakHour;
import com.nepu.metro.vo.TigerCard;
import com.nepu.metro.vo.Zone;

public class IntegrationTest {

    private TigerCard card1;

    private Zone zone1, zone2;

    private FareCalculatorService fareCalculatorService;

    private DateTimeFormatter formatter;

    @Before
    public void setUp() {
        initialize();
    }

    @Test
    public void testOneDayFareCalculation_dailyCapReached() {
        int totalFare = 0;
        for (Journey journey : getTestDataForOneDay()) {
            Fare fare = fareCalculatorService.calculateFare(card1, journey);
            totalFare += fare.getAmount();
        }
        Assert.assertEquals(120, totalFare);
    }

    @Test
    public void testMultipleDayCalculation_weeklyCapReached() {
        int totalFare = 0;
        for (Journey journey : getTestData()) {
            Fare fare = fareCalculatorService.calculateFare(card1, journey);
            totalFare += fare.getAmount();
        }
        Assert.assertEquals(700, totalFare);
    }

    @Test
    public void testOneDayFareCalculation_dailyCapReached_onlyZone2() {
        int totalFare = 0;
        List<Journey> data = new ArrayList<>();

        // Monday  2-2 80
        data.add(new Journey(LocalDateTime.parse("2022-01-03 10:20:00", formatter), zone2, zone2));
        data.add(new Journey(LocalDateTime.parse("2022-01-03 10:45:00", formatter), zone2, zone2));
        data.add(new Journey(LocalDateTime.parse("2022-01-03 16:15:00", formatter), zone2, zone2));
        data.add(new Journey(LocalDateTime.parse("2022-01-03 18:15:00", formatter), zone2, zone2));
        data.add(new Journey(LocalDateTime.parse("2022-01-03 19:00:00", formatter), zone2, zone2));

        for (Journey journey : data) {
            Fare fare = fareCalculatorService.calculateFare(card1, journey);
            totalFare += fare.getAmount();
        }
        Assert.assertEquals(80, totalFare);
    }

    private List<Journey> getTestDataForOneDay() {
        List<Journey> data = new ArrayList<>();

        // Monday  1-2 120
        data.add(new Journey(LocalDateTime.parse("2022-01-03 10:20:00", formatter), zone2, zone1));
        data.add(new Journey(LocalDateTime.parse("2022-01-03 10:45:00", formatter), zone1, zone1));
        data.add(new Journey(LocalDateTime.parse("2022-01-03 16:15:00", formatter), zone1, zone1));
        data.add(new Journey(LocalDateTime.parse("2022-01-03 18:15:00", formatter), zone1, zone1));
        data.add(new Journey(LocalDateTime.parse("2022-01-03 19:00:00", formatter), zone1, zone2));

        return data;
    }

    private List<Journey> getTestData() {
        List<Journey> data = new ArrayList<>();

        // Monday  1-2 120
        data.add(new Journey(LocalDateTime.parse("2022-01-03 10:20:00", formatter), zone2, zone1));
        data.add(new Journey(LocalDateTime.parse("2022-01-03 10:45:00", formatter), zone1, zone1));
        data.add(new Journey(LocalDateTime.parse("2022-01-03 16:15:00", formatter), zone1, zone1));
        data.add(new Journey(LocalDateTime.parse("2022-01-03 18:15:00", formatter), zone1, zone1));
        data.add(new Journey(LocalDateTime.parse("2022-01-03 19:00:00", formatter), zone1, zone2));

        // Tuesday 1-2 120
        data.add(new Journey(LocalDateTime.parse("2022-01-04 10:20:00", formatter), zone2, zone1));
        data.add(new Journey(LocalDateTime.parse("2022-01-04 10:45:00", formatter), zone1, zone1));
        data.add(new Journey(LocalDateTime.parse("2022-01-04 16:15:00", formatter), zone1, zone1));
        data.add(new Journey(LocalDateTime.parse("2022-01-04 18:15:00", formatter), zone1, zone1));
        data.add(new Journey(LocalDateTime.parse("2022-01-04 19:00:00", formatter), zone1, zone2));

        // Wed 1-2 120
        data.add(new Journey(LocalDateTime.parse("2022-01-05 10:20:00", formatter), zone2, zone1));
        data.add(new Journey(LocalDateTime.parse("2022-01-05 10:45:00", formatter), zone1, zone1));
        data.add(new Journey(LocalDateTime.parse("2022-01-05 16:15:00", formatter), zone1, zone1));
        data.add(new Journey(LocalDateTime.parse("2022-01-05 18:15:00", formatter), zone1, zone1));
        data.add(new Journey(LocalDateTime.parse("2022-01-05 19:00:00", formatter), zone1, zone2));

        // Thursday 1-2 120
        data.add(new Journey(LocalDateTime.parse("2022-01-06 10:20:00", formatter), zone2, zone1));
        data.add(new Journey(LocalDateTime.parse("2022-01-06 10:45:00", formatter), zone1, zone1));
        data.add(new Journey(LocalDateTime.parse("2022-01-06 16:15:00", formatter), zone1, zone1));
        data.add(new Journey(LocalDateTime.parse("2022-01-06 18:15:00", formatter), zone1, zone1));
        data.add(new Journey(LocalDateTime.parse("2022-01-06 19:00:00", formatter), zone1, zone2));

        // Friday 1-1 80
        data.add(new Journey(LocalDateTime.parse("2022-01-07 10:20:00", formatter), zone1, zone1));
        data.add(new Journey(LocalDateTime.parse("2022-01-07 10:45:00", formatter), zone1, zone1));
        data.add(new Journey(LocalDateTime.parse("2022-01-07 16:15:00", formatter), zone1, zone1));

        // Sat 1-2 40
        data.add(new Journey(LocalDateTime.parse("2022-01-08 10:20:00", formatter), zone2, zone1));
        data.add(new Journey(LocalDateTime.parse("2022-01-08 10:45:00", formatter), zone1, zone1));

        // Sun 1-2 0
        data.add(new Journey(LocalDateTime.parse("2022-01-09 10:20:00", formatter), zone2, zone1));
        data.add(new Journey(LocalDateTime.parse("2022-01-09 10:45:00", formatter), zone1, zone1));

        // Mon 1-2 100
        data.add(new Journey(LocalDateTime.parse("2022-01-10 10:20:00", formatter), zone2, zone1));
        data.add(new Journey(LocalDateTime.parse("2022-01-10 10:00:00", formatter), zone1, zone2));
        data.add(new Journey(LocalDateTime.parse("2022-01-10 16:15:00", formatter), zone2, zone1));

        return data;
    }

    private void initialize() {
        Contact c1 = new Contact();
        c1.setName("Varun Sharma");
        c1.setPhoneNumber("9876543210");
        c1.setAddress("Nepu city");
        card1 = new TigerCard("12345", c1);

        zone1 = new Zone("1", "Zone-1");
        zone2 = new Zone("2", "Zone-2");

        PeakHourTimingDao peakHourTimingDao = new PeakHourTimingDaoImpl();
        initializePeakHourTimings(peakHourTimingDao);
        PeakHourTimingService peakHourTimingService = new PeakHourTimingService(peakHourTimingDao);

        FareCappingLimitsDao fareCappingLimitsDao = new FareCappingLimitsDapImpl();
        initializeFareCappingLimits(fareCappingLimitsDao);
        FareCappingLimitsService fareCappingLimitsService = new FareCappingLimitsService(fareCappingLimitsDao);

        TimeBasedFareDao timeBasedFareDao = new TimeBasedFareDaoImpl();
        initializeTimeBasedFares(timeBasedFareDao);
        TimeBasedFareService timeBasedFareService = new TimeBasedFareService(timeBasedFareDao, peakHourTimingService);

        JourneyFareMappingDao journeyFareMappingDao = new JourneyFareMappingDaoImpl();
        JourneyFareMappingService journeyFareMappingService = new JourneyFareMappingService(journeyFareMappingDao);

        fareCalculatorService = new FareCalculatorService();
        fareCalculatorService.setFareCappingLimitsService(fareCappingLimitsService);
        fareCalculatorService.setFareMappingService(journeyFareMappingService);
        fareCalculatorService.setTimeBasedFareService(timeBasedFareService);

        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    private void initializePeakHourTimings(PeakHourTimingDao peakHourTimingDao) {
        for (int i = 1; i <= 7; i++) {
            List<PeakHour> timingList = new ArrayList<>();
            if (i < 6) {
                // weekdays peak hours: 07:00 - 10:30, 17:00 - 20:00
                timingList.add(new PeakHour(LocalTime.of(7, 0), LocalTime.of(10, 30)));
                timingList.add(new PeakHour(LocalTime.of(17, 0), LocalTime.of(20, 0)));
            } else {
                // weekends peak hours: 09:00 - 11:00, 18:00 - 22:00
                timingList.add(new PeakHour(LocalTime.of(9, 0), LocalTime.of(11, 0)));
                timingList.add(new PeakHour(LocalTime.of(18, 0), LocalTime.of(22, 0)));
            }

            peakHourTimingDao.addPeakHourForDayOfWeek(DayOfWeek.of(i), timingList);
        }
    }

    private void initializeFareCappingLimits(FareCappingLimitsDao fareCappingLimitsDao) {
        Map<CappingCategory, Fare> zone11FareCappingMap = new HashMap<>();
        zone11FareCappingMap.put(CappingCategory.DAILY, new Fare(100));
        zone11FareCappingMap.put(CappingCategory.WEEKLY, new Fare(500));
        fareCappingLimitsDao.addFareCappingLimitsForKey(Util.getKeyFromZones(zone1, zone1), zone11FareCappingMap);

        Map<CappingCategory, Fare> zone12FareCappingMap = new HashMap<>();
        zone12FareCappingMap.put(CappingCategory.DAILY, new Fare(120));
        zone12FareCappingMap.put(CappingCategory.WEEKLY, new Fare(600));
        fareCappingLimitsDao.addFareCappingLimitsForKey(Util.getKeyFromZones(zone1, zone2), zone12FareCappingMap);

        Map<CappingCategory, Fare> zone22FareCappingMap = new HashMap<>();
        zone22FareCappingMap.put(CappingCategory.DAILY, new Fare(80));
        zone22FareCappingMap.put(CappingCategory.WEEKLY, new Fare(400));
        fareCappingLimitsDao.addFareCappingLimitsForKey(Util.getKeyFromZones(zone2, zone2), zone22FareCappingMap);
    }

    private void initializeTimeBasedFares(TimeBasedFareDao timeBasedFareDao) {
        Map<FareCategory, Fare> zone11FareMap = new HashMap<>();
        zone11FareMap.put(FareCategory.PEAK_HOURS, new Fare(30));
        zone11FareMap.put(FareCategory.OFF_PEAK_HOURS, new Fare(25));
        timeBasedFareDao.addFareForKey(Util.getKeyFromZones(zone1, zone1), zone11FareMap);

        Map<FareCategory, Fare> zone12FareMap = new HashMap<>();
        zone12FareMap.put(FareCategory.PEAK_HOURS, new Fare(35));
        zone12FareMap.put(FareCategory.OFF_PEAK_HOURS, new Fare(30));
        timeBasedFareDao.addFareForKey(Util.getKeyFromZones(zone1, zone2), zone12FareMap);

        Map<FareCategory, Fare> zone22FareMap = new HashMap<>();
        zone22FareMap.put(FareCategory.PEAK_HOURS, new Fare(25));
        zone22FareMap.put(FareCategory.OFF_PEAK_HOURS, new Fare(20));
        timeBasedFareDao.addFareForKey(Util.getKeyFromZones(zone2, zone2), zone22FareMap);
    }

}
