package de.linus.deepltranslator;

import com.machinepublishers.jbrowserdriver.Timezone;

import java.time.Duration;
import java.util.function.Function;

public class DeepLConfiguration {

    /**
     * If the http response didn't receive within the specified time,
     * the request cancels.
     *
     * Default duration is 10 seconds.
     *
     * @see DeepLTranslatorBase#getTranslation(String, Language, Language)
     */
    private final Duration timeout;

    /**
     * Used if an error occurs.
     * -1 is used for repeating the request until it succeeds.
     *
     * Default value is 3.
     */
    private final int repetitions;

    /**
     * Can only be used if {@link DeepLConfiguration#repetitions} isn't zero.
     * This value represents the delay with which the request is repeated.
     *
     * Default interval is [3000 + 5000 * retryNumber] milliseconds.
     * Note: The first retry has the retryNumber 0.
     */
    private final Function<Integer, Duration> repetitionsDelay;

    /**
     * Whether the translation should be post-processed.
     * If post-processing is enabled, line breaks are removed and multiple consecutive spaces are replaced with a single space.
     *
     * By default, post-processing is enabled.
     */
    private final boolean postProcessing;

    /**
     * The timezone the browser instances are using.
     * Default timezone is Timezone.UTC.
     *
     * @see com.machinepublishers.jbrowserdriver.JBrowserDriver
     */
    private final Timezone timezone;

    private DeepLConfiguration(Duration timeout, int repetitions, Function<Integer, Duration> repetitionsDelay, boolean postProcessing, Timezone timezone) {
        this.timeout = timeout;
        this.repetitions = repetitions;
        this.repetitionsDelay = repetitionsDelay;
        this.postProcessing = postProcessing;
        this.timezone = timezone;
    }

    /**
     * If the http response didn't receive within the specified time,
     * the request cancels.
     *
     * Default duration is 10 seconds.
     *
     * @see DeepLTranslatorBase#getTranslation(String, Language, Language)
     */
    public Duration getTimeout() {
        return timeout;
    }

    /**
     * Used if an error occurs.
     * -1 is used for repeating the request until it succeeds.
     *
     * Default value is 3.
     */
    public int getRepetitions() {
        return repetitions;
    }

    /**
     * Can only be used if {@link DeepLConfiguration#repetitions} isn't zero.
     * This value represents the delay with which the request is repeated.
     *
     * Default interval is [3000 + 5000 * retryNumber] milliseconds.
     * Note: The first retry has the retryNumber 0.
     */
    public Function<Integer, Duration> getRepetitionsDelay() {
        return repetitionsDelay;
    }

    /**
     * Whether the translation should be post-processed.
     * If post-processing is enabled, line breaks are removed and multiple consecutive spaces are replaced with a single space.
     *
     * By default, post-processing is enabled.
     */
    public boolean isPostProcessingEnabled() {
        return postProcessing;
    }

    /**
     * The timezone the browser instances are using.
     * Default timezone is Timezone.UTC.
     *
     * @see com.machinepublishers.jbrowserdriver.JBrowserDriver
     */
    public Timezone getTimezone() {
        return timezone;
    }

    public static class Builder {

        private Duration timeout;
        private int repetitions;
        private Function<Integer, Duration> repetitionsDelay;
        private boolean postProcessing;
        private Timezone timezone;

        public Builder() {
            timeout = Duration.ofSeconds(10);
            repetitions = 3;
            repetitionsDelay = retryNumber -> Duration.ofMillis(3000L + 5000L * retryNumber);
            postProcessing = true;
            timezone = Timezone.UTC;
        }

        /**
         * If the http response didn't receive within the specified time,
         * the request cancels.
         *
         * Default duration is 10 seconds.
         *
         * @see DeepLTranslatorBase#getTranslation(String, Language, Language)
         */
        public Builder setTimeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        /**
         * Used if an error occurs.
         * -1 is used for repeating the request until it succeeds.
         *
         * Default value is 3.
         */
        public Builder setRepetitions(int repetitions) {
            this.repetitions = repetitions;
            return this;
        }

        /**
         * Can only be used if {@link DeepLConfiguration#repetitions} isn't zero.
         * This value represents the delay with which the request is repeated.
         *
         * Default interval is [3000 + 5000 * retryNumber] milliseconds.
         * Note: The first retry has the retryNumber 0.
         */
        public Builder setRepetitionsDelay(Function<Integer, Duration> repetitionsDelay) {
            this.repetitionsDelay = repetitionsDelay;
            return this;
        }

        /**
         * Whether the translation should be post-processed.
         * If post-processing is enabled, line breaks are removed and multiple consecutive spaces are replaced with a single space.
         *
         * By default, post-processing is enabled.
         */
        public Builder setPostProcessing(boolean postProcessing) {
            this.postProcessing = postProcessing;
            return this;
        }

        /**
         * The timezone the browser instances are using.
         * Default timezone is Timezone.UTC.
         *
         * @see com.machinepublishers.jbrowserdriver.JBrowserDriver
         */
        public Builder setTimezone(Timezone timezone) {
            this.timezone = timezone;
            return this;
        }

        /**
         * Builds the configuration.
         */
        public DeepLConfiguration build() {
            return new DeepLConfiguration(timeout, repetitions, repetitionsDelay, postProcessing, timezone);
        }

    }

}
