package de.linus.deepltranslator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * Represents a short and simple form
 * of the response from the server.
 */
public final class Translation {

    private JSONObject response;

    private boolean hasError;
    private OptionalInt errorCode;
    private Optional<String> errorMessage;

    private Optional<Language> sourceLanguage;
    private Optional<Language> targetLanguage;

    private List<Beam> beamList;
    private List<String> translationList;

    /**
     * The translation class parses the {@link JSONObject}
     * from the server and simplifies the values.
     *
     * This class represents a short and simple form
     * of the {@link JSONObject}.
     *
     * @param response response of the server
     */
    Translation(JSONObject response) {
        this.response = response;
        
        this.beamList = new ArrayList<>();
        this.translationList = new ArrayList<>();

        this.errorCode = OptionalInt.empty();
        this.errorMessage = Optional.empty();
        this.sourceLanguage = Optional.empty();
        this.targetLanguage = Optional.empty();

        parse();
    }

    /**
     * Parses the response to get translations etc.
     */
    private void parse() {
        if(response.has("error")) {
            JSONObject error = response.getJSONObject("error");

            if(error.has("code")) {
                errorCode = OptionalInt.of(error.getInt("code"));
            }

            if(error.has("message")) {
                errorMessage = Optional.of(error.getString("message"));
            }

            hasError = true;
        }

        if(response.has("result")) {
            JSONObject result = response.getJSONObject("result");

            if(result.has("source_lang")) {
                sourceLanguage = Language.getLanguage(result.getString("source_lang"));
            }

            if(result.has("target_lang")) {
                sourceLanguage = Language.getLanguage(result.getString("target_lang"));
            }

            if(result.has("translations")) {
                JSONArray translations = result.getJSONArray("translations");

                if(translations.length() > 0) {
                    JSONObject firstTranslation = translations.getJSONObject(0);

                    if(firstTranslation.has("beams")) {
                        JSONArray beams = firstTranslation.getJSONArray("beams");

                        for(int i = 0; i < beams.length(); i++) {
                            JSONObject beam = beams.getJSONObject(i);

                            if(beam.has("postprocessed_sentence")) {
                                String translation = beam.getString("postprocessed_sentence");
                                Beam beamObject = new Beam(translation);

                                if(beam.has("score")) {
                                    beamObject.setScore(beam.getDouble("score"));
                                }

                                if(beam.has("totalLogProb")) {
                                    beamObject.setTotalLogProb(beam.getDouble("totalLogProb"));
                                }

                                if(beam.has("num_symbols")) {
                                    beamObject.setNumSymbols(beam.getInt("num_symbols"));
                                }

                                beamList.add(beamObject);
                                translationList.add(translation);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns the response of the server as a {@link JSONObject}.
     *
     * @return response
     */
    public JSONObject getResponse() {
        return response;
    }

    /**
     * Returns true if an error occurred.
     *
     * @return error occurred
     */
    public boolean hasError() {
        return hasError;
    }

    /**
     * Returns the error code.
     *
     * @return error code
     */
    public OptionalInt getErrorCode() {
        return errorCode;
    }

    /**
     * Returns the error message.
     *
     * Possible errors are:
     * - Too many requests
     * - Parse error
     * - Etc.
     *
     * @return error message
     */
    public Optional<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Prints out the error.
     *
     * @return whether an error occurred
     */
    public boolean printError() {
        if(!hasError) {
            return false;
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[DeepLTranslator] Error");

        if(getErrorCode().isPresent()) {
            stringBuilder.append(" ").append(getErrorCode().getAsInt());
        }

        if(getErrorMessage().isPresent()) {
            stringBuilder.append(": ").append(getErrorMessage().get());
        }

        System.err.println(stringBuilder.toString());
        return true;
    }

    /**
     * Returns the source language (from).
     *
     * @return the language
     */
    public Optional<Language> getSourceLanguage() {
        return sourceLanguage;
    }

    /**
     * Returns the target language (to).
     *
     * @return the language
     */
    public Optional<Language> getTargetLanguage() {
        return targetLanguage;
    }

    /**
     * Returns true if there are some translations.
     * If it's false, there aren't any translations.
     *
     * @return has translations
     */
    public boolean hasTranslations() {
        return beamList.size() > 0 && translationList.size() > 0;
    }

    /**
     * Returns the beam list of the translation.
     *
     * It's a list, because there are many
     * different translations.
     *
     * @return beam list
     * @see Beam
     */
    public List<Beam> getBeamList() {
        return beamList;
    }

    /**
     * Returns the translation list.
     *
     * It's a list, because there are many
     * different translations.
     *
     * @return translation list
     */
    public List<String> getTranslations() {
        return translationList;
    }

    /**
     * A beam has beside the translation (postProcessedSentence)
     * also some other specifications (score, totalLogProb & numSymbols).
     */
    private final class Beam {

        private OptionalDouble score;
        private OptionalDouble totalLogProb;
        private String postProcessedSentence;
        private OptionalInt numSymbols;

        /**
         * Creates a beam with a translation.
         *
         * @param postProcessedSentence translation
         */
        Beam(String postProcessedSentence) {
            this.postProcessedSentence = postProcessedSentence;
            this.score = OptionalDouble.empty();
            this.totalLogProb = OptionalDouble.empty();
            this.numSymbols = OptionalInt.empty();
        }

        /**
         * Returns the score.
         */
        public OptionalDouble getScore() {
            return score;
        }

        /**
         * Sets the score of the beam.
         *
         * @param score score
         */
        void setScore(double score) {
            this.score = OptionalDouble.of(score);
        }

        /**
         * Returns the total log prob.
         *
         * @return total log prob
         */
        public OptionalDouble getTotalLogProb() {
            return totalLogProb;
        }

        /**
         * Sets the total log prob.
         *
         * @param totalLogProb total log prob
         */
        void setTotalLogProb(double totalLogProb) {
            this.totalLogProb = OptionalDouble.of(totalLogProb);
        }

        /**
         * Returns the translation.
         *
         * @return translation
         */
        public String getPostProcessedSentence() {
            return postProcessedSentence;
        }

        /**
         * Returns the amount of num symbols.
         *
         * @return num symbols
         */
        public OptionalInt getNumSymbols() {
            return numSymbols;
        }

        /**
         * Sets the amount of num symbols.
         *
         * @param numSymbols num symbols.
         */
        void setNumSymbols(int numSymbols) {
            this.numSymbols = OptionalInt.of(numSymbols);
        }
    }

}
