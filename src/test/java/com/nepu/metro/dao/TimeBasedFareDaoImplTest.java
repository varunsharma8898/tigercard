package com.nepu.metro.dao;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.nepu.metro.vo.Fare;
import com.nepu.metro.vo.FareCategory;

public class TimeBasedFareDaoImplTest {

    TimeBasedFareDao dao;

    Map<FareCategory, Fare> testMap;

    private static final String TEST_STR_1 = "test-string-1";

    private static final String TEST_STR_2 = "test-string-2";

    @Before
    public void setUp() {
        dao = new TimeBasedFareDaoImpl();
        testMap = new HashMap<>();
        testMap.put(FareCategory.PEAK_HOURS, new Fare(1));
        testMap.put(FareCategory.OFF_PEAK_HOURS, new Fare(2));
    }

    @Test
    public void testGetFareForKey() {
        dao.addFareForKey(TEST_STR_1, testMap);
        Assert.assertNotNull(dao.getFareForKey(TEST_STR_1));
    }

    @Test
    public void testGetFareForKey_NotPresent() {
        dao.addFareForKey(TEST_STR_1, testMap);
        Assert.assertNull(dao.getFareForKey(TEST_STR_2));
    }

    @Test
    public void testAddFareForKey() {
        dao.addFareForKey(TEST_STR_1, testMap);
        Assert.assertNotNull(dao.getFareForKey(TEST_STR_1));
    }

    @Test
    public void testDeleteFareForKey() {
        dao.addFareForKey(TEST_STR_1, testMap);
        dao.deleteFareForKey(TEST_STR_1);
        Assert.assertNull(dao.getFareForKey(TEST_STR_1));
    }

    @Test
    public void testDeleteFareForKey_NotPresent() {
        dao.addFareForKey(TEST_STR_1, testMap);
        dao.deleteFareForKey(TEST_STR_2);
        Assert.assertNotNull(dao.getFareForKey(TEST_STR_1));
        Assert.assertNull(dao.getFareForKey(TEST_STR_2));
    }
}