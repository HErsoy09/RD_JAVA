package com.rd.config;

import java.util.*;

public class RiskConfig {
    // Türkçe karakter dönüşüm tablosu
    public static final Map<Character, Character> TR_CHAR_MAP = new HashMap<>() {{
        put('ç', 'c'); put('ğ', 'g'); put('ı', 'i');
        put('ö', 'o'); put('ş', 's'); put('ü', 'u');
        put('Ç', 'C'); put('Ğ', 'G'); put('İ', 'I');
        put('Ö', 'O'); put('Ş', 'S'); put('Ü', 'U');
    }};

    // Risk ile ilgili informal kelimeler ve karşılıkları
    public static final Map<String, String> RISK_INFORMAL_MAP = new HashMap<>() {{
        // Yangın ile ilgili
        put("yanıyo", "yangın");
        put("yanıyor", "yangın");
        put("yanmış", "yangın");
        put("tutuştu", "yangın");
        put("tutuşuyo", "yangın");
        put("tutuşuyor", "yangın");
        put("yandı", "yangın");
        put("yanacak", "yangın");
        put("yanıcak", "yangın");
        put("alev", "yangın");
        put("alevli", "yangın");
        
        // Patlama ile ilgili
        put("patlıyo", "patlama");
        put("patlıyor", "patlama");
        put("patlıcak", "patlama");
        put("patlayacak", "patlama");
        put("patladı", "patlama");
        
        // Düşme ile ilgili
        put("düşüyo", "düşme");
        put("düşüyor", "düşme");
        put("düştü", "düşme");
        put("düşecek", "düşme");
        put("düşcek", "düşme");
        put("kırıldı", "kırık");
        put("kırılıyo", "kırık");
        put("bozuldu", "bozuk");
        put("bozuluyo", "bozuk");
        put("koptu", "kopuk");
        put("kopuyo", "kopuk");
        put("yıkıldı", "yıkık");
        put("yıkılıyo", "yıkık");
        put("çöktü", "çökmüş");
        put("çöküyo", "çökmüş");
    }};

    // Yaygın informal kullanımları düzelt
    public static final Map<String, String> INFORMAL_MAP = new HashMap<>() {{
        put("nolur", "ne olur");
        put("naber", "ne haber");
        put("naparsak", "ne yaparsak");
        put("naparsam", "ne yaparsam");
        put("dimi", "değil mi");
        put("bişi", "bir şey");
        put("naptın", "ne yaptın");
        put("bikere", "bir kere");
        put("bune", "bu ne");
        put("hiçbi", "hiçbir");
        put("bitek", "bir tek");
        put("tşk", "teşekkürler");
        put("eyw", "eyvallah");
        put("inş", "inşallah");
    }};

    // Stop words listesi
    public static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
        "ve", "veya", "ama", "fakat", "için", "de", "ile", "çünkü", "ancak", "şayet",
        "halbuki", "bile", "öyle", "gibi", "ya", "ne", "her", "hiç", "bir", "diğer",
        "çok", "az", "peki", "sadece", "belki", "yani", "zira", "madem", "ki", "ise",
        "üzere", "dahi", "mi", "mı", "mu", "mü", "nasıl", "neden", "niye", "kadar",
        "rağmen", "göre", "dolayı", "diye", "sanki", "oysa", "hatta", "üstelik",
        "ayrıca", "dahil", "itibaren", "tamam", "yalnız", "gerçi", "eğer", "acaba",
        "keşke", "ama", "lakin", "yoksa", "oysaki", "öyleyse", "bunlar", "şunlar",
        "onlar", "biz", "siz", "bu", "şu", "o", "kim", "kimi", "kimin", "ne",
        "nerede", "nereye", "nereden", "hangi", "hangisi", "kaç", "kaçı", "kaçıncı",
        "vb", "vs",
        
