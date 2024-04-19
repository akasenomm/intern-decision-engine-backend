package ee.taltech.inbankbackend.exceptions;

/**
 * Thrown when provided personal ID code is invalid.
 */
public class InvalidPersonalCodeException extends InvalidInputException {


    public InvalidPersonalCodeException(String message) {
        super(message);
    }
}
