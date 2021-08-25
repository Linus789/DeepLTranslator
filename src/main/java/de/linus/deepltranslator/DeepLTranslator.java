package de.linus.deepltranslator;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

public class DeepLTranslator extends DeepLTranslatorBase {

    /**
     * With default settings
     */
    public DeepLTranslator() {
        super();
    }

    /**
     * With custom settings
     */
    public DeepLTranslator(DeepLConfiguration configuration) {
        super(configuration);
    }

    /**
     * Translates a text, which is limited to 5000 characters,
     * from a source {@code Language} (from) to a target {@code Language} (to).
     *
     * This method returns the translation or throws an exception.
     *
     * Possible reasons for exceptions:
     * - text is null or empty
     * - {@code Language} is null
     * - text length exceeds the limit of 5000 characters
     *
     * @param text source text
     * @param from source language
     * @param to target language
     *
     * @return the translation
     * @throws Exception an exception
     */
    public String translate(String text, SourceLanguage from, TargetLanguage to) throws Exception {
        isValid(text, from, to);

        TimeoutException timeoutException = null;

        for (int i = 0; i <= getConfiguration().getRepetitions(); i++) {
            try {
                return getTranslation(text, from, to);
            } catch (TimeoutException e) {
                Thread.sleep(getConfiguration().getRepetitionsDelay().apply(i).toMillis());
                timeoutException = e;
            }
        }

        if (timeoutException != null)
            throw timeoutException;

        return null;
    }

    /**
     * @see DeepLTranslator#translate(String, SourceLanguage, TargetLanguage)
     */
    public void translateAsync(String text, SourceLanguage from, TargetLanguage to, TranslationConsumer translationConsumer) throws IllegalStateException {
        isValid(text, from, to);

        EXECUTOR.submit(() -> {
            try {
                translationConsumer.passTranslation(translate(text, from, to));
            } catch (Exception e) {
                translationConsumer.passException(e);
            }
        });
    }

    /**
     * Tries to quit all browsers and cancel all active threads, which were started for asynchronous translating.
     */
    public static void shutdown() {
        GLOBAL_INSTANCES.forEach(WebDriver::quit);
        EXECUTOR.shutdownNow();
    }

}
