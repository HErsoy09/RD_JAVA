package com.rd.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.rd.service.RiskScores;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskAssessment {
    // Temel bilgiler
    private String no;
    private String bolum;
    private String faaliyet;
    private String tehlike;
    private String risk;
    private String sonuc;
    private String etkilenenler;
    private String mevcutOnlem;
    private String alinacakOnlem;
    private String sorumlu;
    private String tamamlanmaTarihi;
    private String sonucOs;
    private String sheetName;
    
    // Risk değerleri
    private RiskScores currentRiskScores;
    private RiskScores afterMeasuresRiskScores;
    
    // Mevcut risk değerleri
    private double siklik;
    private double olasilik;
    private double siddet;
    private String currentLowRisk;
    private String currentAcceptableRisk;
    private String currentMediumRisk;
    private String currentSignificantRisk;
    private String currentUnacceptableRisk;
    
    // Önlem sonrası risk değerleri
    private double siklikOS;
    private double olasilikOS;
    private double siddetOS;
    private String afterMeasuresLowRisk;
    private String afterMeasuresAcceptableRisk;
    private String afterMeasuresMediumRisk;
    private String afterMeasuresSignificantRisk;
    private String afterMeasuresUnacceptableRisk;
    private String riskSonucMesaji;  // Risk değerlendirme mesajı
    
    // Yardımcı metodlar
    public double[] getCurrentRiskValues() {
        if (currentRiskScores == null) {
            return new double[]{siklik, olasilik, siddet};
        }
        return currentRiskScores.toArray();
    }
    
    public double getCurrentSpecialProbability() {
        return currentRiskScores != null ? currentRiskScores.getSpecialProbability() : 1;
    }
    
    public double[] getAfterMeasuresRiskValues() {
        if (afterMeasuresRiskScores == null) {
            return new double[]{siklikOS, olasilikOS, siddetOS};
        }
        return afterMeasuresRiskScores.toArray();
    }
    
    public double getAfterMeasuresSpecialProbability() {
        return afterMeasuresRiskScores != null ? afterMeasuresRiskScores.getSpecialProbability() : 1;
    }
    
    public void setCurrentRiskLevel(double riskScore) {
        currentLowRisk = "";
        currentAcceptableRisk = "";
        currentMediumRisk = "";
        currentSignificantRisk = "";
        currentUnacceptableRisk = "";
        
        if (riskScore <= 20) {
            currentLowRisk = String.valueOf(riskScore);
        } else if (riskScore <= 70) {
            currentAcceptableRisk = String.valueOf(riskScore);
        } else if (riskScore <= 200) {
            currentMediumRisk = String.valueOf(riskScore);
        } else if (riskScore <= 400) {
            currentSignificantRisk = String.valueOf(riskScore);
        } else {
            currentUnacceptableRisk = String.valueOf(riskScore);
        }
    }
    
    public void setAfterMeasuresRiskLevel(double riskScore) {
        afterMeasuresLowRisk = "";
        afterMeasuresAcceptableRisk = "";
        afterMeasuresMediumRisk = "";
        afterMeasuresSignificantRisk = "";
        afterMeasuresUnacceptableRisk = "";
        
        if (riskScore <= 20) {
            afterMeasuresLowRisk = String.valueOf(riskScore);
        } else if (riskScore <= 70) {
            afterMeasuresAcceptableRisk = String.valueOf(riskScore);
        } else if (riskScore <= 200) {
            afterMeasuresMediumRisk = String.valueOf(riskScore);
        } else if (riskScore <= 400) {
            afterMeasuresSignificantRisk = String.valueOf(riskScore);
        } else {
            afterMeasuresUnacceptableRisk = String.valueOf(riskScore);
        }
    }

    
    public String getSonucOs(double riskScore) {
        if (riskScore > 400) {
            return "Hemen gerekli önlemler alınmalı veya tesis, bina, üretim veya çevrenin kapatılması gerekmektedir.";
        } else if (riskScore > 200) {
            return "Kısa dönemde iyileştirici tedbirler alınmalıdır.";
        } else if (riskScore > 70) {
            return "Uzun dönemde iyileştirilmelidir. Sürekli kontroller yapılmalıdır. Alınan önlemler gerektiğinde kontrol edilmelidir.";
        } else if (riskScore > 20) {
            return "Gözetim altında tutulmalıdır.";
        } else if (riskScore >= 0) {
            return "Gelecekte önemli bir tehlikeyi oluşturmaması için, incelenir ve gerekirse önlemler planlanan uygulamalar kısmında tarif edilir, uygulama kontrolleri yapılır ve personele ihtiyaç duyulan eğitimler verilir.";
        } else {
            return "Geçersiz risk değerlendirme skoru";
        }
    }
    
    public String getSonucOs() {
        return this.sonucOs;
    }
    
    public void setSonucOs(String sonucOs) {
        this.sonucOs = sonucOs;
    }
}