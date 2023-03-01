package de.linus.deepltranslator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum TargetLanguage {

    BULGARIAN("bg-BG", "translator-lang-option-bg"),
    CHINESE_SIMPLIFIED("zh-ZH", "translator-lang-option-zh"),
    CZECH("cs-CS", "translator-lang-option-cs"),
    DANISH("da-DA", "translator-lang-option-da"),
    DUTCH("nl-NL", "translator-lang-option-nl"),
    ENGLISH_AMERICAN("en-US", "translator-lang-option-en-US"),
    ENGLISH_BRITISH("en-GB", "translator-lang-option-en-GB"),
    ESTONIAN("et-ET", "translator-lang-option-et"),
    FINNISH("fi-FI", "translator-lang-option-fi"),
    FRENCH("fr-FR", "translator-lang-option-fr"),
    GERMAN("de-DE", "translator-lang-option-de"),
    GREEK("el-EL", "translator-lang-option-el"),
    HUNGARIAN("hu-HU", "translator-lang-option-hu"),
    ITALIAN("it-IT", "translator-lang-option-it"),
    JAPANESE("ja-JA", "translator-lang-option-ja"),
    LATVIAN("lv-LV", "translator-lang-option-lv"),
    LITHUANIAN("lt-LT", "translator-lang-option-lt"),
    POLISH("pl-PL", "translator-lang-option-pl"),
    PORTUGUESE("pt-PT", "translator-lang-option-pt-PT"),
    PORTUGUESE_BRAZILIAN("pt-BR", "translator-lang-option-pt-BR"),
    ROMANIAN("ro-RO", "translator-lang-option-ro"),
    RUSSIAN("ru-RU", "translator-lang-option-ru"),
    SLOVAK("sk-SK", "translator-lang-option-sk"),
    SLOVENIAN("sl-SL", "translator-lang-option-sl"),
    SPANISH("es-ES", "translator-lang-option-es"),
    SWEDISH("sv-SV", "translator-lang-option-sv");

    private static Map<String, TargetLanguage> codeToLanguage;
    private final String languageCode;
    private final String attributeValue;

    static {
        Map<String, TargetLanguage> codeToLanguage = new HashMap<>();

        for (TargetLanguage language : TargetLanguage.values()) {
            codeToLanguage.put(language.getLanguageCode(), language);
        }

        TargetLanguage.codeToLanguage = codeToLanguage;
    }

    TargetLanguage(String languageCode, String attributeValue) {
        this.languageCode = languageCode;
        this.attributeValue = attributeValue;
    }

    /**
     * Returns the language code (ISO 639‑1 language code, hyphen, ISO-3166 country code – e.g. en-US).
     *
     * @return language code (ISO 639‑1 language code, hyphen, ISO-3166 country code – e.g. en-US)
     */
    public String getLanguageCode() {
        return languageCode;
    }

    /**
     * Returns the value of the attribute to identify the correct button.
     *
     * @return dl-test attribute value
     */
    String getAttributeValue() {
        return attributeValue;
    }

    /**
     * Returns the language from a specific language code (ISO 639‑1 language code, hyphen, ISO-3166 country code – e.g. en-US).
     *
     * @param languageCode language code (ISO 639‑1 language code, hyphen, ISO-3166 country code – e.g. en-US)
     * @return the language
     */
    public static Optional<TargetLanguage> getLanguage(String languageCode) {
        return Optional.ofNullable(codeToLanguage.get(languageCode));
    }
}
