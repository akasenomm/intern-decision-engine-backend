package ee.taltech.inbankbackend.exceptions;

/**
 * Thrown when no valid loan is found.
 */
public class NoValidLoanException extends DecisionEngineException {

    public NoValidLoanException(String message) {
        super(message);
    }
}
