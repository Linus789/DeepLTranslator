package de.linus.deepltranslator;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * API for the DeepL Translator
 */
public final class DeepLTranslator {

    /**
     * The base url, used to request a translation.
     * @see DeepLTranslator#sendRequest(JSONObject)
     */
    private static final String BASE_URL = "https://www.deepl.com/jsonrpc";

    /**
     * Used to generate threads for an asynchronous translation.
     *
     * Default executor is a {@link Executors#newCachedThreadPool()}.
     *
     * @see DeepLTranslator#asyncTranslate(String, Language, Language, TranslationConsumer)
     */
    private static ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    /**
     * If the http response is not received within the specified timeout
     * then a {@link jdk.incubator.http.HttpTimeoutException} is thrown.
     *
     * Default duration is 10 seconds.
     *
     * @see DeepLTranslator#sendRequest(JSONObject)
     */
    private static Duration TIMEOUT_DURATION = Duration.ofSeconds(10);

    /**
     * Used if error 1042901/"Too many requests." is thrown.
     * Request will be repeated when value is true.
     *
     * Default value is true.
     *
     * @see DeepLTranslator#translate(String, Language, Language)
     */
    private static boolean REPEAT_REQUEST = true;

    /**
     * Can only be used if {@link DeepLTranslator#REPEAT_REQUEST} is true.
     * This value represents the waiting time for repeating the request.
     *
     * Default duration is 3 seconds.
     *
     * @see DeepLTranslator#translate(String, Language, Language)
     */
    private static Duration REPEAT_REQUEST_TIME = Duration.ofSeconds(3);

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
     * - https response went wrong (e.g. {@code HttpTimeoutException})
     *
     * @param text source text
     * @param from source language
     * @param to target language
     *
     * @return the translation
     * @throws Exception an exception
     */
    public static Translation translate(String text, Language from, Language to) throws Exception {
        if(text == null || text.isEmpty()) {
            throw new IllegalStateException("Text is null or empty");
        } else if(from == null || to == null) {
            throw new IllegalStateException("Language is null");
        } else if(text.length() > 5000) {
            throw new IllegalStateException("Text length is limited to 5000 characters");
        }

        JSONObject request = getJsonRequest(text, from, to);
        JSONObject response = sendRequest(request);

        if(REPEAT_REQUEST) {
            if (response.has("error")) {
                JSONObject error = response.getJSONObject("error");

                if ((error.has("code") && error.getInt("code") == 1042901)
                        || (error.has("message") && error.getString("message").equals("Too many requests."))) {
                    Thread.sleep(REPEAT_REQUEST_TIME.toMillis());
                    return translate(text, from, to);
                }
            }
        }

        return new Translation(response);
    }

    /**
     * Translates a text, which is limited to 5000 characters,
     * from a source {@code Language} (from) to a target {@code Language} (to).
     *
     * The {@link TranslationConsumer} is used, to handle translations
     * and exceptions.
     *
     * Possible reasons for exceptions:
     * - text is null or empty
     * - {@code Language} is null
     * - text length exceeds the limit of 5000 characters
     * - https response went wrong (e.g. {@code HttpTimeoutException})
     *
     * @param text source text
     * @param from source language
     * @param to target language
     * @param translationConsumer handles {@code Translation}s and {@code Exception}s
     *
     * @see DeepLTranslator#translate(String, Language, Language)
     */
    public static void asyncTranslate(String text, Language from, Language to, TranslationConsumer translationConsumer) {
        EXECUTOR.submit(() -> {
            try {
                Translation translation = translate(text, from, to);
                translationConsumer.handleTranslation(translation);
            } catch (Exception e) {
                translationConsumer.handleException(e);
            }
        });
    }

