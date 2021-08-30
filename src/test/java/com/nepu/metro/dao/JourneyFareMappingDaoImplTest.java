package com.nepu.metro.dao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.nepu.metro.vo.Contact;
import com.nepu.metro.vo.DailyFare;
import com.nepu.metro.vo.Fare;
import com.nepu.metro.vo.FareCalculatorContext;
import com.nepu.metro.vo.Journey;
import com.nepu.metro.vo.TigerCard;
import com.nepu.metro.vo.WeeklyFare;
import com.nepu.metro.vo.Zone;

public class JourneyFareMappingDaoImplTest {

    JourneyFareMappingDao dao;

    FareCalculatorContext context;

    Journey journey1, journey2;

    @Before
    public void setUp() {
        dao = new JourneyFareMappingDaoImpl();

        TigerCard card = new TigerCard("12345", new Contact());
        Zone zone1 = new Zone("1", "Zone-1");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        journey1 = new Journey(LocalDateTime.parse("2022-01-07 10:20:00", formatter), zone1, zone1);
        journey2 = new Journey(LocalDateTime.parse("2022-02-08 10:20:00", formatter), zone1, zone1);

        context = new FareCalculatorContext(card, journey1);
        context.setFare(new Fare(10));
        context.setTotalDailyFareAmount(10);
        context.setTotalWeeklyFareAmount(10);
        context.setApplicableDailyLimit(50);
        context.setApplicableWeeklyLimit(100);
    }

    @Test
    public void testSaveFareForJourney() {
        dao.saveFareForJourney(context);

        WeeklyFare weeklyFare = dao.getExistingFareForWeek(context.getTigerCard(), journey1.getDatetime());

        Assert.assertNotNull(weeklyFare);
        Assert.assertEquals(10, weeklyFare.getTotalWeeklyFare());
        Assert.assertEquals(100, weeklyFare.getApplicableWeeklyCap());
        Assert.assertEquals(1, weeklyFare.getDailyFareMap().size());
        Assert.assertEquals(10, weeklyFare.getDailyFareMap().get("2022-01-07").getTotalFare());
        Assert.assertEquals(50, weeklyFare.getDailyFareMap().get("2022-01-07").getApplicableDailyCap());
    }

    @Test
    public void testGetExistingFareForDay() {
        dao.saveFareForJourney(context);

        DailyFare dailyFare = dao.getExistingFareForDay(context.getTigerCard(), journey1.getDatetime());

        Assert.assertEquals(10, dailyFare.getTotalFare());
        Assert.assertEquals(50, dailyFare.getApplicableDailyCap());
    }

    @Test
    public void testGetExistingFareForDay_NullEntries() {
        DailyFare dailyFare = dao.getExistingFareForDay(context.getTigerCard(), context.getJourney().getDatetime());
        Assert.assertNull(dailyFare);

        dao.saveFareForJourney(context);
        dailyFare = dao.getExistingFareForDay(context.getTigerCard(), journey2.getDatetime());
        Assert.assertNull(dailyFare);
    }

    @Test
    public void testGetExistingFareForWeek() {
        dao.saveFareForJourney(context);

        WeeklyFare weeklyFare = dao.getExistingFareForWeek(context.getTigerCard(), journey1.getDatetime());

        Assert.assertNotNull(weeklyFare);
        Assert.assertEquals(10, weeklyFare.getTotalWeeklyFare());
        Assert.assertEquals(100, weeklyFare.getApplicableWeeklyCap());
        Assert.assertEquals(1, weeklyFare.getDailyFareMap().size());
    }

    @Test
    public void testGetExistingFareForWeek_NulEntries() {
        WeeklyFare weeklyFare = dao.getExistingFareForWeek(context.getTigerCard(), journey1.getDatetime());
        Assert.assertNull(weeklyFare);

        dao.saveFareForJourney(context);
        weeklyFare = dao.getExistingFareForWeek(context.getTigerCard(), journey2.getDatetime());
        Assert.assertNull(weeklyFare);
    }
}