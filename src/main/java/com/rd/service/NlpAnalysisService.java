package com.rd.service;

import com.rd.config.RiskConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zemberek.morphology.TurkishMorphology;
import zemberek.core.turkish.SecondaryPos;
import zemberek.morphology.analysis.SingleAnalysis;
import zemberek.morphology.analysis.WordAnalysis;
import zemberek.tokenization.TurkishTokenizer;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NlpAnalysisService {
    private final TurkishMorphology morphology;
    private final TurkishTokenizer tokenizer;

    public NlpAnalysisService(TurkishMorphology morphology, TurkishTokenizer tokenizer) {
        this.morphology = morphology;
        this.tokenizer = tokenizer;
    }

    public String normalizeText(String text) {
        try {
            // Basic normalization without TurkishSentenceNormalizer
            String normalizedText = text.toLowerCase(new Locale("tr", "TR")).trim();
            
            // Additional custom normalizations
            for (Map.Entry<Character, Character> entry : RiskConfig.TR_CHAR_MAP.entrySet()) {
                normalizedText = normalizedText.replace(entry.getKey(), entry.getValue());
            }
            
            // Use TurkishTokenizer for proper word tokenization
            List<String> words = tokenizer.tokenize(normalizedText).stream()
                .map(token -> token.content)
                .collect(Collectors.toList());
            
            List<String> normalizedWords = new ArrayList<>();
            
            for (String word : words) {
                // Tire içeren kelimeleri koru
                if (word.contains("-")) {
                    normalizedWords.add(word);
                    continue;
                }
                
                // İnformal kelimeleri düzelt
                if (RiskConfig.INFORMAL_MAP.containsKey(word)) {
                    normalizedWords.add(RiskConfig.INFORMAL_MAP.get(word));
                    continue;
                }
                
                normalizedWords.add(word);
            }
            
            return String.join(" ", normalizedWords).trim().replaceAll("\\s+", " ");
        } catch (Exception e) {
            log.error("Metin normalizasyon hatası: {}", e.getMessage());
            return text;
        }
    }

    public String getNormalizedRoot(String word) {
        try {
            // Tire içeren kelimeleri kontrol et
            if (word.contains("-")) {
                String[] parts = word.split("-");
                return Arrays.stream(parts)
                    .map(this::getNormalizedRoot)
                    .collect(Collectors.joining("-"));
            }
            
            WordAnalysis analysis = morphology.analyze(word);
            if (analysis.analysisCount() == 0) {
                return word;
            }
            
            SingleAnalysis bestAnalysis = analysis.getAnalysisResults().get(0);
            
            // Kısaltma kontrolü
            if (bestAnalysis.getDictionaryItem().secondaryPos == SecondaryPos.Abbreviation) {
                return word;
            }
            
            return bestAnalysis.getStem();
            
        } catch (Exception e) {
            log.error("Morfolojik analiz hatası ({}): {}", word, e.getMessage());
            return word;
        }
    }

    public RiskScores analyzeRisk(String text) {
        try {
            // Metni normalize et
            String normalizedText = normalizeText(text);
            log.info("Normalize edilmiş metin: {}", normalizedText);
            
            // Kelimeleri tokenize et ve kökleri bul
            Set<String> rootWords = tokenizer.tokenize(normalizedText).stream()
                .map(token -> token.content)
                .filter(word -> !RiskConfig.STOP_WORDS.contains(word))
                .map(this::getNormalizedRoot)
                .collect(Collectors.toSet());
                
            log.info("Bulunan kök kelimeler: {}", rootWords);
            
            // Risk değerlerini hesapla
            double maxFrequency = RiskConfig.FREQUENCY_RARE;      // En düşük sıklık
            double maxProbability = RiskConfig.PROBABILITY_RARE;  // En düşük olasılık
            double maxSeverity = RiskConfig.SEVERITY_MINIMAL;     // En düşük şiddet
            
            // Tek kelimeleri kontrol et
            for (String root : rootWords) {
                if (RiskConfig.RISK_DATABASE.containsKey(root)) {
                    double[] scores = RiskConfig.RISK_DATABASE.get(root);
                    maxFrequency = Math.max(maxFrequency, scores[0]);
                    maxProbability = Math.max(maxProbability, scores[1]);
                    maxSeverity = Math.max(maxSeverity, scores[2]);
                }
            }
            
            // 2-4 kelimelik öbekleri kontrol et
            List<String> wordList = new ArrayList<>(rootWords);
            for (int n = 2; n <= Math.min(4, wordList.size()); n++) {
                for (int i = 0; i <= wordList.size() - n; i++) {
                    String phrase = String.join(" ", wordList.subList(i, i + n));
                    if (RiskConfig.RISK_DATABASE.containsKey(phrase)) {
                        double[] scores = RiskConfig.RISK_DATABASE.get(phrase);
                        maxFrequency = Math.max(maxFrequency, scores[0]);
                        maxProbability = Math.max(maxProbability, scores[1]);
                        maxSeverity = Math.max(maxSeverity, scores[2]);
                    }
                }
            }
            
            log.info("Hesaplanan risk değerleri - Sıklık: {}, Olasılık: {}, Şiddet: {}", 
                    maxFrequency, maxProbability, maxSeverity);
            
            return new RiskScores(maxFrequency, maxProbability, maxSeverity);
            
        } catch (Exception e) {
            log.error("Risk analizi hatası: {}", e.getMessage());
            // Varsayılan minimum değerler
            return new RiskScores(
                RiskConfig.FREQUENCY_RARE,
                RiskConfig.PROBABILITY_RARE,
                RiskConfig.SEVERITY_MINIMAL
            );
        }
    }

    public RiskScores analyzeCombinedRisk(String hazardText, String riskText) {
        try {
            // Her iki metni ayrı ayrı analiz et
            RiskScores hazardScores = analyzeRisk(hazardText);
            RiskScores riskScores = analyzeRisk(riskText);
            
            // Her bir risk parametresi için maksimum değeri al
            double maxFrequency = Math.max(hazardScores.frequency(), riskScores.frequency());
            double maxProbability = Math.max(hazardScores.probability(), riskScores.probability());
            double maxSeverity = Math.max(hazardScores.severity(), riskScores.severity());
            
            log.info("Birleşik risk değerleri - Sıklık: {}, Olasılık: {}, Şiddet: {}", 
                    maxFrequency, maxProbability, maxSeverity);
            
            return new RiskScores(maxFrequency, maxProbability, maxSeverity);
            
        } catch (Exception e) {
            log.error("Birleşik risk analizi hatası: {}", e.getMessage());
            // Varsayılan minimum değerler
            return new RiskScores(
                RiskConfig.FREQUENCY_RARE,
                RiskConfig.PROBABILITY_RARE,
                RiskConfig.SEVERITY_MINIMAL
            );
        }
    }
}
