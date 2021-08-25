package com.nepu.metro.rules;

import com.nepu.metro.vo.Fare;
import com.nepu.metro.vo.FareCalculatorContext;

public class WeeklyCapRuleEvaluator extends AbstractRuleEvaluator {

    WeeklyCapRuleEvaluator(RuleEvaluator nextRuleEvaluator) {
        super(nextRuleEvaluator);
    }

    @Override
    public void doEvaluate(FareCalculatorContext context, Fare fare) {
        int calculatedAmount = fare.getAmount();

        int weeklyAmount = context.getTotalWeeklyFareAmount();
        int weeklyCap = context.getApplicableWeeklyLimit();

        if (weeklyAmount == weeklyCap) {
            calculatedAmount = 0;
        } else if (weeklyAmount < weeklyCap
                && weeklyAmount + calculatedAmount > weeklyCap) {
            calculatedAmount = weeklyCap - weeklyAmount;
        }

        if (calculatedAmount != fare.getAmount()) {
            fare.setAmount(calculatedAmount);
            return;
        }
    }
}
