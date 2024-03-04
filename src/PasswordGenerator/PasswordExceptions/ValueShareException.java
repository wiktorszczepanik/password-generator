package PasswordGenerator.PasswordExceptions;

public class ValueShareException extends RuntimeException {
    public ValueShareException(String type, int addUpTo) {
        super("Provided " + type + " do not adds up to " + addUpTo + ".");
    }
    public ValueShareException(int lowerBound, int upperBound) {
        super(
            "Provided number on index must be from closed interval between " + lowerBound + " and " + upperBound
        );
    }
    public ValueShareException(char min, String higherThan) {
        super ("Minimum value cannot be higher than" + higherThan);
    }
    public ValueShareException(String errorMessage) {
        super(errorMessage);
    }
}
