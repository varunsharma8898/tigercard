package com.nepu.metro.rules;

import com.nepu.metro.vo.Fare;
import com.nepu.metro.vo.FareCalculatorContext;

public interface RuleEvaluator {

    void evaluate(FareCalculatorContext context, Fare fare);
}
