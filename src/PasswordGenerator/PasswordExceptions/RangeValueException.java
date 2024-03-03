package PasswordGenerator.PasswordExceptions;

public class RangeValueException extends RuntimeException {
    public RangeValueException(String errorMessage) {
        super(errorMessage);
    }
}
