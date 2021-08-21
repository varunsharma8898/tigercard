package com.nepu.metro.dao;

import java.util.Map;

import com.nepu.metro.vo.CappingCategory;
import com.nepu.metro.vo.Fare;

public interface FareCappingLimitsDao {

    Map<CappingCategory, Fare> getFareCappingLimitsForKey(String key);

    void addFareCappingLimitsForKey(String key, Map<CappingCategory, Fare> map);

    void deleteFareCappingLimitsForKey(String key);
}
