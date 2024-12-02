package com.rd.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import zemberek.morphology.TurkishMorphology;
import zemberek.normalization.TurkishSpellChecker;
import zemberek.normalization.TurkishSentenceNormalizer;
import zemberek.tokenization.TurkishSentenceExtractor;
import zemberek.tokenization.TurkishTokenizer;

import java.io.IOException;

@Configuration
public class NlpConfig {
    private static final String LM_UNIGRAM_PATH = "nlp/lm-unigram.slm";
    private static final String UNIGRAM_PATH = "nlp/turkishPatternTable";

    @Bean
    public TurkishMorphology turkishMorphology() {
        return TurkishMorphology.createWithDefaults();
    }

    @Bean
    public TurkishSpellChecker turkishSpellChecker(TurkishMorphology morphology) {
        try {
            return new TurkishSpellChecker(morphology);
        } catch (IOException e) {
            throw new RuntimeException("Türkçe yazım denetleyicisi başlatılırken hata oluştu: " + e.getMessage(), e);
        }
    }

    @Bean
    public TurkishSentenceNormalizer turkishSentenceNormalizer(TurkishMorphology morphology) {
        try {
            ClassPathResource lmResource = new ClassPathResource(LM_UNIGRAM_PATH);
            ClassPathResource unigramResource = new ClassPathResource(UNIGRAM_PATH);
            
            if (!lmResource.exists()) {
                throw new RuntimeException("Dil modeli dosyası bulunamadı: " + LM_UNIGRAM_PATH);
            }
            
            if (!unigramResource.exists()) {
                throw new RuntimeException("Unigram dosyası bulunamadı: " + UNIGRAM_PATH);
            }
            
            return new TurkishSentenceNormalizer(
                morphology, 
                lmResource.getFile().toPath(), 
                unigramResource.getFile().toPath()
            );
        } catch (IOException e) {
            throw new RuntimeException("Zemberek dosyaları yüklenirken hata oluştu: " + e.getMessage(), e);
        }
    }

    @Bean
    public TurkishSentenceExtractor turkishSentenceExtractor() {
        return TurkishSentenceExtractor.DEFAULT;
    }

    @Bean
    public TurkishTokenizer turkishTokenizer() {
        return TurkishTokenizer.DEFAULT;
    }
}
