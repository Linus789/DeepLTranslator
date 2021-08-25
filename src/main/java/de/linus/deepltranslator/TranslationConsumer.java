package de.linus.deepltranslator;

/**
 * The TranslationConsumer is used to handle translations and exceptions.
 *
 * @see DeepLTranslator#translateAsync(String, SourceLanguage, TargetLanguage, TranslationConsumer)
 */
public class TranslationConsumer {

    /**
     * Passes the translation to the handler.
     */
    void passTranslation(String translation) {
        handleTranslation(translation);
        handleFinally();
    }

    /**
     * Passes the exception to the handler.
     */
    void passException(Exception e) {
        handleException(e);
        handleFinally();
    }

    /**
     * Handles the translation.
     *
     * @param translation the translation
     */
    public void handleTranslation(String translation) {}

    /**
     * Handles an {@code Exception}.
     *
     * Possible reasons for exceptions:
     * - text is null or empty
     * - {@code Language} is null
     * - text length exceeds the limit of 5000 characters
     * - https response went wrong
     *
     * @param e an exception
     */
    public void handleException(Exception e) {}

    /**
     * Is executed after {@link TranslationConsumer#handleTranslation(String)}
     * or {@link TranslationConsumer#handleException(Exception)}.
     */
    public void handleFinally() {}
    
}
