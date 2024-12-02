package com.rd.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;

public class SampleDataGenerator {
    private static final String[] HEADERS = {
        "Faaliyet No", "Risk Belirleme Tarihi", "Risk Değ. Sebebi", 
        "Faaliyet, Ekipman, Malzeme Adı", "Tehlike", "Risk", "Sonuç", 
        "Etkilenenler", "Mevcut Önlem", "Sıklık", "Olasılık", "Şiddet",
        "Düşük", "Kabul Edilebilir", "Orta", "Belirgin", "Tolere Edilemez",
        "Alınacak Önlem", "Sorumlu", "Tamamlanma Tarihi",
        "Sıklık (ÖS)", "Olasılık (ÖS)", "Şiddet (ÖS)",
        "Düşük (ÖS)", "Kabul Edilebilir (ÖS)", "Orta (ÖS)",
        "Belirgin (ÖS)", "Tolere Edilemez (ÖS)", "Sonuç (ÖS)"
    };

    private static final String[][] SAMPLE_DATA = {
        {"F001", LocalDate.now().toString(), "Periyodik Değerlendirme", 
         "Üretim Hattı - Kesme Makinesi", "Kesici Alet", "Kesik Riski", "Yaralanma",
         "Operatörler", "Koruyucu Eldiven", "3", "2", "4",
         "", "", "", "", "",
         "Yeni güvenlik ekipmanı alımı", "İş Güvenliği Uzmanı", LocalDate.now().plusMonths(1).toString(),
         "", "", "", "", "", "", "", "", ""},
         
        {"F002", LocalDate.now().toString(), "Periyodik Değerlendirme",
         "Depo - Forklift", "Çarpışma", "Kaza Riski", "Ciddi Yaralanma",
         "Depo Çalışanları", "Uyarı Işıkları", "2", "3", "5",
         "", "", "", "", "",
         "Forklift operatör eğitimi", "İK Müdürü", LocalDate.now().plusMonths(2).toString(),
         "", "", "", "", "", "", "", "", ""}
    };

    public static void generateSampleExcel(String filePath) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Risk Assessment");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            CellStyle headerStyle = createHeaderStyle(workbook);
            
            for (int i = 0; i < HEADERS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(HEADERS[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Add sample data
            for (int i = 0; i < SAMPLE_DATA.length; i++) {
                Row row = sheet.createRow(i + 1);
                for (int j = 0; j < SAMPLE_DATA[i].length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellValue(SAMPLE_DATA[i][j]);
                }
            }
            
            // Auto size columns
            for (int i = 0; i < HEADERS.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Write to file
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        }
    }

    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    public static void main(String[] args) {
        try {
            generateSampleExcel("rd_5x5.xlsx");
            System.out.println("Sample Excel file created successfully!");
        } catch (IOException e) {
            System.err.println("Error creating sample Excel file: " + e.getMessage());
        }
    }
}
