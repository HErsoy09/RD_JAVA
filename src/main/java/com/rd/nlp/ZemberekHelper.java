package com.rd.nlp;

import com.rd.config.RiskConfig;
import lombok.extern.slf4j.Slf4j;
import zemberek.morphology.TurkishMorphology;
import zemberek.tokenization.TurkishTokenizer;
import zemberek.morphology.analysis.SingleAnalysis;
import zemberek.morphology.analysis.WordAnalysis;
import java.util.*;

@Slf4j
public class ZemberekHelper {
    private final TurkishMorphology morphology;
    private final TurkishTokenizer tokenizer;

    public TurkishMorphology getMorphology() {
        return morphology;
    }

    public TurkishTokenizer getTokenizer() {
        return tokenizer;
    }

    public ZemberekHelper() {
        try {
            this.morphology = TurkishMorphology.createWithDefaults();
            this.tokenizer = TurkishTokenizer.builder()
                .ignoreTypes()
                .build();
            log.info("Zemberek başarıyla başlatıldı");
        } catch (Exception e) {
            log.error("Zemberek başlatma hatası", e);
            throw new RuntimeException("Zemberek başlatılamadı", e);
        }
    }

    private String[] tokenize(String text) {
        if (text == null || text.trim().isEmpty()) {
            return new String[0];
        }

        // Metni temizle ve normalize et
        text = text.trim()
                  .replaceAll("[^a-zA-ZğüşıöçĞÜŞİÖÇ\\s-]", " ")
                  .replaceAll("\\s+", " ")
                  .trim();

        if (text.isEmpty()) {
            return new String[0];
        }

        try {
            return tokenizer.tokenizeToStrings(text)
                          .stream()
                          .map(String::trim)
                          .filter(s -> !s.isEmpty())
                          .toArray(String[]::new);
        } catch (Exception e) {
            log.warn("Tokenization error for text: '{}', using simple split", text);
            return text.split("\\s+");
        }
    }

    public String normalizeText(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "";
        }

        try {
            // Önce basit normalizasyon
            text = text.toLowerCase(new Locale("tr", "TR"))
                      .replaceAll("[^a-zçğıöşü\\s]", "")
                      .trim()
                      .replaceAll("\\s+", " ");

            return text;
        } catch (Exception e) {
            log.error("Metin normalizasyon hatası: " + text, e);
            return text.trim();
        }
    }

    public Set<String> extractRootWords(String text) {
        Set<String> rootWords = new HashSet<>();
        if (text == null || text.trim().isEmpty()) {
            return rootWords;
        }

        try {
            String[] tokens = tokenize(text);
            for (String token : tokens) {
                WordAnalysis results = morphology.analyze(token);
                if (!results.getAnalysisResults().isEmpty()) {
                    // Her kelime için tüm olası kökleri ekle
                    for (SingleAnalysis analysis : results.getAnalysisResults()) {
                        rootWords.addAll(analysis.getLemmas());
                    }
                } else {
                    rootWords.add(token);
                }
            }
        } catch (Exception e) {
            log.error("Kök kelime çıkarma hatası: " + text, e);
        }

        return rootWords;
    }

    public List<String> findRiskPhrases(String text) {
        if (text == null || text.trim().isEmpty()) {
            return Collections.emptyList();
        }

        try {
            List<String> riskPhrases = new ArrayList<>();
            Set<String> rootWords = extractRootWords(text);
            String[] words = rootWords.toArray(new String[0]);

            // Tek kelimeli risk terimlerini kontrol et
            for (String word : words) {
                if (RiskConfig.RISK_DATABASE.containsKey(word)) {
                    riskPhrases.add(word);
                }
            }

            // N-gram analizi (2-4 kelimelik öbekler)
            for (int n = 2; n <= 4 && n <= words.length; n++) {
                for (int i = 0; i <= words.length - n; i++) {
                    String phrase = String.join(" ", Arrays.copyOfRange(words, i, i + n));
                    if (RiskConfig.RISK_DATABASE.containsKey(phrase)) {
                        riskPhrases.add(phrase);
                    }
                }
            }

            return riskPhrases;
        } catch (Exception e) {
            log.error("Error finding risk phrases in text: " + text, e);
            return Collections.emptyList();
        }
    }
}
