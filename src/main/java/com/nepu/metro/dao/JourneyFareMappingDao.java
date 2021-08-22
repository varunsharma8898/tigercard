package com.nepu.metro.dao;

import java.time.LocalDateTime;

import com.nepu.metro.vo.DailyFare;
import com.nepu.metro.vo.FareCalculatorContext;
import com.nepu.metro.vo.TigerCard;
import com.nepu.metro.vo.WeeklyFare;

public interface JourneyFareMappingDao {

    void saveFareForJourney(FareCalculatorContext context);

    DailyFare getExistingFareForDay(TigerCard card, LocalDateTime dateTime);

    WeeklyFare getExistingFareForWeek(TigerCard card, LocalDateTime dateTime);
}
