package cz.cvut.fel.ear.tm.exception;

public class ValidationException extends EarException{
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
