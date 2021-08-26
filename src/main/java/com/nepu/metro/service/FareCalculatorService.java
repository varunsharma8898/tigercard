package com.nepu.metro.service;

import com.nepu.metro.rules.RuleEvaluator;
import com.nepu.metro.rules.RuleEvaluatorChainBuilder;
import com.nepu.metro.vo.DailyFare;
import com.nepu.metro.vo.Fare;
import com.nepu.metro.vo.FareCalculatorContext;
import com.nepu.metro.vo.Journey;
import com.nepu.metro.vo.TigerCard;
import com.nepu.metro.vo.WeeklyFare;

public class FareCalculatorService {

    private RuleEvaluator ruleEvaluator;

    private JourneyFareMappingService fareMappingService;

    private TimeBasedFareService timeBasedFareService;

    private FareCappingLimitsService fareCappingLimitsService;

    public FareCalculatorService() {
        this.ruleEvaluator = new RuleEvaluatorChainBuilder().build();
    }

    public Fare calculateFare(TigerCard card, Journey journey) {

        FareCalculatorContext context = new FareCalculatorContext(card, journey);

        DailyFare prevDaily = fareMappingService.getExistingFareForDay(card, journey.getDatetime());
        WeeklyFare prevWeekly = fareMappingService.getExistingFareForWeek(card, journey.getDatetime());

        int dailyCap = fareCappingLimitsService.calculateDailyCappingLimit(journey, prevDaily);
        int dailyAmount = 0;
        if (prevDaily != null) {
            dailyAmount = prevDaily.getTotalFare();
        }

        int weeklyCap = fareCappingLimitsService.calculateWeeklyCappingLimit(journey, prevWeekly);
        int weeklyAmount = 0;
        if (prevWeekly != null) {
            weeklyAmount = prevWeekly.getTotalWeeklyFare();
        }

        // variables needed for ruleEvaluator
        context.setApplicableDailyLimit(dailyCap);
        context.setApplicableWeeklyLimit(weeklyCap);
        context.setTotalDailyFareAmount(dailyAmount);
        context.setTotalWeeklyFareAmount(weeklyAmount);

        Fare calculatedFare = timeBasedFareService.getTimeBasedFare(journey);

        // run the rule engine
        ruleEvaluator.evaluate(context, calculatedFare);

        // save total daily and weekly amounts
        context.setFare(calculatedFare);
        context.setTotalDailyFareAmount(dailyAmount + calculatedFare.getAmount());
        context.setTotalWeeklyFareAmount(weeklyAmount + calculatedFare.getAmount());

//        System.out.println("calculatedAmount = " + calculatedFare.getAmount());
//        System.out.println("Total Daily amount for " + Util.getDailyKeyFromDateTime(journey.getDatetime()) + " = " + (context.getTotalDailyFareAmount()));
//        System.out.println("Daily Cap for " + Util.getDailyKeyFromDateTime(journey.getDatetime()) + " = " + (context.getApplicableDailyLimit()));
//        System.out.println("Total Weekly amount for " + Util.getDailyKeyFromDateTime(journey.getDatetime()) + " = " + (context.getTotalWeeklyFareAmount()));
//        System.out.println("Weekly Cap for " + Util.getDailyKeyFromDateTime(journey.getDatetime()) + " = " + (context.getApplicableWeeklyLimit()));
        fareMappingService.saveFareForJourney(context);

        return calculatedFare;
    }

    public JourneyFareMappingService getFareMappingService() {
        return fareMappingService;
    }

    public void setFareMappingService(JourneyFareMappingService fareMappingService) {
        this.fareMappingService = fareMappingService;
    }

    public TimeBasedFareService getTimeBasedFareService() {
        return timeBasedFareService;
    }

    public void setTimeBasedFareService(TimeBasedFareService timeBasedFareService) {
        this.timeBasedFareService = timeBasedFareService;
    }

    public FareCappingLimitsService getFareCappingLimitsService() {
        return fareCappingLimitsService;
    }

    public void setFareCappingLimitsService(FareCappingLimitsService fareCappingLimitsService) {
        this.fareCappingLimitsService = fareCappingLimitsService;
    }
}
