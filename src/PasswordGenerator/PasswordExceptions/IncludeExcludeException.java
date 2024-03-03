package PasswordGenerator.PasswordExceptions;

public class IncludeExcludeException extends RuntimeException {
    public IncludeExcludeException(String errorMessage) {
        super(errorMessage);
    }
}
