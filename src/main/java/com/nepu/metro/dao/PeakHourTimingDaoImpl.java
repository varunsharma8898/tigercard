package com.nepu.metro.dao;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nepu.metro.vo.PeakHour;

public class PeakHourTimingDaoImpl implements PeakHourTimingDao {

    private Map<DayOfWeek, List<PeakHour>> timingByDayOfWeekMap;

    public PeakHourTimingDaoImpl() {
        timingByDayOfWeekMap = new HashMap<>();
    }

    @Override
    public List<PeakHour> getPeakHourMapping(DayOfWeek dayOfWeek) {
        if (timingByDayOfWeekMap.isEmpty() || !timingByDayOfWeekMap.containsKey(dayOfWeek)) {
            return null;
        }

        return timingByDayOfWeekMap.get(dayOfWeek);
    }

    @Override
    public void addPeakHourForDayOfWeek(DayOfWeek dayOfWeek, List<PeakHour> peakHours) {
        timingByDayOfWeekMap.put(dayOfWeek, peakHours);
    }

    @Override
    public void deletePeakHourForDayOfWeek(DayOfWeek dayOfWeek, PeakHour peakHour) {
        if (timingByDayOfWeekMap.containsKey(dayOfWeek) && timingByDayOfWeekMap.get(dayOfWeek).contains(peakHour)) {
            timingByDayOfWeekMap.remove(peakHour);
        }
    }
}
