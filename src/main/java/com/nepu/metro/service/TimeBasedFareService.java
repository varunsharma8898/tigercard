package com.nepu.metro.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import com.nepu.metro.dao.TimeBasedFareDao;
import com.nepu.metro.util.Util;
import com.nepu.metro.vo.Fare;
import com.nepu.metro.vo.FareCategory;
import com.nepu.metro.vo.Journey;
import com.nepu.metro.vo.PeakHour;

public class TimeBasedFareService {

    private TimeBasedFareDao timeBasedFareDao;

    private PeakHourTimingService peakHourTimingService;

    public TimeBasedFareService(TimeBasedFareDao dao, PeakHourTimingService timingService) {
        this.timeBasedFareDao = dao;
        this.peakHourTimingService = timingService;
    }

    public Fare getTimeBasedFare(Journey journey) {
        if (isPeakHour(journey.getDatetime())) {
            return getPeakHoursFareForJourney(journey);
        }
        return getOffPeakHoursFareForJourney(journey);
    }

    private boolean isPeakHour(LocalDateTime journeyDateTime) {
        LocalTime journeyTime = LocalTime.of(journeyDateTime.getHour(), journeyDateTime.getMinute());
        List<PeakHour> peakHourTimings = peakHourTimingService.getPeakHourTimings(journeyDateTime.getDayOfWeek());
        // TODO: check for null in peakHourTimings, currently all days are present

        for (PeakHour peakHour : peakHourTimings) {
            if (journeyTime.isAfter(peakHour.getStartTime()) && journeyTime.isBefore(peakHour.getEndTime())) {
                return true;
            }
        }
        return false;
    }

    private Fare getPeakHoursFareForJourney(Journey journey) {
        String key = Util.getKeyFromZones(journey.getFromZone(), journey.getToZone());
        Map<FareCategory, Fare> fareMap = timeBasedFareDao.getFareForKey(key);
        Fare peakHoursFare = fareMap.get(FareCategory.PEAK_HOURS);
        return new Fare(peakHoursFare.getAmount());
    }

    private Fare getOffPeakHoursFareForJourney(Journey journey) {
        String key = Util.getKeyFromZones(journey.getFromZone(), journey.getToZone());
        Map<FareCategory, Fare> fareMap = timeBasedFareDao.getFareForKey(key);
        Fare offPeakHoursFare = fareMap.get(FareCategory.OFF_PEAK_HOURS);
        return new Fare(offPeakHoursFare.getAmount());
    }
}
