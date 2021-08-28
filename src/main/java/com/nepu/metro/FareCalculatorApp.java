package com.nepu.metro;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.nepu.metro.vo.DailyFare;
import com.nepu.metro.vo.Fare;
import com.nepu.metro.vo.FareCalculatorContext;
import com.nepu.metro.vo.FareCategory;
import com.nepu.metro.vo.Journey;
import com.nepu.metro.vo.PeakHour;
import com.nepu.metro.vo.TigerCard;
import com.nepu.metro.vo.WeeklyFare;
import com.nepu.metro.vo.Zone;

public class FareCalculatorApp {

    TigerCard card1;

    Zone zone1, zone2;

    PeakHourTimingService peakHourTimingService;

    FareCappingLimitsService fareCappingLimitsService;

    TimeBasedFareService timeBasedFareService;

    JourneyFareMappingService journeyFareMappingService;

    FareCalculatorService fareCalculatorService;

    DateTimeFormatter formatter;

    public static void main(String[] args) {

        FareCalculatorApp app = new FareCalculatorApp();
        app.initialize();
        app.calculateFare();

    }

    /**
     * Main logic:
     * 1. fetch prev daily and weekly fares for journey's day and week if present
     * 2. getTimeBasedFare
     * 3. calculate and set daily and weekly limits
     * 4. Run rule engine:
     *   - i. weekly rule:
     *     - if prevWeekly == weeklyCap, fare = 0
     *     - if prevWeekly + fare > weeklyCap, fare = weeklyCap - prevWeekly
     *   - ii. daily rule:
     *     - if prevDaily == dailyCap, fare = 0
     *     - if prevDaily + fare > dailyCap, fare = dailyCap - prevDaily
     * 5. save the fare, daily and weekly caps and total amounts in a cache
     *    against the card (to be used in step 1 for next journeys)
     */

    private void calculateFare() {
        int totalFare = 0;
        for (Journey journey : getTestData()) {
            Fare fare = fareCalculatorService.calculateFare(card1, journey);
            totalFare += fare.getAmount();
        }
        System.out.println("TotalFare = " + totalFare);
    }

    private void calculateFareOld() {
        int totalFare = 0;
        for (Journey journey : getTestData()) {
            DailyFare prevDaily = journeyFareMappingService.getExistingFareForDay(card1, journey.getDatetime());
            WeeklyFare prevWeekly = journeyFareMappingService.getExistingFareForWeek(card1, journey.getDatetime());

            Fare calculatedFare = timeBasedFareService.getTimeBasedFare(journey);
            int calculatedAmount = calculatedFare.getAmount();
            FareCalculatorContext context = new FareCalculatorContext(card1, journey);

            // calculate and set applicable daily cap
            int dailyCap = 0;
            int dailyAmount = 0;
            if (prevDaily != null) {
                dailyCap = prevDaily.getApplicableDailyCap();
                dailyAmount = prevDaily.getTotalFare();
            }
            dailyCap = Math.max(dailyCap, fareCappingLimitsService.getDailyCappingLimit(journey).getAmount());
            context.setApplicableDailyLimit(dailyCap);

            // calculate and set applicable weekly cap
            int weeklyCap = 0;
            int weeklyAmount = 0;
            if (prevWeekly != null) {
                weeklyCap = prevWeekly.getApplicableWeeklyCap();
                weeklyAmount = prevWeekly.getTotalWeeklyFare();
            }
            weeklyCap = Math.max(weeklyCap, fareCappingLimitsService.getWeeklyCappingLimit(journey).getAmount());
            context.setApplicableWeeklyLimit(weeklyCap);

            if (weeklyAmount == weeklyCap) {
                calculatedAmount = 0;
            } else if (weeklyAmount < weeklyCap
                    && weeklyAmount + calculatedAmount > weeklyCap) {
                calculatedAmount = weeklyCap - weeklyAmount;
            }

            if (dailyAmount == dailyCap) {
                calculatedAmount = 0;
            } else if (dailyAmount < dailyCap && dailyAmount + calculatedAmount > dailyCap) {
                calculatedAmount = dailyCap - dailyAmount;
            }

            if (calculatedAmount != calculatedFare.getAmount()) {
                calculatedFare = new Fare(calculatedAmount);
            }

            // save total daily and weekly amounts
            context.setFare(calculatedFare);
            context.setTotalDailyFareAmount(dailyAmount + calculatedAmount);
            context.setTotalWeeklyFareAmount(weeklyAmount + calculatedAmount);

            System.out.println("calculatedAmount = " + calculatedAmount);
            System.out.println("total Daily amount for " + Util.getDailyKeyFromDateTime(journey.getDatetime()) + " = " + (dailyAmount + calculatedAmount));
            totalFare += calculatedFare.getAmount();
            journeyFareMappingService.saveFareForJourney(context);
        }

        System.out.println("TotalFare = " + totalFare);
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
        peakHourTimingService = new PeakHourTimingService(peakHourTimingDao);

        FareCappingLimitsDao fareCappingLimitsDao = new FareCappingLimitsDapImpl();
        initializeFareCappingLimits(fareCappingLimitsDao);
        fareCappingLimitsService = new FareCappingLimitsService(fareCappingLimitsDao);

        TimeBasedFareDao timeBasedFareDao = new TimeBasedFareDaoImpl();
        initializeTimeBasedFares(timeBasedFareDao);
        timeBasedFareService = new TimeBasedFareService(timeBasedFareDao, peakHourTimingService);

        JourneyFareMappingDao journeyFareMappingDao = new JourneyFareMappingDaoImpl();
        journeyFareMappingService = new JourneyFareMappingService(journeyFareMappingDao);

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
