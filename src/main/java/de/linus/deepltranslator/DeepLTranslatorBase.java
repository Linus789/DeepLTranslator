package de.linus.deepltranslator;

import com.google.common.net.UrlEscapers;
import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.Settings;
import com.machinepublishers.jbrowserdriver.UserAgent;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * API for the DeepL Translator
 */
class DeepLTranslatorBase {

    /**
     * For asynchronous translating.
     *
     * @see DeepLTranslator#translateAsync(String, Language, Language, TranslationConsumer)
     */
    static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    /**
     * All browser instances created.
     */
    static final List<JBrowserDriver> GLOBAL_INSTANCES = new ArrayList<>();

    /**
     * Available browser instances for this configuration.
     */
    private final LinkedBlockingQueue<JBrowserDriver> INSTANCES = new LinkedBlockingQueue<>();

    /**
     * All settings
     */
    private final DeepLConfiguration configuration;

    /**
     * With default settings
     */
    DeepLTranslatorBase() {
        this.configuration = new DeepLConfiguration.Builder().build();
    }

    /**
     * With custom settings
     */
    DeepLTranslatorBase(DeepLConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Checks if all arguments are valid, if not, an exception is thrown.
     */
    void isValid(String text, Language from, Language to) throws IllegalStateException {
        if(text == null || text.trim().isEmpty()) {
            throw new IllegalStateException("Text is null or empty");
        } else if(from == null || to == null) {
            throw new IllegalStateException("Language is null");
        } else if(text.length() > 5000) {
            throw new IllegalStateException("Text length is limited to 5000 characters");
        }
    }

    /**
     * Generates a request with all settings like timeout etc.
     * and returns the translation if succeeded.
     */
    String getTranslation(String text, Language from, Language to) throws DeepLException {
        JBrowserDriver driver = INSTANCES.poll();

        if(driver == null) {
            driver = new JBrowserDriver(
                    Settings.builder()
                            .userAgent(UserAgent.CHROME)
                            .timezone(configuration.getTimezone())
                            .screen(new Dimension(1920, 1080))
                            .ajaxWait(100)
                            .ajaxResourceTimeout(configuration.getTimeout().toMillis())
                            .connectTimeout((int) configuration.getTimeout().toMillis())
                            .cache(true)
                            .build()
            );

            GLOBAL_INSTANCES.add(driver);
        }

        String url = "https://www.deepl.com/translator#"
                        + from.getLanguageCode()
                        + "/"
                        + to.getLanguageCode()
                        + "/"
                        +  UrlEscapers.urlFragmentEscaper().escape(text);

        String result;
        long end = System.currentTimeMillis() + configuration.getTimeout().toMillis();

        driver.get(url);

        do {
            result = readResult(driver);

            if(result != null && !result.isEmpty())
                break;

            if(System.currentTimeMillis() > end)
                throw new DeepLException("Timed out.");

            driver.pageWait();
        } while (true);

        int statusCode = driver.getStatusCode();

        INSTANCES.offer(driver);

        if(statusCode != 200)
            throw new DeepLException("Received status code " + statusCode + ".");

        return result;
    }

    /**
     * Returns the translation, or null if the translation is pending or something went wrong.
     */
    private String readResult(JBrowserDriver driver) {
        WebElement webElement = driver.findElementByClassName("lmt__translations_as_text__text_btn");

        if(webElement == null)
            return null;

        String text = webElement.getAttribute("textContent");

        if(text == null)
            return null;

        if(!configuration.isPostProcessingEnabled())
            return text;

        return text
                .trim()
                .replaceAll("(?:\r?\n)+", " ")
                .replaceAll("\\s{2,}", " ");
    }

    /**
     * Whether the request should be repeated or not.
     */
    boolean repeat(int repeated) {
        return configuration.getRepetitions() == -1 || configuration.getRepetitions() > repeated;
    }

    /**
     * The settings.
     */
    public DeepLConfiguration getConfiguration() {
        return configuration;
    }

}
