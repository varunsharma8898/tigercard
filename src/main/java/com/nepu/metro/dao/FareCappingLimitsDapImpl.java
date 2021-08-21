package com.nepu.metro.dao;

import java.util.HashMap;
import java.util.Map;

import com.nepu.metro.vo.CappingCategory;
import com.nepu.metro.vo.Fare;

public class FareCappingLimitsDapImpl implements FareCappingLimitsDao {

    private Map<String, Map<CappingCategory, Fare>> fareCappingByKeyMap;

    public FareCappingLimitsDapImpl() {
        fareCappingByKeyMap = new HashMap<>();
    }

    @Override
    public Map<CappingCategory, Fare> getFareCappingLimitsForKey(String key) {
        if (fareCappingByKeyMap.containsKey(key)) {
            return fareCappingByKeyMap.get(key);
        }
        return null;
    }

    @Override
    public void addFareCappingLimitsForKey(String key, Map<CappingCategory, Fare> map) {
        fareCappingByKeyMap.put(key, map);
    }

    @Override
    public void deleteFareCappingLimitsForKey(String key) {
        if (fareCappingByKeyMap.containsKey(key)) {
            fareCappingByKeyMap.remove(key);
        }
    }
}