    /**
     * This method generates a json string to
     * create a translation request to the server.
     *
     * @param text source text
     * @param from source language
     * @param to target language
     *
     * @return a {@link JSONObject}
     */
    private static JSONObject getJsonRequest(String text, Language from, Language to) {
        JSONObject send = new JSONObject();
        send.put("jsonrpc", "2.0");
        send.put("method", "LMT_handle_jobs");

        JSONObject paramsObject = new JSONObject();

        JSONArray jobsArray = new JSONArray();

        JSONObject jobsObject = new JSONObject();
        jobsObject.put("kind", "default");
        jobsObject.put("raw_en_sentence", text);

        jobsArray.put(jobsObject);

        JSONObject langObject = new JSONObject();

        JSONArray userPreferredLangsArray = new JSONArray();
        userPreferredLangsArray.put(from.getLanguageCode());
        userPreferredLangsArray.put(to.getLanguageCode());

        langObject.put("user_preferred_langs", userPreferredLangsArray);
        langObject.put("source_lang_user_selected", from.getLanguageCode());
        langObject.put("target_lang", to.getLanguageCode());

        paramsObject.put("jobs", jobsArray);
        paramsObject.put("lang", langObject);

        send.put("params", paramsObject);

        return send;
    }

    /**
     * This methods sends a json request to the {@link DeepLTranslator#BASE_URL},
     * the translation is in the json response.
     *
     * If the response is not received within the specified {@link DeepLTranslator#TIMEOUT_DURATION}
     * then a {@link jdk.incubator.http.HttpTimeoutException} is thrown.
     *
     * @param json the response json {@link DeepLTranslator#getJsonRequest(String, Language, Language)}
     *
     * @return the response as a {@link JSONObject}
     * @throws Exception if https response went wrong (e.g. {@link jdk.incubator.http.HttpTimeoutException})
     */
    private static JSONObject sendRequest(JSONObject json) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder(new URI(BASE_URL))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .timeout(TIMEOUT_DURATION)
                .POST(HttpRequest.BodyProcessor.fromString(json.toString()))
                .build();

        HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandler.asString());
        String jsonResponse = httpResponse.body();

        return new JSONObject(jsonResponse);
    }

    /**
     * Should be used at the end of the program.
     * It shutdowns the executor. If it isn't
     * called, the program cannot stop.
     */
    public static void shutdown() {
        EXECUTOR.shutdown();
    }

    /**
     * Returns the executor which is responsible
     * to the asynchronous requests.
     *
     * @return executor
     */
    public static ExecutorService getExecutor() {
        return EXECUTOR;
    }

    /**
     * Sets the executor which is responsible
     * to the asynchronous requests.
     *
     * @param executor executor
     */
    public static void setExecutor(ExecutorService executor) {
        DeepLTranslator.EXECUTOR = executor;
    }

    /**
     * Returns the timeout duration.
     *
     * If the http response is not received within the specified timeout
     * then a {@link jdk.incubator.http.HttpTimeoutException} is thrown.
     *
     * @return timeout duration
     */
    public static Duration getTimeoutDuration() {
        return TIMEOUT_DURATION;
    }

    /**
     * Sets the timeout duration.
     *
     * If the http response is not received within the specified timeout
     * then a {@link jdk.incubator.http.HttpTimeoutException} is thrown.
     *
     * @param timeoutDuration timeout duration
     */
    public static void setTimeoutDuration(Duration timeoutDuration) {
        TIMEOUT_DURATION = timeoutDuration;
    }

    /**
     * Used if error 1042901/"Too many requests." is thrown.
     * Returns whether the client should repeat the request.
     *
     * @return repeat request
     */
    public static boolean isRepeatRequest() {
        return REPEAT_REQUEST;
    }

    /**
     * Used if error 1042901/"Too many requests." is thrown.
     * Sets whether the client should repeat the request.
     *
     * @param repeatRequest repeat request
     */
    public static void setRepeatRequest(boolean repeatRequest) {
        REPEAT_REQUEST = repeatRequest;
    }

    /**
     * Returns the waiting time for repeating a request.
     *
     * @return waiting time as {@link Duration}
     */
    public static Duration getRepeatRequestTime() {
        return REPEAT_REQUEST_TIME;
    }

    /**
     * Sets the waiting time for repeating a request.
     *
     * @param repeatRequestTime waiting time
     */
    public static void setRepeatRequestTime(Duration repeatRequestTime) {
        REPEAT_REQUEST_TIME = repeatRequestTime;
    }
}
