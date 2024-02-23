import PasswordGenerator.*;

public class Main {
    public static void main(String[] args) {
        PasswordGenerator password = new PasswordGenerator();
        password.setLength(10);
        password.setRange(10, 15);
        password.setConstantShare(10, 40 , 25, 25);
        password.setRandomShare();
        System.out.println(password);
    }
}