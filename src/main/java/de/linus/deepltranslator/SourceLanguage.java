package de.linus.deepltranslator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum SourceLanguage {

    AUTO_DETECT("auto", "translator-lang-option-auto"),
    BULGARIAN("bg", "translator-lang-option-bg"),
    CHINESE("zh", "translator-lang-option-zh"),
    CZECH("cs", "translator-lang-option-cs"),
    DANISH("da", "translator-lang-option-da"),
    DUTCH("nl", "translator-lang-option-nl"),
    ENGLISH("en", "translator-lang-option-en"),
    ESTONIAN("et", "translator-lang-option-et"),
    FINNISH("fi", "translator-lang-option-fi"),
    FRENCH("fr", "translator-lang-option-fr"),
    GERMAN("de", "translator-lang-option-de"),
    GREEK("el", "translator-lang-option-el"),
    HUNGARIAN("hu", "translator-lang-option-hu"),
    INDONESIAN("id", "translator-lang-option-id"),
    ITALIAN("it", "translator-lang-option-it"),
    JAPANESE("ja", "translator-lang-option-ja"),
    KOREAN("ko", "translator-lang-option-ko"),
    LATVIAN("lv", "translator-lang-option-lv"),
    LITHUANIAN("lt", "translator-lang-option-lt"),
    NORWEGIAN("nb", "translator-lang-option-nb"),
    POLISH("pl", "translator-lang-option-pl"),
    PORTUGUESE("pt", "translator-lang-option-pt"),
    ROMANIAN("ro", "translator-lang-option-ro"),
    RUSSIAN("ru", "translator-lang-option-ru"),
    SLOVAK("sk", "translator-lang-option-sk"),
    SLOVENIAN("sl", "translator-lang-option-sl"),
    SPANISH("es", "translator-lang-option-es"),
    SWEDISH("sv", "translator-lang-option-sv"),
    TURKISH("tr", "translator-lang-option-tr"),
    UKRAINIAN("uk", "translator-lang-option-uk");

    private static Map<String, SourceLanguage> codeToLanguage;
    private final String languageCode;
    private final String attributeValue;

    static {
        Map<String, SourceLanguage> codeToLanguage = new HashMap<>();

        for (SourceLanguage language : SourceLanguage.values()) {
            codeToLanguage.put(language.getLanguageCode(), language);
        }

        SourceLanguage.codeToLanguage = codeToLanguage;
    }

    SourceLanguage(String languageCode, String attributeValue) {
        this.languageCode = languageCode;
        this.attributeValue = attributeValue;
    }

    /**
     * Returns the language code (IETF language tag, e.g. en-US or de).
     *
     * @return language code (IETF language tag, e.g. en-US or de)
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
     * Returns the language from a specific language code (IETF language tag, e.g. en-US or de).
     *
     * @param languageCode language code (IETF language tag, e.g. en-US or de)
     * @return the language
     */
    public static Optional<SourceLanguage> getLanguage(String languageCode) {
        return Optional.ofNullable(codeToLanguage.get(languageCode));
    }
}
