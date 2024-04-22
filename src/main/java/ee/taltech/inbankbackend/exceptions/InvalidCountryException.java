package ee.taltech.inbankbackend.exceptions;

/**
 * Thrown when requested loan country is invalid.
 */
public class InvalidCountryException extends InvalidInputException {

    public InvalidCountryException(String message) {
        super(message);
    }
}
