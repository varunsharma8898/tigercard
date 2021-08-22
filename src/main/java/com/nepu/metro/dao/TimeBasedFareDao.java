package com.nepu.metro.dao;

import java.util.Map;

import com.nepu.metro.vo.Fare;
import com.nepu.metro.vo.FareCategory;

public interface TimeBasedFareDao {

    Map<FareCategory, Fare> getFareForKey(String key);

    void addFareForKey(String key, Map<FareCategory, Fare> map);

    void deleteFareForKey(String key);
}
