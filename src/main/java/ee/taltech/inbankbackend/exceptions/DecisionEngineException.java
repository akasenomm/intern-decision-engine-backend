package ee.taltech.inbankbackend.exceptions;

/**
 * General exception
 */
public class DecisionEngineException extends Throwable {
    private final String message;
    private final Throwable cause;

    public DecisionEngineException(String message) {
        this(message, null);
    }

    public DecisionEngineException(String message, Throwable cause) {
        this.message = message;
        this.cause = cause;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
