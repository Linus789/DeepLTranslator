package de.linus.deepltranslator;

import java.util.Optional;

/**
 * Current available languages of the DeepL Translator.
 */
public enum Language {

    AUTO_DETECT("auto"),
    GERMAN("DE"),
    ENGLISH("EN"),
    FRENCH("FR"),
    SPANISH("ES"),
    ITALIAN("IT"),
    DUTCH("NL"),
    POLISH("PL");

    private String languageCode;

    Language(String languageCode) {
        this.languageCode = languageCode;
    }

    /**
     * Returns the language code (ISO 639‑1 scheme).
     * The language code is used to build a request json.
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
