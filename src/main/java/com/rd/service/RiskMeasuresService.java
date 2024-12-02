package com.rd.service;

import com.rd.model.RiskAssessment;
import org.springframework.stereotype.Service;

@Service
public class RiskMeasuresService {
    
    private final RiskCalculationService riskCalculationService;
    
    public RiskMeasuresService(RiskCalculationService riskCalculationService) {
        this.riskCalculationService = riskCalculationService;
    }
    
    public void calculateAndSetAfterMeasuresRiskScores(RiskAssessment assessment, RiskScores scores) {
        // Önlem sonrası değerleri set et
        assessment.setSiklikOS(scores.frequency());
        assessment.setOlasilikOS(1.0); // Önlem sonrası olasılık 1 olarak alınır
        assessment.setSiddetOS(scores.severity());
        
        // RiskScores nesnesini oluştur ve set et
        RiskScores afterScores = new RiskScores(
            assessment.getSiklikOS(),
            assessment.getOlasilikOS(),
            assessment.getSiddetOS()
        );
        assessment.setAfterMeasuresRiskScores(afterScores);
        
        // Risk skorlarını hesapla
        riskCalculationService.calculateAndSetRiskScores(assessment);
    }
    
    public void setSiklikOS(RiskAssessment assessment, double siklikOS) {
        assessment.setSiklikOS(siklikOS);
        updateAfterMeasuresRiskScores(assessment);
    }
    
    public void setOlasilikOS(RiskAssessment assessment, double olasilikOS) {
        assessment.setOlasilikOS(olasilikOS);
        updateAfterMeasuresRiskScores(assessment);
    }
    
    public void setSiddetOS(RiskAssessment assessment, double siddetOS) {
        assessment.setSiddetOS(siddetOS);
        updateAfterMeasuresRiskScores(assessment);
    }
    
    private void updateAfterMeasuresRiskScores(RiskAssessment assessment) {
        RiskScores afterScores = new RiskScores(
            assessment.getSiklikOS(),
            assessment.getOlasilikOS(),
            assessment.getSiddetOS()
        );
        assessment.setAfterMeasuresRiskScores(afterScores);
        riskCalculationService.calculateAndSetRiskScores(assessment);
    }
}
