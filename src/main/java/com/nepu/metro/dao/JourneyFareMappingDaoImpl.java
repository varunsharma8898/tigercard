package com.nepu.metro.dao;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.nepu.metro.util.Util;
import com.nepu.metro.vo.DailyFare;
import com.nepu.metro.vo.FareCalculatorContext;
import com.nepu.metro.vo.TigerCard;
import com.nepu.metro.vo.WeeklyFare;

public class JourneyFareMappingDaoImpl implements JourneyFareMappingDao {

    private Map<TigerCard, Map<String, WeeklyFare>> cache;

    public JourneyFareMappingDaoImpl() {
        cache = new HashMap<>();
    }

    @Override
    public void saveFareForJourney(FareCalculatorContext context) {
        String weekStr = Util.getWeeklyKeyFromDateTime(context.getJourney().getDatetime());
        if (!cache.containsKey(context.getTigerCard())) {
            Map<String, WeeklyFare> weeklyFareMap = new HashMap<>();
            cache.put(context.getTigerCard(), weeklyFareMap);
        }

        Map<String, WeeklyFare> weeklyFareMap = cache.get(context.getTigerCard());
        if (!weeklyFareMap.containsKey(weekStr)) {
            weeklyFareMap.put(weekStr, new WeeklyFare());
        }

        weeklyFareMap.get(weekStr).setApplicableWeeklyCap(context.getApplicableWeeklyLimit());
        weeklyFareMap.get(weekStr).setTotalWeeklyFare(context.getTotalWeeklyFareAmount());

        String dateStr = Util.getDailyKeyFromDateTime(context.getJourney().getDatetime());
        Map<String, DailyFare> dailyFareMap = weeklyFareMap.get(weekStr).getDailyFareMap();
        if (!dailyFareMap.containsKey(dateStr)) {
            dailyFareMap.put(dateStr, new DailyFare());
        }

        dailyFareMap.get(dateStr).setTotalFare(context.getTotalDailyFareAmount());
        dailyFareMap.get(dateStr).setApplicableDailyCap(context.getApplicableDailyLimit());
    }

    @Override
    public DailyFare getExistingFareForDay(TigerCard card, LocalDateTime dateTime) {
        String weekStr = Util.getWeeklyKeyFromDateTime(dateTime);
        String dateStr = Util.getDailyKeyFromDateTime(dateTime);
        if ((!cache.containsKey(card)) || !cache.get(card).containsKey(weekStr) || !cache.get(card).get(weekStr).getDailyFareMap().containsKey(dateStr)) {
            return null;
        }

        return cache.get(card).get(weekStr).getDailyFareMap().get(dateStr);
    }

    @Override
    public WeeklyFare getExistingFareForWeek(TigerCard card, LocalDateTime dateTime) {
        String weekStr = Util.getWeeklyKeyFromDateTime(dateTime);
        if ((!cache.containsKey(card)) || !cache.get(card).containsKey(weekStr)) {
            return null;
        }

        return cache.get(card).get(weekStr);
    }

}
