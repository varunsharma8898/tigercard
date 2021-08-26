package com.nepu.metro.service;

import java.time.LocalDateTime;

import com.nepu.metro.dao.JourneyFareMappingDao;
import com.nepu.metro.vo.DailyFare;
import com.nepu.metro.vo.FareCalculatorContext;
import com.nepu.metro.vo.TigerCard;
import com.nepu.metro.vo.WeeklyFare;

public class JourneyFareMappingService {

    private JourneyFareMappingDao journeyFareMappingDao;

    public JourneyFareMappingService(JourneyFareMappingDao dao) {
        this.journeyFareMappingDao = dao;
    }

    public void saveFareForJourney(FareCalculatorContext context) {
        journeyFareMappingDao.saveFareForJourney(context);
    }

    public DailyFare getExistingFareForDay(TigerCard card, LocalDateTime dateTime) {
        return journeyFareMappingDao.getExistingFareForDay(card, dateTime);
    }

    public WeeklyFare getExistingFareForWeek(TigerCard card, LocalDateTime dateTime) {
        return journeyFareMappingDao.getExistingFareForWeek(card, dateTime);
    }
}
