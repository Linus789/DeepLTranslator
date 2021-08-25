package de.linus.deepltranslator;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.CompletableFuture;

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
    public String translate(String text, SourceLanguage from, TargetLanguage to) throws IllegalStateException, TimeoutException {
        isValid(text, from, to);

        TimeoutException timeoutException = null;

        for (int i = 0; i <= getConfiguration().getRepetitions(); i++) {
            try {
                return getTranslation(text, from, to);
            } catch (TimeoutException e) {
                try {
                    Thread.sleep(getConfiguration().getRepetitionsDelay().apply(i).toMillis());
                } catch (InterruptedException ignore) {}

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
    public CompletableFuture<String> translateAsync(String text, SourceLanguage from, TargetLanguage to) throws IllegalStateException {
        isValid(text, from, to);

        return CompletableFuture.supplyAsync(() -> translate(text, from, to), EXECUTOR);
    }

    /**
     * Tries to quit all browsers and all active threads, which were started for asynchronous translating.
     */
    public static void shutdown() {
        GLOBAL_INSTANCES.forEach(WebDriver::quit);
        EXECUTOR.shutdownNow();
    }

}
