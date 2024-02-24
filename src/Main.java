import PasswordGenerator.*;

public class Main {
    public static void main(String[] args) {
        PasswordGenerator password = new PasswordGenerator();
        password.setLength(10);
        password.setRange(10, 15);
        password.setConstantShare(10, 40 , 25, 25);
        password.setRandomShare();
        password.showCaseCollection("\t");
        password.showRules();
//        for (int i = 0; i < 255; i++) {
//            System.out.println(i + "\t" + (char) i);
//        }
        // password.generate(); //
//        System.out.println(password);
    }
}