package ee.taltech.inbankbackend.exceptions;

public class InvalidInputException extends DecisionEngineException {

    public InvalidInputException(String message) {
        super(message);
    }
}
