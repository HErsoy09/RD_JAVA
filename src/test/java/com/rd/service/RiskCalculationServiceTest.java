package com.rd.service;

import com.rd.nlp.ZemberekHelper;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RiskCalculationServiceTest {
    private RiskCalculationService riskCalculationService;
    private ZemberekHelper zemberekHelper;

    @Before
    public void setUp() {
        zemberekHelper = new ZemberekHelper();
        riskCalculationService = new RiskCalculationService(zemberekHelper);
    }

    @Test
    public void whenAnalyzingRiskText_thenCalculateRiskValues() {
        // given
        String text = "yangın çıktı ve patlama oldu";

        // when
        RiskScores scores = riskCalculationService.calculateRiskScores(text);

        // then
        assertNotNull("Risk değerleri null olmamalı", scores);
        assertTrue("Frekans 1'den büyük olmalı", scores.frequency() > 1);
        assertTrue("Olasılık 1'den büyük olmalı", scores.probability() > 1);
        assertTrue("Şiddet 1'den büyük olmalı", scores.severity() > 1);
        assertTrue("Özel olasılık 1 veya 2 olmalı", 
                  scores.getSpecialProbability() >= 1 && scores.getSpecialProbability() <= 2);
    }

    @Test
    public void whenCalculatingRiskScore_thenReturnCorrectValues() {
        // given
        double frequency = 3.0;
        double probability = 4.0;
        double severity = 5.0;

        // when
        RiskScores scores = riskCalculationService.calculateRiskScores(frequency, probability, severity);

        // then
        assertNotNull("Risk skorları null olmamalı", scores);
        assertEquals("Normal risk skoru yanlış", scores.calculateNormalRiskScore(), 60.0, 0.01);
        assertEquals("Özel risk skoru yanlış", scores.calculateSpecialRiskScore(), 30.0, 0.01);
        assertEquals("Özel olasılık yanlış", scores.getSpecialProbability(), 2, 0.0);
    }
}
