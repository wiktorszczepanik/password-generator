package PasswordGenerator.PasswordExceptions;

public class ExactValueException extends RuntimeException {
    public ExactValueException(String errorMessage) {
        super(errorMessage);
    }
}
