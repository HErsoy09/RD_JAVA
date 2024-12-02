package com.rd.service;

import com.rd.nlp.ZemberekHelper;
import com.rd.config.RiskConfig;
import com.rd.model.RiskAssessment;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class RiskCalculationService {
    private final ZemberekHelper zemberekHelper;
    private static final double[] DEFAULT_RISK = {1, 1, 1}; // frequency, probability, severity

    public RiskCalculationService(ZemberekHelper zemberekHelper) {
        this.zemberekHelper = zemberekHelper;
    }

    public RiskScores calculateRiskScores(String text) {
        if (text == null || text.trim().isEmpty()) {
            log.warn("Boş metin için risk hesaplaması yapılamadı");
            return new RiskScores(DEFAULT_RISK[0], DEFAULT_RISK[1], DEFAULT_RISK[2]);
        }
    
        log.info("Risk analizi başlatılıyor. Gelen metin: {}", text);

        // Metni normalize et ve kök kelimeleri çıkar
        String normalizedText = zemberekHelper.normalizeText(text);
        if (normalizedText.isEmpty()) {
            log.warn("Metin normalizasyonu başarısız oldu. Ham metin: {}", text);
            return new RiskScores(DEFAULT_RISK[0], DEFAULT_RISK[1], DEFAULT_RISK[2]);
        }

        // İnformal kelimeleri formal karşılıklarıyla değiştir
        String[] words = normalizedText.split("\\s+");
        List<String> normalizedWords = new ArrayList<>();
        
        for (String word : words) {
            // Önce risk informal map'ten kontrol et
            String normalizedWord = RiskConfig.RISK_INFORMAL_MAP.getOrDefault(word, word);
            normalizedWords.add(normalizedWord);
            
            // Eğer kelime bir risk terimi ise, direkt olarak ekle
            if (RiskConfig.RISK_DATABASE.containsKey(normalizedWord)) {
                normalizedWords.add(normalizedWord);
            }
        }
        
        normalizedText = String.join(" ", normalizedWords);
        
        Set<String> rootWords = zemberekHelper.extractRootWords(normalizedText);
        if (rootWords.isEmpty()) {
            log.warn("Kök kelime çıkarımı başarısız oldu. Normalize metin: {}", normalizedText);
            return new RiskScores(DEFAULT_RISK[0], DEFAULT_RISK[1], DEFAULT_RISK[2]);
        }
        
        log.info("Metin analizi tamamlandı - Normalize metin: {}", normalizedText);
        log.info("Bulunan kök kelimeler: {}", rootWords);
    
        // Risk değerleri için hesaplama
        double maxFrequency = DEFAULT_RISK[0];
        double maxProbability = DEFAULT_RISK[1];
        double maxSeverity = DEFAULT_RISK[2];
        
        // Bulunan risk terimlerini takip etmek için
        Map<String, double[]> foundRiskTerms = new LinkedHashMap<>();

        // Tek kelimeler için risk değerlerini kontrol et
        for (String root : rootWords) {
            if (RiskConfig.RISK_DATABASE.containsKey(root)) {
                double[] values = RiskConfig.RISK_DATABASE.get(root);
                updateMaxRiskValues(root, values, "Tekli kelime", foundRiskTerms);
                maxFrequency = Math.max(values[0], maxFrequency);
                maxProbability = Math.max(values[1], maxProbability);
                maxSeverity = Math.max(values[2], maxSeverity);
            }
        }
    
        // N-gram analizi (2-4 kelimelik öbekler)
        String[] wordsArray = rootWords.toArray(new String[0]);
        for (int n = 4; n >= 2; n--) {
            if (n > wordsArray.length) continue;
            
            for (int i = 0; i <= wordsArray.length - n; i++) {
                String[] phraseWords = Arrays.copyOfRange(wordsArray, i, i + n);
                String phrase = String.join(" ", phraseWords);
                
                if (RiskConfig.RISK_DATABASE.containsKey(phrase)) {
                    double[] values = RiskConfig.RISK_DATABASE.get(phrase);
                    updateMaxRiskValues(phrase, values, n + "-gram", foundRiskTerms);
                    maxFrequency = Math.max(values[0], maxFrequency);
                    maxProbability = Math.max(values[1], maxProbability);
                    maxSeverity = Math.max(values[2], maxSeverity);
                }
            }
        }

        // Risk özeti
        if (!foundRiskTerms.isEmpty()) {
            log.info("Bulunan risk terimleri ve değerleri:");
            foundRiskTerms.forEach((term, values) -> 
                log.info("  {} -> F:{}, P:{}, S:{}, ÖS:{}", 
                    term, values[0], values[1], values[2], 
                    (values[1] >= 4 ? 2 : 1)));
        } else {
            log.warn("Metinde hiç risk terimi bulunamadı");
            return new RiskScores(DEFAULT_RISK[0], DEFAULT_RISK[1], DEFAULT_RISK[2]);
        }

        RiskScores scores = new RiskScores(maxFrequency, maxProbability, maxSeverity);
        log.info("Hesaplanan risk değerleri - F:{}, P:{}, S:{}, ÖS:{}", 
                scores.frequency(), scores.probability(), scores.severity(),
                scores.getSpecialProbability());

        return scores;
    }

    private void updateMaxRiskValues(String term, double[] values, String type,
            Map<String, double[]> foundRiskTerms) {
        foundRiskTerms.put(term, values);
        log.debug("{} risk terimi bulundu: {} -> F:{}, P:{}, S:{}", 
            type, term, values[0], values[1], values[2]);
    }

    public RiskScores calculateRiskScores(double frequency, double probability, double severity) {
        return new RiskScores(frequency, probability, severity);
    }

    public void calculateAndSetRiskScores(RiskAssessment assessment) {
        
        // Mevcut risk skorlarını hesapla
        RiskScores currentScores = assessment.getCurrentRiskScores();
        if (currentScores != null) {
            double currentNormalRiskScore = currentScores.calculateNormalRiskScore();
            assessment.setCurrentRiskLevel(currentNormalRiskScore);
        }

        // Önlem sonrası risk skorlarını hesapla
        RiskScores afterScores = assessment.getAfterMeasuresRiskScores();
        if (afterScores != null) {
            double afterNormalRiskScore = afterScores.calculateNormalRiskScore();
            assessment.setAfterMeasuresRiskLevel(afterNormalRiskScore);
        }
    }
}
