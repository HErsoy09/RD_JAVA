# Risk Değerlendirme Uygulaması

Bu uygulama, iş sağlığı ve güvenliği risk değerlendirmelerini otomatik olarak analiz eden ve skorlayan bir Java uygulamasıdır. Doğal dil işleme (NLP) teknolojilerini kullanarak tehlike ve risk metinlerini analiz eder ve Fine Kinney metodolojisine göre risk skorları üretir.

## Özellikler

- Excel formatında risk değerlendirme verilerini okuma
- Tehlike ve risk metinlerinin otomatik analizi (Zemberek NLP)
- Fine Kinney metodolojisine göre risk skorlama
- Mevcut durum ve önlem sonrası risk değerlendirmesi
- Detaylı Excel raporlama
- Özelleştirilebilir risk veritabanı
- Türkçe dil desteği

## Kurulum

1. Java 17 veya üzeri sürümü yükleyin
2. Maven'i yükleyin
3. Projeyi klonlayın:
```bash
git clone [repository-url]
```
4. Bağımlılıkları yükleyin:
```bash
mvn clean install
```

## Kullanım

1. Girdi Excel dosyanızı hazırlayın (`ornekler/girdi.xlsx` formatında)
2. Uygulamayı çalıştırın:
```bash
java -jar target/risk-assessment-1.0-SNAPSHOT-jar-with-dependencies.jar
```
3. Sonuç dosyasını kontrol edin (`rd_Fine_Kinney.xlsx`)

## Excel Formatı

### Girdi Dosyası
- `NO`: Faaliyet numarası
- `BÖLÜM`: İlgili departman/bölüm
- `FAALİYET`: Risk değerlendirmesi yapılan faaliyet
- `TEHLİKE`: Tespit edilen tehlike
- `RİSK`: Tehlikeye bağlı risk
- `SONUÇ`: Olası sonuçlar
- `ETKİ ALANI`: Etkilenen kişi/gruplar
- `MEVCUT DURUM`: Mevcut önlemler
- `ALINACAK ÖNLEM`: Planlanan önlemler
- `SORUMLU`: Sorumlu kişi/birim
- `TERMİN`: Tamamlanma tarihi

### Çıktı Dosyası
Yukarıdaki alanlara ek olarak:
- `SIKLIK`: Hesaplanan sıklık değeri (F)
- `OLASILIK`: Hesaplanan olasılık değeri (P)
- `ŞİDDET`: Hesaplanan şiddet değeri (S)
- Risk kategorileri (Düşük, Kabul Edilebilir, Orta, Belirgin, Tolere Edilemez)
- Önlem sonrası değerler (ÖS)

## Risk Kategorileri

- **Düşük Risk**: < 20
- **Kabul Edilebilir Risk**: 20-70
- **Orta Risk**: 70-200
- **Belirgin Risk**: 200-400
- **Tolere Edilemez Risk**: > 400

## Geliştirme

### Bağımlılıklar
- Apache POI: Excel işlemleri
- Zemberek NLP: Türkçe doğal dil işleme
- Log4j2: Loglama
- Lombok: Kod sadeleştirme
- JUnit: Birim testleri

### Paket Yapısı
```
com.rd
├── config/          # Konfigürasyon sınıfları
├── model/           # Veri modelleri
├── nlp/            # NLP işlemleri
├── service/        # İş mantığı servisleri
└── util/           # Yardımcı sınıflar
```

## Lisans

Bu proje MIT lisansı altında lisanslanmıştır. Detaylar için `LICENSE` dosyasına bakın.
