package com.nepu.metro.rules;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RuleEvaluatorChainBuilderTest {

    RuleEvaluatorChainBuilder builder;

    @Before
    public void setUp() {
        builder = new RuleEvaluatorChainBuilder();
    }

    @Test
    public void build() {
        RuleEvaluator evaluator = builder.build();
        Assert.assertNotNull(evaluator);
        Assert.assertTrue(evaluator instanceof RuleEvaluator);
        Assert.assertTrue(evaluator instanceof RuleEvaluator);
    }
}