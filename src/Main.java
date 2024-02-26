import PasswordGenerator.*;

public class Main {
    public static void main(String[] args) {
        PasswordGenerator password = new PasswordGenerator();
        password.setLength(30);
        password.setRange(1, 2);
        password.setConstantShare(25, 25 , 25, 25);
        password.setRandomShare();
        String testPassword = password.generate();
        password.setDefaultRules();
        password.showCaseCollection("\t");
        password.showRules();
        // password.include(" ", "special"); // else are "upper", "lower", "number", "special"
        // ^you can also provide char array | also takes 'u', 'l', 'n', 's'
        // ^takes with only first input also
        // password.exclude(" ", "special"); // else are "upper", "lower", "number", "special"
        // ^you can also provide char array | also takes 'u', 'l', 'n', 's'
        // ^takes with only first input also
    }
}