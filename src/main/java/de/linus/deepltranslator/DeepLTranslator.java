package de.linus.deepltranslator;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class DeepLTranslator extends DeepLTranslatorBase {

    /**
     * With default settings.
     */
    public DeepLTranslator() {
        super();
    }

    /**
     * With custom settings.
     */
    public DeepLTranslator(DeepLConfiguration configuration) {
        super(configuration);
    }

    /**
     * Translates a text, which is limited to 5000 characters,
     * from a {@code SourceLanguage} to a {@code TargetLanguage}.
     * <p></p>
     * This method returns the translation or throws an exception.
     * <p></p>
     * Possible reasons for exceptions:
     * <p>- text is null or empty
     * <p>- {@code SourceLanguage} or {@code TargetLanguage} is null
     * <p>- text length exceeds the limit of 5000 characters
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

        return CompletableFuture.supplyAsync(() -> translate(text, from, to), executor);
    }

    /**
     * Blocks until all async translations from this instance have completed execution, or the timeout occurs,
     * or the current thread is interrupted, whichever happens first.
     * <p></p>
     * After the termination, you can no longer use this instance for async translations.
     * For new async translations, you have to create another instance of DeepLTranslator.
     */
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        executor.shutdown();
        boolean result = executor.awaitTermination(timeout, unit);
        EXECUTOR_LIST.remove(executor);
        return result;
    }

    /**
     * Tries to quit all browsers and all active threads, which were started for asynchronous translating.
     * This method does not wait for the running tasks to finish.
     */
    public static void shutdown() {
        GLOBAL_INSTANCES.forEach(WebDriver::quit);
        EXECUTOR_LIST.forEach(ExecutorService::shutdownNow);
        CLEANUP_EXECUTOR.shutdownNow();
    }

}
