package com.nepu.metro.service;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.nepu.metro.dao.JourneyFareMappingDao;
import com.nepu.metro.vo.FareCalculatorContext;
import com.nepu.metro.vo.TigerCard;

public class JourneyFareMappingServiceTest {

    @Mock
    private JourneyFareMappingDao journeyFareMappingDao;

    @Mock
    TigerCard card;

    @Mock
    FareCalculatorContext context;

    private LocalDateTime dateTime;

    private JourneyFareMappingService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new JourneyFareMappingService(journeyFareMappingDao);
        dateTime = LocalDateTime.now();
    }

    @Test
    public void testSaveFareForJourney() {
        service.saveFareForJourney(context);
        Mockito.verify(journeyFareMappingDao).saveFareForJourney(context);
    }

    @Test
    public void testGetExistingFareForDay() {
        service.getExistingFareForDay(card, dateTime);
        Mockito.verify(journeyFareMappingDao).getExistingFareForDay(card, dateTime);
    }

    @Test
    public void testGetExistingFareForWeek() {
        service.getExistingFareForWeek(card, dateTime);
        Mockito.verify(journeyFareMappingDao).getExistingFareForWeek(card, dateTime);
    }
}