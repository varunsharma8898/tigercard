package com.nepu.metro.vo;

import java.util.HashMap;
import java.util.Map;

public class WeeklyFare {

    private Map<String, DailyFare> dailyFareMap;

    private int applicableWeeklyCap;

    private int totalWeeklyFare;

    public WeeklyFare() {
        dailyFareMap = new HashMap<>();
    }

    public Map<String, DailyFare> getDailyFareMap() {
        return dailyFareMap;
    }

    public void setDailyFareMap(Map<String, DailyFare> dailyFareMap) {
        this.dailyFareMap = dailyFareMap;
    }

    public int getApplicableWeeklyCap() {
        return applicableWeeklyCap;
    }

    public void setApplicableWeeklyCap(int applicableWeeklyCap) {
        this.applicableWeeklyCap = applicableWeeklyCap;
    }

    public int getTotalWeeklyFare() {
        return totalWeeklyFare;
    }

    public void setTotalWeeklyFare(int totalWeeklyFare) {
        this.totalWeeklyFare = totalWeeklyFare;
    }
}