package com.rd.service;

import com.rd.model.RiskAssessment;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ExcelService {
    // 5x5 L Tipi Matris Excel sütun indeksleri
    private static final int COL_NO = 0;
    private static final int COL_BOLUM = 1;
    private static final int COL_FAALIYET = 2;
    private static final int COL_TEHLIKE = 3;
    private static final int COL_RISK = 4;
    private static final int COL_SONUC = 5;
    private static final int COL_ETKI_ALANI = 6;
    //private static final int COL_OLASILIK = 7;
    //private static final int COL_SIDDET = 8;
    //private static final int COL_RDS = 9;
    //private static final int COL_ONCELIK_SIRASI = 10;
    private static final int COL_MEVCUT_DURUM = 11;
    private static final int COL_ALINACAK_ONLEM = 12;
    private static final int COL_SORUMLU = 13;
    private static final int COL_TERMIN = 14;
    //private static final int COL_OLASILIK_OS = 15;
    //private static final int COL_SIDDET_OS = 16;
    //private static final int COL_RDS_OS = 17;
    //private static final int COL_ONCELIK_SIRASI_OS = 18;
    //private static final int COL_SONUC_OS = 19;

/*    private static final int COL_NO = 0;
    private static final int COL_TARIH = 1;
    private static final int COL_SEBEP = 2;
    private static final int COL_FAALIYET = 3;
    private static final int COL_TEHLIKE = 4;
    private static final int COL_RISK = 5;
    private static final int COL_SONUC = 6;
    private static final int COL_ETKILENENLER = 7;
    private static final int COL_MEVCUT_ONLEM = 8;
    private static final int COL_SIKLIK = 9;
    private static final int COL_OLASILIK = 10;
    private static final int COL_SIDDET = 11;
    private static final int COL_DUSUK = 12;
    private static final int COL_KABUL_EDILEBILIR = 13;
    private static final int COL_ORTA = 14;
    private static final int COL_BELIRGIN = 15;
    private static final int COL_TOLERE_EDILEMEZ = 16;
    private static final int COL_ALINACAK_ONLEM = 17;
    private static final int COL_SORUMLU = 18;
    private static final int COL_TERMIN = 19;
    private static final int COL_SIKLIK_OS = 20;
    private static final int COL_OLASILIK_OS = 21;
    private static final int COL_SIDDET_OS = 22;
    private static final int COL_DUSUK_OS = 23;
    private static final int COL_KABUL_EDILEBILIR_OS = 24;
    private static final int COL_ORTA_OS = 25;
    private static final int COL_BELIRGIN_OS = 26;
    private static final int COL_TOLERE_EDILEMEZ_OS = 27;
    private static final int COL_SONUC_OS = 28;
*/  

    // Fine Kinney Matris Excel sütun indeksleri
    private static final int COL_NEW_NO = 0;
    private static final int COL_NEW_RISK_BELIRLEME_TARIHI = 1;
    private static final int COL_NEW_RISK_DEG_SEBEBI = 2;
    private static final int COL_NEW_FAALIYET_EKIPMAN_MALZEME_ADI = 3;
    private static final int COL_NEW_TEHLIKE = 4;
    private static final int COL_NEW_RISK = 5;
    private static final int COL_NEW_SONUC = 6;
    private static final int COL_NEW_ETKILENENLER = 7;
    private static final int COL_NEW_MEVCUT_ONLEM = 8;
    private static final int COL_NEW_SIKLIK = 9;
    private static final int COL_NEW_OLASILIK = 10;
    private static final int COL_NEW_SIDDET = 11;
    private static final int COL_NEW_DUSUK = 12;
    private static final int COL_NEW_KABUL_EDILEBILIR = 13;
    private static final int COL_NEW_ORTA = 14;
    private static final int COL_NEW_BELIRGIN = 15;
    private static final int COL_NEW_TOLERE_EDILEMEZ = 16;
    private static final int COL_NEW_ALINACAK_ONLEM = 17;
    private static final int COL_NEW_SORUMLU = 18;
    private static final int COL_NEW_TAMAMLANMA_TARIHI = 19;
    private static final int COL_NEW_SIKLIK_OS = 20;
    private static final int COL_NEW_OLASILIK_OS = 21;
    private static final int COL_NEW_SIDDET_OS = 22;
    private static final int COL_NEW_DUSUK_OS = 23;
    private static final int COL_NEW_KABUL_EDILEBILIR_OS = 24;
    private static final int COL_NEW_ORTA_OS = 25;
    private static final int COL_NEW_BELIRGIN_OS = 26;
    private static final int COL_NEW_TOLERE_EDILEMEZ_OS = 27;
    private static final int COL_NEW_SONUC_OS = 28;


    private static final String[] NEW_HEADERS = {
        "Faaliyet No", "Risk Belirleme Tarihi", "Risk Değ. Sebebi", "Faaliyet, Ekipman, Malzeme Adı", 
        "Tehlike", "Risk", "Sonuç", "Etkilenenler", "Mevcut Önlem", "Sıklık", "Olasılık", "Şiddet", 
        "Düşük", "Kabul Edilebilir", "Orta", "Belirgin", "Tolere Edilemez", "Alınacak Önlem", 
        "Sorumlu", "Tamamlanma Tarihi", "Sıklık (ÖS)", "Olasılık (ÖS)", "Şiddet (ÖS)", "Düşük (ÖS)", 
        "Kabul Edilebilir (ÖS)", "Orta (ÖS)", "Belirgin (ÖS)", "Tolere Edilemez (ÖS)", "Sonuç (ÖS)"
    };

    private final RiskCalculationService riskCalculationService;
    private final NlpAnalysisService nlpAnalysisService;
    private final RiskMeasuresService riskMeasuresService;

    public ExcelService(RiskCalculationService riskCalculationService, 
                       NlpAnalysisService nlpAnalysisService,
                       RiskMeasuresService riskMeasuresService) {
        this.riskCalculationService = riskCalculationService;
        this.nlpAnalysisService = nlpAnalysisService;
        this.riskMeasuresService = riskMeasuresService;
    }

    public List<RiskAssessment> readExcel(String inputFile) throws IOException {
        List<RiskAssessment> allAssessments = new ArrayList<>();
        
        try (FileInputStream fis = new FileInputStream(inputFile);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            // Tüm sekmeleri işle
            for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
                Sheet sheet = workbook.getSheetAt(sheetIndex);
                String sheetName = sheet.getSheetName();
                log.info("Processing sheet: {}", sheetName);
                
                Row headerRow = sheet.getRow(0);
                if (headerRow == null) {
                    log.warn("Sheet {} has no header row, skipping", sheetName);
                    continue;
                }
                
                // Başlık satırını kontrol et
                log.info("Excel headers for sheet {}:", sheetName);
                for (Cell cell : headerRow) {
                    String headerValue = getCellValueAsString(cell);
                    log.info("Column {}: {}", cell.getColumnIndex(), headerValue);
                }
                
                // Veri satırlarını işle
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue; // Başlık satırını atla
                    
                    try {
                        log.debug("Processing row {} in sheet {}", row.getRowNum(), sheetName);
                        RiskAssessment assessment = parseRow(row);
                        if (assessment != null) {
                            assessment.setSheetName(sheetName);
                            allAssessments.add(assessment);
                        }
                    } catch (Exception e) {
                        log.warn("Error processing row {} in sheet {}: {}", row.getRowNum(), sheetName, e.getMessage());
                    }
                }
            }
        }
        
        return allAssessments;
    }

    public void writeExcel(String outputFile, List<RiskAssessment> assessments) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            // Değerlendirmeleri sekmelere göre grupla
            Map<String, List<RiskAssessment>> assessmentsBySheet = assessments.stream()
                .collect(Collectors.groupingBy(RiskAssessment::getSheetName));
            
            // Her sekme için ayrı sayfa oluştur
            for (Map.Entry<String, List<RiskAssessment>> entry : assessmentsBySheet.entrySet()) {
                String sheetName = entry.getKey();
                List<RiskAssessment> sheetAssessments = entry.getValue();
                
                // Risk skorlarına göre sırala
                sheetAssessments.sort((a1, a2) -> 
                    Double.compare(a2.getCurrentRiskScores().calculateNormalRiskScore(), 
                                 a1.getCurrentRiskScores().calculateNormalRiskScore())
                );
                
                Sheet sheet = workbook.createSheet(sheetName);
                
                // Başlık stilini oluştur
                CellStyle headerStyle = createHeaderStyle(workbook);
                CellStyle dataStyle = createDataStyle(workbook);
                
                // Başlık satırını oluştur
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < NEW_HEADERS.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(NEW_HEADERS[i]);
                    cell.setCellStyle(headerStyle);
                }
                
                // Değerlendirmeleri yaz
                int rowNum = 1;
                for (RiskAssessment assessment : sheetAssessments) {
                    Row row = sheet.createRow(rowNum++);
                    fillAssessmentRow(row, assessment, sheet);
                    
                    // Her hücreye veri stili uygula
                    for (int i = 0; i < NEW_HEADERS.length; i++) {
                        Cell cell = row.getCell(i);
                        if (cell != null) {
                            cell.setCellStyle(dataStyle);
                        }
                    }
                }
                
                // Sütun genişliklerini ayarla
                for (int i = 0; i < NEW_HEADERS.length; i++) {
                    sheet.setColumnWidth(i, 4000); // Başlangıç genişliği
                    sheet.autoSizeColumn(i); // İçeriğe göre otomatik ayarla
                    // Minimum genişlik kontrolü
                    if (sheet.getColumnWidth(i) < 3000) {
                        sheet.setColumnWidth(i, 3000);
                    }
                }
                
                // Satır yüksekliklerini ayarla
                headerRow.setHeight((short) 800); // Başlık satırı yüksekliği
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row != null) {
                        row.setHeight((short) 600); // Veri satırları yüksekliği
                    }
                }
            }
            
            // Dosyaya kaydet
            try (FileOutputStream fileOut = new FileOutputStream(outputFile)) {
                workbook.write(fileOut);
            }
        }
    }

    public void processWorkbook(String inputPath, String outputPath) throws IOException {
        try (FileInputStream fis = new FileInputStream(inputPath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            // Her sekmeyi işle
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                if (sheet.getLastRowNum() > 0) { // Boş sekmeleri atla
                    log.info("Sheet processed: " + sheet.getSheetName());
                }
            }

            // Değişiklikleri kaydet
            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                workbook.write(fos);
                log.info("Workbook saved to: " + outputPath);
            }
        }
    }

    private RiskAssessment parseRow(Row row) {
        try {
            RiskAssessment assessment = new RiskAssessment();
            
            // Temel alanları doldur
            assessment.setNo(getCellValueAsString(row.getCell(COL_NO)));
            assessment.setBolum(getCellValueAsString(row.getCell(COL_BOLUM)));
            assessment.setFaaliyet(getCellValueAsString(row.getCell(COL_FAALIYET)));
            
            // Tehlike ve Risk metinlerini al
            String tehlikeText = getCellValueAsString(row.getCell(COL_TEHLIKE));
            String riskText = getCellValueAsString(row.getCell(COL_RISK));
            
            if (tehlikeText.isEmpty()) {
                log.warn("Tehlike metni boş, satır atlanıyor: {}", row.getRowNum());
                return null;
            }
            
            assessment.setTehlike(tehlikeText);
            assessment.setRisk(riskText);
            
            // NLP analizi ile risk skorlarını hesapla (Tehlike ve Risk metinlerini birlikte analiz et)
            RiskScores scores = nlpAnalysisService.analyzeCombinedRisk(tehlikeText, riskText);
                       
            // Risk skorlarını set et
            assessment.setCurrentRiskScores(scores);
            riskCalculationService.calculateAndSetRiskScores(assessment);
            
            // Diğer alanları doldur
            assessment.setSonuc(getCellValueAsString(row.getCell(COL_SONUC)));
            assessment.setEtkilenenler(getCellValueAsString(row.getCell(COL_ETKI_ALANI)));
            assessment.setMevcutOnlem(getCellValueAsString(row.getCell(COL_MEVCUT_DURUM)));
            assessment.setAlinacakOnlem(getCellValueAsString(row.getCell(COL_ALINACAK_ONLEM)));
            assessment.setSorumlu(getCellValueAsString(row.getCell(COL_SORUMLU)));
            assessment.setTamamlanmaTarihi(getCellValueAsString(row.getCell(COL_TERMIN)));
            
            // Önlem sonrası değerleri hesapla
            riskMeasuresService.calculateAndSetAfterMeasuresRiskScores(assessment, scores);

            // Önlem sonrası risk değerlendirmesi için mesaj oluştur
            RiskScores afterScores = assessment.getAfterMeasuresRiskScores();
            if (afterScores != null) {
                double afterRiskScore = afterScores.calculateNormalRiskScore();
                String afterMessage = assessment.getSonucOs(afterRiskScore);
                assessment.setSonucOs(afterMessage);
            }
            
            return assessment;
            
        } catch (Exception e) {
            log.error("Satır işleme hatası ({}): {}", row.getRowNum(), e.getMessage());
            return null;
        }
    }

    private double parseRiskValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private Cell setCellValue(Row row, int columnIndex, Object value, boolean applyFormatting) {
        Cell cell = row.createCell(columnIndex);
        if (value != null) {
            if (value instanceof String && applyFormatting) {
                String formattedText = formatCellText(value.toString(), columnIndex);
                cell.setCellValue(formattedText);
            } else if (value instanceof Number) {
                cell.setCellValue(((Number) value).doubleValue());
            } else if (value instanceof LocalDate) {
                cell.setCellValue(value.toString());
            } else {
                cell.setCellValue(value.toString());
            }
            return cell;
        }
        return null;
    }

    private String formatCellText(String text, int columnIndex) {
        if (text == null || text.trim().isEmpty()) {
            return text;
        }

        // İlk 3 sütun için tümü büyük harf
        if (columnIndex <= COL_NEW_FAALIYET_EKIPMAN_MALZEME_ADI) {
            return text.toUpperCase(new Locale("tr", "TR"));
        }

        // Sonraki 5 sütun ve Sorumlu, Tamamlanma Tarihi için her kelime büyük harfle başlasın
        if ((columnIndex >= COL_NEW_TEHLIKE && columnIndex <= COL_NEW_ETKILENENLER) ||
            columnIndex == COL_NEW_SORUMLU || columnIndex == COL_NEW_TAMAMLANMA_TARIHI) {
            return capitalizeEachWord(text);
        }

        // Mevcut Önlem, Alınacak Önlem ve Sonuç için cümle yapısı
        if (columnIndex == COL_NEW_MEVCUT_ONLEM || columnIndex == COL_NEW_ALINACAK_ONLEM ||
            columnIndex == COL_NEW_SONUC || columnIndex == COL_NEW_SONUC_OS) {
            return capitalizeSentence(text);
        }

        return text;
    }

    private String capitalizeEachWord(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        String[] words = text.toLowerCase(new Locale("tr", "TR")).split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                if (result.length() > 0) {
                    result.append(" ");
                }
                result.append(Character.toUpperCase(word.charAt(0)))
                      .append(word.substring(1));
            }
        }

        return result.toString();
    }

    private String capitalizeSentence(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        String lowerCase = text.toLowerCase(new Locale("tr", "TR"));
        return Character.toUpperCase(lowerCase.charAt(0)) + lowerCase.substring(1);
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        
        // Yazı tipi ayarları
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12); // Font boyutunu artır
        headerStyle.setFont(headerFont);
        
        // Hizalama
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setWrapText(true);
        
        // Kenarlıklar
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        
        // Arka plan rengi - Açık mavi
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        return headerStyle;
    }

    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle dataStyle = workbook.createCellStyle();
        
        // Yazı tipi ayarları
        Font dataFont = workbook.createFont();
        dataFont.setFontHeightInPoints((short) 11);
        dataStyle.setFont(dataFont);
        
        // Hizalama - hem yatay hem dikey ortalı
        dataStyle.setAlignment(HorizontalAlignment.CENTER);
        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        dataStyle.setWrapText(true);
        
        // Kenarlıklar
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);
        
        return dataStyle;
    }

    private CellStyle createDataStyleLeft(Workbook workbook) {
        // Uzun metinler için sola dayalı stil
        CellStyle dataStyleLeft = createDataStyle(workbook);
        dataStyleLeft.setAlignment(HorizontalAlignment.LEFT);
        return dataStyleLeft;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate().toString();
                }
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    private String getSheetCode(String sheetName) {
        // Sekme adından kod oluştur
        if (sheetName == null || sheetName.trim().isEmpty()) {
            return "KOD";
        }
        
        // Sekme adını temizle
        String cleanName = sheetName.trim()
            .replaceAll("'", "") // Tırnak işaretlerini kaldır
            .replaceAll("!.*$", "") // ! işaretinden sonrasını kaldır
            .toUpperCase(); // Büyük harfe çevir
            
        // İlk 3 karakteri al
        if (cleanName.length() > 3) {
            cleanName = cleanName.substring(0, 3);
        }
        
        return cleanName;
    }

    private void fillAssessmentRow(Row row, RiskAssessment assessment, Sheet sheet) {
        // Stil oluştur
        CellStyle dataStyle = createDataStyle(row.getSheet().getWorkbook());
        CellStyle dataStyleLeft = createDataStyleLeft(row.getSheet().getWorkbook());
        
        // Faaliyet No'yu sekme adı ile birlikte oluştur
        String rowNum = String.format("%s-%03d", getSheetCode(sheet.getSheetName()), row.getRowNum());
        Cell noCell = setCellValue(row, COL_NEW_NO, rowNum, true);
        noCell.setCellStyle(dataStyle);

        // Diğer alanları doldur - formatlanmış metin ile
        Cell[] cells = new Cell[] {
            setCellValue(row, COL_NEW_RISK_BELIRLEME_TARIHI, LocalDate.now().toString(), true),
            setCellValue(row, COL_NEW_RISK_DEG_SEBEBI, "Periyodik Değerlendirme", true),
            setCellValue(row, COL_NEW_FAALIYET_EKIPMAN_MALZEME_ADI, assessment.getBolum() + " - " + assessment.getFaaliyet(), true),
            setCellValue(row, COL_NEW_TEHLIKE, assessment.getTehlike(), true),
            setCellValue(row, COL_NEW_RISK, assessment.getRisk(), true),
            setCellValue(row, COL_NEW_SONUC, assessment.getSonuc(), true),
            setCellValue(row, COL_NEW_ETKILENENLER, assessment.getEtkilenenler(), true),
            setCellValue(row, COL_NEW_MEVCUT_ONLEM, assessment.getMevcutOnlem(), true),
            setCellValue(row, COL_NEW_ALINACAK_ONLEM, assessment.getAlinacakOnlem(), true),
            setCellValue(row, COL_NEW_SORUMLU, assessment.getSorumlu(), true),
            setCellValue(row, COL_NEW_TAMAMLANMA_TARIHI, assessment.getTamamlanmaTarihi(), true)
        };

        // Uzun metin içeren hücrelere sola dayalı stil uygula
        for (int i = 0; i < cells.length; i++) {
            if (cells[i] != null) {
                boolean isLongText = i == COL_NEW_TEHLIKE || i == COL_NEW_RISK || 
                                   i == COL_NEW_SONUC || i == COL_NEW_MEVCUT_ONLEM || 
                                   i == COL_NEW_ALINACAK_ONLEM;
                cells[i].setCellStyle(isLongText ? dataStyleLeft : dataStyle);
            }
        }
        
        // Risk skorları ve diğer sayısal değerler
        RiskScores currentScores = assessment.getCurrentRiskScores();
        Cell[] numericCells = new Cell[] {
            setCellValue(row, COL_NEW_SIKLIK, currentScores.frequency(), false),
            setCellValue(row, COL_NEW_OLASILIK, currentScores.probability(), false),
            setCellValue(row, COL_NEW_SIDDET, currentScores.severity(), false)
        };
        
        // Sayısal değerlere stil uygula
        for (Cell cell : numericCells) {
            if (cell != null) cell.setCellStyle(dataStyle);
        }

        // Risk kategorilerini doldur
        double currentRiskScore = currentScores.calculateNormalRiskScore();
        Cell[] riskCells = new Cell[] {
            setCellValue(row, COL_NEW_DUSUK, parseRiskValue(assessment.getCurrentLowRisk()) > 0 ? currentRiskScore : 0, false),
            setCellValue(row, COL_NEW_KABUL_EDILEBILIR, parseRiskValue(assessment.getCurrentAcceptableRisk()) > 0 ? currentRiskScore : 0, false),
            setCellValue(row, COL_NEW_ORTA, parseRiskValue(assessment.getCurrentMediumRisk()) > 0 ? currentRiskScore : 0, false),
            setCellValue(row, COL_NEW_BELIRGIN, parseRiskValue(assessment.getCurrentSignificantRisk()) > 0 ? currentRiskScore : 0, false),
            setCellValue(row, COL_NEW_TOLERE_EDILEMEZ, parseRiskValue(assessment.getCurrentUnacceptableRisk()) > 0 ? currentRiskScore : 0, false)
        };
        
        // Risk kategorilerine stil uygula
        for (Cell cell : riskCells) {
            if (cell != null) cell.setCellStyle(dataStyle);
        }
        
        // Önlem sonrası değerler
        RiskScores afterScores = assessment.getAfterMeasuresRiskScores();
        if (afterScores != null) {
            Cell[] afterCells = new Cell[] {
                setCellValue(row, COL_NEW_SIKLIK_OS, afterScores.frequency(), false),
                setCellValue(row, COL_NEW_OLASILIK_OS, afterScores.probability(), false),
                setCellValue(row, COL_NEW_SIDDET_OS, afterScores.severity(), false)
            };
            
            for (Cell cell : afterCells) {
                if (cell != null) cell.setCellStyle(dataStyle);
            }
            
            // Önlem sonrası risk kategorilerini doldur
            double afterRiskScore = afterScores.calculateNormalRiskScore();
            Cell[] afterRiskCells = new Cell[] {
                setCellValue(row, COL_NEW_DUSUK_OS, parseRiskValue(assessment.getAfterMeasuresLowRisk()) > 0 ? afterRiskScore : 0, false),
                setCellValue(row, COL_NEW_KABUL_EDILEBILIR_OS, parseRiskValue(assessment.getAfterMeasuresAcceptableRisk()) > 0 ? afterRiskScore : 0, false),
                setCellValue(row, COL_NEW_ORTA_OS, parseRiskValue(assessment.getAfterMeasuresMediumRisk()) > 0 ? afterRiskScore : 0, false),
                setCellValue(row, COL_NEW_BELIRGIN_OS, parseRiskValue(assessment.getAfterMeasuresSignificantRisk()) > 0 ? afterRiskScore : 0, false),
                setCellValue(row, COL_NEW_TOLERE_EDILEMEZ_OS, parseRiskValue(assessment.getAfterMeasuresUnacceptableRisk()) > 0 ? afterRiskScore : 0, false)
            };
            
            for (Cell cell : afterRiskCells) {
                if (cell != null) cell.setCellStyle(dataStyle);
            }
            
            Cell sonucCell = setCellValue(row, COL_NEW_SONUC_OS, assessment.getSonucOs(), true);
            if (sonucCell != null) sonucCell.setCellStyle(dataStyleLeft);
        }
    }
}