package com.nepu.metro.dao;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.nepu.metro.vo.CappingCategory;
import com.nepu.metro.vo.Fare;

public class FareCappingLimitsDapImplTest {

    FareCappingLimitsDao fareCappingLimitsDao;

    Map<CappingCategory, Fare> testMap;

    private static final String TEST_STR_1 = "test-string-1";

    private static final String TEST_STR_2 = "test-string-2";

    @Before
    public void setUp() {
        fareCappingLimitsDao = new FareCappingLimitsDapImpl();
        testMap = new HashMap<>();
        testMap.put(CappingCategory.DAILY, new Fare(1));
        testMap.put(CappingCategory.WEEKLY, new Fare(2));
    }

    @Test
    public void testGetFareCappingLimitsForKey() {
        fareCappingLimitsDao.addFareCappingLimitsForKey(TEST_STR_1, testMap);
        Assert.assertNotNull(fareCappingLimitsDao.getFareCappingLimitsForKey(TEST_STR_1));
    }

    @Test
    public void testGetFareCappingLimitsForKeyNotPresent() {
        fareCappingLimitsDao.addFareCappingLimitsForKey(TEST_STR_1, testMap);
        Assert.assertNull(fareCappingLimitsDao.getFareCappingLimitsForKey(TEST_STR_2));
    }

    @Test
    public void testAddFareCappingLimitsForKey() {
        fareCappingLimitsDao.addFareCappingLimitsForKey(TEST_STR_1, testMap);
        Assert.assertNotNull(fareCappingLimitsDao.getFareCappingLimitsForKey(TEST_STR_1));
    }

    @Test
    public void testDeleteFareCappingLimitsForKey() {
        fareCappingLimitsDao.addFareCappingLimitsForKey(TEST_STR_1, testMap);
        fareCappingLimitsDao.deleteFareCappingLimitsForKey(TEST_STR_1);
        Assert.assertNull(fareCappingLimitsDao.getFareCappingLimitsForKey(TEST_STR_1));
    }

    @Test
    public void testDeleteFareCappingLimitsForKeyNotPresent() {
        fareCappingLimitsDao.addFareCappingLimitsForKey(TEST_STR_1, testMap);
        fareCappingLimitsDao.deleteFareCappingLimitsForKey(TEST_STR_2);
        Assert.assertNotNull(fareCappingLimitsDao.getFareCappingLimitsForKey(TEST_STR_1));
        Assert.assertNull(fareCappingLimitsDao.getFareCappingLimitsForKey(TEST_STR_2));
    }
}
