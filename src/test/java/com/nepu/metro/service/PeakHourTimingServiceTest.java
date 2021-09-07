package com.nepu.metro.service;

import java.time.DayOfWeek;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.nepu.metro.dao.PeakHourTimingDao;

public class PeakHourTimingServiceTest {

    @Mock
    private PeakHourTimingDao peakHourTimingDao;

    private PeakHourTimingService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new PeakHourTimingService(peakHourTimingDao);
    }

    @Test
    public void getPeakHourTimings() {
        service.getPeakHourTimings(DayOfWeek.MONDAY);
        Mockito.verify(peakHourTimingDao).getPeakHourMapping(DayOfWeek.MONDAY);
    }
}