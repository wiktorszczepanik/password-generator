import PasswordGenerator.*;

public class Main {
    public static void main(String[] args) {
        PasswordGenerator password = new PasswordGenerator();
        password.setRandomShare();
        password.showCaseCollection("\t");
        System.out.println("");
        char[] additional = {'"', '&'};
        password.exclude(additional, 2);
        password.showCaseCollection("\t");
        // Add auto group in: include, exclude - done
        // test auto group
        // Add random share but set minimum value in type
        // clean code
        // Add readme.md
    }
}