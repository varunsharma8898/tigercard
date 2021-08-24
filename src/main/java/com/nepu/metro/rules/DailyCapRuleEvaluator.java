package com.nepu.metro.rules;

import com.nepu.metro.vo.Fare;
import com.nepu.metro.vo.FareCalculatorContext;

public class DailyCapRuleEvaluator extends AbstractRuleEvaluator {

    DailyCapRuleEvaluator(RuleEvaluator nextRuleEvaluator) {
        super(nextRuleEvaluator);
    }

    @Override
    public void doEvaluate(FareCalculatorContext context, Fare fare) {
        int calculatedAmount = fare.getAmount();

        int dailyAmount = context.getTotalDailyFareAmount();
        int dailyCap = context.getApplicableDailyLimit();

        if (0 < dailyCap && dailyAmount == dailyCap) {
            calculatedAmount = 0;
        } else if (dailyAmount < dailyCap && dailyAmount + calculatedAmount > dailyCap) {
            calculatedAmount = dailyCap - dailyAmount;
        }

        if (calculatedAmount != fare.getAmount()) {
            fare.setAmount(calculatedAmount);
            return;
        }
    }
}
