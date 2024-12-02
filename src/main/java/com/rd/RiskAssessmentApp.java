package com.rd;

import com.rd.model.RiskAssessment;
import com.rd.nlp.ZemberekHelper;
import com.rd.service.RiskCalculationService;
import com.rd.service.ExcelService;
import com.rd.service.NlpAnalysisService;
import com.rd.service.RiskMeasuresService;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
public class RiskAssessmentApp {
    private static final String INPUT_FILE = "rd_5x5.xlsx";
    private static final String OUTPUT_FILE = "rd_Fine_Kinney.xlsx";

    public static void main(String[] args) {
        log.info("Starting Risk Assessment Application");
        
        try {
            validateInputFile();
            processRiskAssessment();
            log.info("Risk Assessment completed successfully");
            
        } catch (Exception e) {
            log.error("Application error: {}", e.getMessage(), e);
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
    
    private static void validateInputFile() throws Exception {
        Path inputPath = Paths.get(INPUT_FILE);
        if (!Files.exists(inputPath)) {
            throw new Exception("Input file not found: " + INPUT_FILE);
        }
        
        if (!Files.isReadable(inputPath)) {
            throw new Exception("Input file is not readable: " + INPUT_FILE);
        }
        
        if (Files.size(inputPath) == 0) {
            throw new Exception("Input file is empty: " + INPUT_FILE);
        }
    }
    
    private static void processRiskAssessment() throws Exception {
        // Initialize services
        ZemberekHelper zemberekHelper = initializeZemberek();
        RiskCalculationService riskCalculationService = new RiskCalculationService(zemberekHelper);
        NlpAnalysisService nlpAnalysisService = new NlpAnalysisService(zemberekHelper.getMorphology(),
                                                                      zemberekHelper.getTokenizer());
        RiskMeasuresService riskMeasuresService = new RiskMeasuresService(riskCalculationService);
        ExcelService excelService = new ExcelService(riskCalculationService, 
                                                   nlpAnalysisService,
                                                   riskMeasuresService);

        // Process Excel file
        log.info("Reading input file: {}", INPUT_FILE);
        List<RiskAssessment> assessments = excelService.readExcel(INPUT_FILE);
        log.info("Successfully read {} risk assessments", assessments.size());

        if (assessments.isEmpty()) {
            throw new Exception("No valid risk assessments found in input file");
        }

        // Write results
        log.info("Writing results to output file: {}", OUTPUT_FILE);
        excelService.writeExcel(OUTPUT_FILE, assessments);
        log.info("Results successfully written to: {}", OUTPUT_FILE);
    }
    
    private static ZemberekHelper initializeZemberek() {
        try {
            log.info("Initializing Zemberek NLP");
            return new ZemberekHelper();
        } catch (Exception e) {
            log.error("Failed to initialize Zemberek: {}", e.getMessage());
            throw new RuntimeException("Failed to initialize Zemberek", e);
        }
    }
}
