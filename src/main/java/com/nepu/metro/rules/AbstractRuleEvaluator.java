package com.nepu.metro.rules;

import com.nepu.metro.vo.Fare;
import com.nepu.metro.vo.FareCalculatorContext;

public abstract class AbstractRuleEvaluator implements RuleEvaluator {

    private RuleEvaluator nextRuleEvaluator;

    AbstractRuleEvaluator(RuleEvaluator nextRuleEvaluator) {
        this.nextRuleEvaluator = nextRuleEvaluator;
    }

    public void evaluate(FareCalculatorContext context, Fare fare) {
        doEvaluate(context, fare);
        evaluateNext(context, fare);
    }

    public abstract void doEvaluate(FareCalculatorContext context, Fare fare);

    private void evaluateNext(FareCalculatorContext context, Fare fare) {
        if (nextRuleEvaluator != null) {
            nextRuleEvaluator.evaluate(context, fare);
        }
    }
}
