package ee.taltech.inbankbackend.exceptions;

/**
 * Thrown when loan requester age is restricted.
 */
public class InvalidAgeException extends InvalidInputException {

    public InvalidAgeException(String message) {
        super(message);
    }
}
