package ee.taltech.inbankbackend.exceptions;

/**
 * Thrown when any request parameter is invalid.
 */
public class InvalidInputException extends DecisionEngineException {

    public InvalidInputException(String message) {
        super(message);
    }
}
