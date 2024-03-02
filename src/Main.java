import PasswordGenerator.*;

public class Main {
    public static void main(String[] args) {
        PasswordGenerator password = new PasswordGenerator();
        password.setRandomShare();
        password.showCaseCollection("\t");
        System.out.println("");
        char[] additional = {'"'};
        password.exclude('"', 4);
        password.showCaseCollection("\t");
        // Add auto group
        // move exception files to other folder
        // clean code
        // Add readme.md
    }
}