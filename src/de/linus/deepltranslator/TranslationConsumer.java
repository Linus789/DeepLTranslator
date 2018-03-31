package de.linus.deepltranslator;

/**
 * The TranslationConsumer is used, to handle translations
 * and exceptions. But if an exception is thrown, the
 * translation is null. And it's the other way around, too.
 */
@FunctionalInterface
public interface TranslationConsumer {

    /**
     * Consumes a {@code Translation} <b>or</b> an {@code Exception}.
     *
     * Possible reasons for exceptions:
     * - text is null or empty
     * - {@code Language} is null
     * - text length exceeds the limit of 5000 characters
     * - https response went wrong (e.g. {@code HttpTimeoutException})
     *
     * @param translation the translation
     * @param e an exception
     *
     * @see DeepLTranslator#asyncTranslate(String, Language, Language, TranslationConsumer)
     */
    void response(Translation translation, Exception e);
}
