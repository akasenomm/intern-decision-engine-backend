package ee.taltech.inbankbackend.exceptions;

/**
 * Thrown when requested loan amount is invalid.
 */
public class InvalidLoanAmountException extends InvalidInputException {
    public InvalidLoanAmountException(String message) {
        super(message);
    }

}