            // Risk analizi için özel stop words
            "olabilir", "olabilecek", "oluşabilir", "oluşabilecek", "var", "yok",
            "mevcut", "bulunur", "bulunabilir", "sahip", "içerir", "içeren",
            "nedeniyle", "sebebiyle", "sonucunda", "durumunda", "halinde",
            "sırasında", "esnasında", "boyunca", "süresince", "zarfında"
    ));

    // Risk veritabanı (sıklık, olasılık, şiddet)
    public static final Map<String, double[]> RISK_DATABASE = new HashMap<>() {{
        // İnşaat ve Genel Saha Riskleri
        put("yükseklik", new double[]{6, 6, 40});
        put("düşme", new double[]{6, 6, 40});
        put("iskele", new double[]{4, 4, 30});
        put("çukur", new double[]{3, 3, 15});
        put("kazı", new double[]{4, 4, 20});
        put("göçük", new double[]{1, 3, 40});
        put("yıkım", new double[]{3, 4, 35});
        put("moloz", new double[]{3, 3, 7});
        put("enkaz", new double[]{2, 3, 25});
        put("malzeme", new double[]{6, 6, 7});
        put("beton", new double[]{3, 3, 7});
        put("çimento", new double[]{3, 3, 5});
        put("hafriyat", new double[]{4, 4, 15});
        put("vinç devrilmesi", new double[]{1, 3, 100});
        put("toprak kayması", new double[]{2, 3, 40});
        put("yapı çökmesi", new double[]{1, 3, 100});
        put("aydınlatma", new double[]{5, 5, 10});
        put("kaygan", new double[]{5, 5, 15});
        put("çivi", new double[]{4, 4, 7});
        put("asbestos", new double[]{2, 4, 40});

        // Elektrik Riskleri
        put("elektrik", new double[]{3, 3, 15});
        put("çarpma", new double[]{3, 3, 15});
        put("kablo", new double[]{3, 3, 10});
        put("kısa devre", new double[]{2, 3, 15});
        put("yüksek gerilim", new double[]{2, 4, 40});
        put("ark", new double[]{2, 3, 20});
        put("statik elektrik", new double[]{3, 3, 10});
        put("topraklama hatası", new double[]{2, 3, 25});
        put("aşırı yük", new double[]{3, 3, 15});
        put("elektrik arkı", new double[]{2, 3, 40});
        put("yalıtım hatası", new double[]{3, 3, 25});
        put("aşırı ısınma", new double[]{3, 3, 20});
        put("elektromanyetik girişim", new double[]{4, 4, 10});
        put("yıldırım çarpması", new double[]{1, 2, 100});

        // Makine ve Ekipman Riskleri
        put("makine", new double[]{3, 3, 7});
        put("ekipman", new double[]{3, 3, 7});
        put("alet", new double[]{4, 4, 5});
        put("vinç", new double[]{3, 3, 25});
        put("forklift", new double[]{3, 3, 25});
        put("kesici", new double[]{5, 5, 7});
        put("delici", new double[]{5, 5, 7});
        put("testere", new double[]{5, 5, 15});
        put("matkap", new double[]{5, 5, 10});
        put("sıkışma", new double[]{4, 4, 15});
        put("ezilme", new double[]{3, 3, 25});
        put("hareketli parçalar", new double[]{4, 4, 20});
        put("hidrolik sistem", new double[]{3, 3, 15});
        put("pnömatik sistem", new double[]{3, 3, 15});
        put("taşlama", new double[]{5, 5, 10});
        put("mekanik arıza", new double[]{3, 3, 15});
        put("aşınmış parçalar", new double[]{4, 4, 10});
        put("güvenlik kilidi arızası", new double[]{2, 3, 30});
        put("aşırı yükleme", new double[]{3, 3, 25});
        put("yetersiz bakım", new double[]{4, 4, 20});
        put("uygunsuz kullanım", new double[]{4, 4, 15});

        // Yangın ve Patlama Riskleri
        put("yangın", new double[]{2, 3, 40});  // Şiddet değerini artırdık
        put("patlama", new double[]{1, 3, 40});
        put("yanıcı", new double[]{2, 3, 30});  // Şiddet değerini artırdık
        put("parlayıcı", new double[]{2, 3, 30});  // Şiddet değerini artırdık
        put("alev", new double[]{2, 3, 30});
        put("yanmak", new double[]{2, 3, 30});
        put("gaz", new double[]{3, 4, 15});
        put("duman", new double[]{4, 4, 10});
        put("kıvılcım", new double[]{3, 3, 15});
        put("yanıcı toz", new double[]{2, 3, 25});
        put("oksijen zenginleşmesi", new double[]{2, 3, 30});
        put("elektriksel yangın", new double[]{2, 3, 30});
        put("kimyasal reaksiyon", new double[]{2, 3, 35});
        put("sıcak yüzeyler", new double[]{4, 4, 15});
        put("yanıcı sıvı sızıntısı", new double[]{3, 3, 30});

        // Kimyasal Riskler
        put("kimyasal", new double[]{3, 3, 7});
        put("asit", new double[]{2, 3, 25});
        put("baz", new double[]{2, 3, 25});
        put("solvent", new double[]{3, 4, 15});
        put("boya", new double[]{3, 3, 7});
        put("yapıştırıcı", new double[]{3, 3, 7});
        put("zehirli", new double[]{3, 4, 20});
        put("korozif", new double[]{2, 3, 15});
        put("buhar", new double[]{4, 4, 10});
        put("aerosol", new double[]{3, 3, 10});
        put("karsinojen", new double[]{2, 4, 40});
        put("mutajen", new double[]{2, 4, 40});
        put("aşındırıcı", new double[]{3, 3, 15});
        put("reaktif", new double[]{3, 3, 20});
        put("akut zehirlenme", new double[]{2, 3, 30});
        put("kronik maruziyet", new double[]{4, 4, 20});
        put("cilt tahrişi", new double[]{5, 5, 10});
        put("göz tahrişi", new double[]{5, 5, 10});
        put("solunum yolu tahrişi", new double[]{5, 5, 15});
        put("kimyasal yanıklar", new double[]{3, 3, 25});

        // Fiziksel Riskler
        put("gürültü", new double[]{10, 6, 3});
        put("titreşim", new double[]{6, 6, 3});
        put("radyasyon", new double[]{2, 3, 20});
        put("sıcak", new double[]{5, 5, 5});
        put("soğuk", new double[]{5, 5, 5});
        put("basınç", new double[]{3, 3, 10});
        put("ultraviyole", new double[]{3, 3, 10});
        put("infrared", new double[]{3, 3, 10});
        put("lazer", new double[]{3, 3, 15});
        put("elektromanyetik alan", new double[]{3, 3, 10});
        put("yüksek basınç", new double[]{2, 3, 25});
        put("düşük basınç", new double[]{2, 3, 15});
        put("aşırı sıcaklık", new double[]{4, 4, 15});
        put("aşırı soğukluk", new double[]{4, 4, 15});
        put("yüksek nem", new double[]{5, 5, 10});
        put("düşük nem", new double[]{5, 5, 10});
        put("yetersiz havalandırma", new double[]{5, 5, 15});
        put("basınç değişimleri", new double[]{3, 3, 20});

        // Ergonomik Riskler
        put("ergonomik", new double[]{6, 6, 3});
        put("ağır", new double[]{6, 6, 7});
        put("yük", new double[]{6, 6, 7});
        put("kaldırma", new double[]{6, 6, 7});
        put("tekrarlayan", new double[]{6, 6, 3});
        put("duruş", new double[]{6, 6, 3});
        put("uzun süreli oturma", new double[]{6, 6, 5});
        put("uzun süreli ayakta durma", new double[]{6, 6, 5});
        put("uygunsuz çalışma yüzeyi", new double[]{5, 5, 5});
        put("uygunsuz aydınlatma", new double[]{5, 5, 3});
        put("birikimli travma bozuklukları", new double[]{5, 5, 15});
        put("kas gerginliği", new double[]{6, 6, 10});
        put("eklem zorlanması", new double[]{5, 5, 15});
        put("görsel yorgunluk", new double[]{6, 6, 7});
        put("yanlış duruş", new double[]{6, 6, 10});
        put("tekrarlayan hareketler", new double[]{6, 6, 15});

        // Diğer kategorileri de ekleyin...
    }};

    // Risk seviyeleri (S x O x Ş için yeni değerler)
    public static final double RISK_LOW = 20.0;           // Düşük Risk
    public static final double RISK_MEDIUM = 70.0;        // Gözetim altında tutulmalıdır
    public static final double RISK_HIGH = 200.0;         // Orta
    public static final double RISK_VERY_HIGH = 400.0;    // Belirgin
    // R>400 Kabul edilemez

    // Sıklık (S) değerleri
    public static final double FREQUENCY_CONTINUOUS = 10.0;    // Sürekli ya da saatte birden fazla
    public static final double FREQUENCY_DAILY = 6.0;         // Günde bir veya bir kaç defa
    public static final double FREQUENCY_WEEKLY = 3.0;        // Haftada bir veya bir kaç defa
    public static final double FREQUENCY_MONTHLY = 2.0;       // Ayda bir veya bir kaç defa
    public static final double FREQUENCY_YEARLY = 1.0;        // Yılda bir kaç defa
    public static final double FREQUENCY_RARE = 0.5;          // Yılda bir veya daha seyrek

    // Olasılık (O) değerleri
    public static final double PROBABILITY_CERTAIN = 10.0;    // Beklenir,Kesin
    public static final double PROBABILITY_HIGH = 6.0;        // Yüksek,oldukça mümkün
    public static final double PROBABILITY_POSSIBLE = 3.0;    // Olası
    public static final double PROBABILITY_LOW = 1.0;         // Mümkün fakat düşük ihtimal
    public static final double PROBABILITY_UNLIKELY = 0.5;    // Beklenmez ama mümkün
    public static final double PROBABILITY_RARE = 0.2;        // Beklenmez

    // Şiddet (Ş) değerleri
    public static final double SEVERITY_CATASTROPHIC = 100.0; // Birden fazla ölümlü kaza
    public static final double SEVERITY_MAJOR = 40.0;         // Öldürücü kaza
    public static final double SEVERITY_SERIOUS = 15.0;       // Kalıcı hasar/yaralanma, iş kaybı
    public static final double SEVERITY_SIGNIFICANT = 7.0;    // Önemli hasar/yaralanma, dış ilk yardım ihtiyacı
    public static final double SEVERITY_MINOR = 3.0;          // Küçük hasar/yaralanma, dahili ilk yardım
    public static final double SEVERITY_MINIMAL = 1.0;        // Ucuz atlatma

    public enum RiskLevel {
        DUSUK(1, "Düşük Risk"),
        KABUL_EDILEBILIR(2, "Kabul Edilebilir Risk"),
        ORTA(3, "Orta Risk"),
        BELIRGIN(4, "Belirgin Risk"),
        TOLERE_EDILEMEZ(5, "Tolere Edilemez Risk");

        private final int level;
        private final String description;

        RiskLevel(int level, String description) {
            this.level = level;
            this.description = description;
        }

        public int getLevel() {
            return level;
        }

        public String getDescription() {
            return description;
        }

        public static RiskLevel fromScore(double score) {
            if (score > RISK_VERY_HIGH) return TOLERE_EDILEMEZ;  // En yüksek risk
            else if (score > RISK_HIGH) return BELIRGIN;    // Yüksek risk
            else if (score > RISK_MEDIUM) return ORTA;         // Orta risk
            else if (score > RISK_LOW) return KABUL_EDILEBILIR;  // Düşük risk
            else return DUSUK;                        // En düşük risk
        }
    }

    /**
     * Metindeki Türkçe karakterleri normalize eder.
     * Örnek: 'İŞÇİ' -> 'ISCI'
     */
    public static String normalizeCharacters(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder result = new StringBuilder(text.length());
        for (char c : text.toCharArray()) {
            // Türkçe karakter dönüşümü
            if (TR_CHAR_MAP.containsKey(c)) {
                result.append(TR_CHAR_MAP.get(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}
