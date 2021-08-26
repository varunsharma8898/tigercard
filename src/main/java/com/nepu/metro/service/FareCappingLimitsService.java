package com.nepu.metro.service;

import java.util.Map;

import com.nepu.metro.dao.FareCappingLimitsDao;
import com.nepu.metro.util.Util;
import com.nepu.metro.vo.CappingCategory;
import com.nepu.metro.vo.DailyFare;
import com.nepu.metro.vo.Fare;
import com.nepu.metro.vo.Journey;
import com.nepu.metro.vo.WeeklyFare;

public class FareCappingLimitsService {

    private FareCappingLimitsDao fareCappingLimitsDao;

    public FareCappingLimitsService(FareCappingLimitsDao fareCappingLimitsDao) {
        this.fareCappingLimitsDao = fareCappingLimitsDao;
    }

    public Fare getDailyCappingLimit(Journey journey) {
        String key = Util.getKeyFromZones(journey.getFromZone(), journey.getToZone());
        Map<CappingCategory, Fare> limits = fareCappingLimitsDao.getFareCappingLimitsForKey(key);
        if (limits != null) {
            return limits.get(CappingCategory.DAILY);
        }
        return null;
    }

    public Fare getWeeklyCappingLimit(Journey journey) {
        String key = Util.getKeyFromZones(journey.getFromZone(), journey.getToZone());
        Map<CappingCategory, Fare> limits = fareCappingLimitsDao.getFareCappingLimitsForKey(key);
        if (limits != null) {
            return limits.get(CappingCategory.WEEKLY);
        }
        return null;
    }

    int calculateDailyCappingLimit(Journey journey, DailyFare prevDaily) {
        int dailyCap = 0;
        if (prevDaily != null) {
            dailyCap = prevDaily.getApplicableDailyCap();
        }
        dailyCap = Math.max(dailyCap, getDailyCappingLimit(journey).getAmount());
        return dailyCap;
    }

    int calculateWeeklyCappingLimit(Journey journey, WeeklyFare prevWeekly) {
        int weeklyCap = 0;
        if (prevWeekly != null) {
            weeklyCap = prevWeekly.getApplicableWeeklyCap();
        }
        weeklyCap = Math.max(weeklyCap, getWeeklyCappingLimit(journey).getAmount());
        return weeklyCap;
    }
}
