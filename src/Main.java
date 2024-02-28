import PasswordGenerator.*;

public class Main {
    public static void main(String[] args) {
        PasswordGenerator password = new PasswordGenerator();
        password.setConstantShare(25, 20 , 30, 25);
        password.setLength(12);
        password.setRange(1, 2);
        password.setRandomShare();
        String testPassword = password.generate();
        password.setDefaultRules();
//        password.showRules();
        password.include('c', "special", false); // else are "upper", "lower", "number", "special"
        char[] additional = {'1', '2', '3', '4'};
        password.include(additional, "upper", true); // else are "upper", "lower", "number", "special"
        password.showCaseCollection("\t");
        // ^you can also provide char array | also takes 'u', 'l', 'n', 's'
        // ^takes with only first input also
        // password.exclude(" ", "special"); // else are "upper", "lower", "number", "special"
        // ^you can also provide char array | also takes 'u', 'l', 'n', 's'
        // ^takes with only first input also
    }
}