package com.nepu.metro.rules;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.nepu.metro.vo.Contact;
import com.nepu.metro.vo.Fare;
import com.nepu.metro.vo.FareCalculatorContext;
import com.nepu.metro.vo.Journey;
import com.nepu.metro.vo.TigerCard;
import com.nepu.metro.vo.Zone;

public class DailyCapRuleEvaluatorTest {

    DailyCapRuleEvaluator ruleEvaluator;

    FareCalculatorContext context;

    Fare fare;

    @Before
    public void setUp() {
        ruleEvaluator = new DailyCapRuleEvaluator(null);

        TigerCard card = new TigerCard("12345", new Contact());
        Zone zone1 = new Zone("1", "Zone-1");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Journey journey1 = new Journey(LocalDateTime.parse("2022-01-07 10:20:00", formatter), zone1, zone1);

        fare = new Fare(10);
        context = new FareCalculatorContext(card, journey1);
        context.setTotalDailyFareAmount(0);
        context.setTotalWeeklyFareAmount(0);
        context.setApplicableDailyLimit(10);
        context.setApplicableWeeklyLimit(100);
    }

    @Test
    public void testDoEvaluate_NoChange() {
        ruleEvaluator.doEvaluate(context, fare);
        Assert.assertEquals(10, fare.getAmount());

    }

    @Test
    public void testDoEvaluate_DailyCapReached() {
        context.setTotalDailyFareAmount(10);
        ruleEvaluator.doEvaluate(context, fare);
        Assert.assertEquals(0, fare.getAmount());
    }

    @Test
    public void testDoEvaluate_FareLargerThanDailyCap() {
        Fare newFare = new Fare(10);
        context.setTotalDailyFareAmount(95);
        context.setApplicableDailyLimit(100);
        ruleEvaluator.doEvaluate(context, newFare);
        Assert.assertEquals(5, newFare.getAmount());
    }
}