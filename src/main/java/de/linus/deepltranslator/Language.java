package de.linus.deepltranslator;

import java.util.Optional;

/**
 * Current available languages of the DeepL Translator.
 */
public enum Language {

    AUTO_DETECT("auto"),
    BULGARIAN("bg"),
    CHINESE("zh"),
    CZECH("cs"),
    DANISH("da"),
    DUTCH("nl"),
    ENGLISH("en"),
    ESTONIAN("et"),
    FINNISH("fi"),
    FRENCH("fr"),
    GERMAN("de"),
    GREEK("el"),
    HUNGARIAN("hu"),
    ITALIAN("it"),
    JAPANESE("ja"),
    LATVIAN("lv"),
    LITHUANIAN("lt"),
    POLISH("pl"),
    PORTUGUESE("pt"),
    ROMANIAN("ro"),
    RUSSIAN("ru"),
    SLOVAK("sk"),
    SLOVENIAN("sl"),
    SPANISH("es"),
    SWEDISH("sv");

    private final String languageCode;

    Language(String languageCode) {
        this.languageCode = languageCode;
    }

    /**
     * Returns the language code (ISO 639‑1 scheme).
     *
     * @return language code (ISO 639‑1)
     */
    public String getLanguageCode() {
        return languageCode;
    }

    /**
     * Returns the language from a specific language code (ISO 639‑1 scheme).
     *
     * @param languageCode language code (ISO 639‑1)
     * @return the language
     */
    public static Optional<Language> getLanguage(String languageCode) {
        for(Language language : Language.values()) {
            if(language.getLanguageCode().equals(languageCode)) {
                return Optional.of(language);
            }
        }

        return Optional.empty();
    }

}
