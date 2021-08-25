package com.nepu.metro.rules;

public class RuleEvaluatorChainBuilder {

    private RuleEvaluator weeklyCapRuleEvaluator;

    private RuleEvaluator dailyCapRuleEvaluator;

    public RuleEvaluator build() {
        RuleEvaluator head;

        dailyCapRuleEvaluator = new DailyCapRuleEvaluator(null);
        weeklyCapRuleEvaluator = new WeeklyCapRuleEvaluator(dailyCapRuleEvaluator);

        head = weeklyCapRuleEvaluator;
        return head;
    }
}
