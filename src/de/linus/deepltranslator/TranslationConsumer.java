package de.linus.deepltranslator;

/**
 * The TranslationConsumer is used, to handle translations
 * and exceptions.
 *
 * @see DeepLTranslator#asyncTranslate(String, Language, Language, TranslationConsumer)
 */
public interface TranslationConsumer {

    /**
     * Handles a {@code Translation}.
     *
     * @param translation the translation
     */
    void handleTranslation(Translation translation);

    /**
     * Handles an {@code Exception}.
     *
     * Possible reasons for exceptions:
     * - text is null or empty
     * - {@code Language} is null
     * - text length exceeds the limit of 5000 characters
     * - https response went wrong (e.g. {@code HttpTimeoutException})
     *
     * @param e an exception
     */
    void handleException(Exception e);
    
}
