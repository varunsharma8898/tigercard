package com.nepu.metro.dao;

import java.time.DayOfWeek;
import java.util.List;

import com.nepu.metro.vo.PeakHour;

public interface PeakHourTimingDao {

    List<PeakHour> getPeakHourMapping(DayOfWeek dayOfWeek);

    void addPeakHourForDayOfWeek(DayOfWeek dayOfWeek, List<PeakHour> peakHours);

    void deletePeakHourForDayOfWeek(DayOfWeek dayOfWeek, PeakHour peakHour);
}
