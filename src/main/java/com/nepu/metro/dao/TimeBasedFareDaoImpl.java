package com.nepu.metro.dao;

import java.util.HashMap;
import java.util.Map;

import com.nepu.metro.vo.Fare;
import com.nepu.metro.vo.FareCategory;

public class TimeBasedFareDaoImpl implements TimeBasedFareDao {

    private Map<String, Map<FareCategory, Fare>> fareMap;

    public TimeBasedFareDaoImpl() {
        fareMap = new HashMap<>();
    }

    @Override
    public Map<FareCategory, Fare> getFareForKey(String key) {
        if (fareMap.containsKey(key)) {
            return fareMap.get(key);
        }
        return null;
    }

    @Override
    public void addFareForKey(String key, Map<FareCategory, Fare> map) {
        fareMap.put(key, map);
    }

    @Override
    public void deleteFareForKey(String key) {
        if (fareMap.containsKey(key)) {
            fareMap.remove(key);
        }
    }
}
