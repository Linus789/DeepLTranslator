package de.linus.deepltranslator;

/**
 * The error which occurred while trying to receive a translation.
 */
public class DeepLException extends Exception {

    private final String message;

    DeepLException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
