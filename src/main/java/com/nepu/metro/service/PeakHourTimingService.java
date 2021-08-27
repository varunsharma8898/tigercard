package com.nepu.metro.service;

import java.time.DayOfWeek;
import java.util.List;

import com.nepu.metro.dao.PeakHourTimingDao;
import com.nepu.metro.vo.PeakHour;

public class PeakHourTimingService {

    private PeakHourTimingDao peakHourTimingDao;

    public PeakHourTimingService(PeakHourTimingDao peakHourTimingDao) {
        this.peakHourTimingDao = peakHourTimingDao;
    }

    public List<PeakHour> getPeakHourTimings(DayOfWeek dayOfWeek) {
        return peakHourTimingDao.getPeakHourMapping(dayOfWeek);
    }

}
