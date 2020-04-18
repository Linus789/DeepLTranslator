package de.linus.deepltranslator;

import com.machinepublishers.jbrowserdriver.JBrowserDriver;

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
    public String translate(String text, Language from, Language to) throws Exception {
        isValid(text, from, to);

        return translate(text, from, to, -1);
    }

    /**
     * @see DeepLTranslator#translate(String, Language, Language)
     */
    private String translate(String text, Language from, Language to, int repeated) throws Exception {
        try {
            return getTranslation(text, from, to);
        } catch (DeepLException e) {
            if(repeat(++repeated)) {
                Thread.sleep(getConfiguration().getRepetitionsDelay().apply(repeated).toMillis());
                return translate(text, from, to, repeated);
            }

            throw e;
        }
    }

    /**
     * @see DeepLTranslator#translate(String, Language, Language)
     */
    public void translateAsync(String text, Language from, Language to, TranslationConsumer translationConsumer) throws IllegalStateException {
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
        GLOBAL_INSTANCES.forEach(JBrowserDriver::quit);
        EXECUTOR.shutdownNow();
    }

}
