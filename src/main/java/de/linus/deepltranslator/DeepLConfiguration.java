package de.linus.deepltranslator;

import java.time.Duration;
import java.util.function.Function;

public class DeepLConfiguration {

    /**
     * If the http response didn't receive within the specified time,
     * the request cancels.
     *
     * Default duration is 10 seconds.
     *
     * @see DeepLTranslatorBase#getTranslation(String, SourceLanguage, TargetLanguage)
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
     * By default, post-processing is disabled.
     */
    private final boolean postProcessing;

    private DeepLConfiguration(Duration timeout, int repetitions, Function<Integer, Duration> repetitionsDelay, boolean postProcessing) {
        this.timeout = timeout;
        this.repetitions = repetitions;
        this.repetitionsDelay = repetitionsDelay;
        this.postProcessing = postProcessing;
    }

    /**
     * If the http response didn't receive within the specified time,
     * the request cancels.
     *
     * Default duration is 10 seconds.
     *
     * @see DeepLTranslatorBase#getTranslation(String, SourceLanguage, TargetLanguage)
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
     * By default, post-processing is disabled.
     */
    public boolean isPostProcessingEnabled() {
        return postProcessing;
    }

    public static class Builder {

        private Duration timeout;
        private int repetitions;
        private Function<Integer, Duration> repetitionsDelay;
        private boolean postProcessing;

        public Builder() {
            timeout = Duration.ofSeconds(10);
            repetitions = 3;
            repetitionsDelay = retryNumber -> Duration.ofMillis(3000L + 5000L * retryNumber);
            postProcessing = false;
        }

        /**
         * If the http response didn't receive within the specified time,
         * the request cancels.
         *
         * Default duration is 10 seconds.
         *
         * @see DeepLTranslatorBase#getTranslation(String, SourceLanguage, TargetLanguage)
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
         * By default, post-processing is disabled.
         */
        public Builder setPostProcessing(boolean postProcessing) {
            this.postProcessing = postProcessing;
            return this;
        }

        /**
         * Builds the configuration.
         */
        public DeepLConfiguration build() {
            return new DeepLConfiguration(timeout, repetitions, repetitionsDelay, postProcessing);
        }

    }

}
