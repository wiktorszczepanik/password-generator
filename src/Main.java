import PasswordGenerator.*;
public class Main {
    public static void main(String[] args) {
        PasswordGenerator password = new PasswordGenerator();
        password.setRandomShare(0.2);
//        password.showCaseCollection("\t");
//        System.out.println("");
//        char[] additional = {'"', '&'};
//        password.exclude(additional, 2);
//        password.showCaseCollection("\t");
        password.showRules();
        // test all
        // clean code
        // Add readme.md
    }
}