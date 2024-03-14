import PasswordGenerator.*;
public class Main {
    public static void main(String[] args) {
        PasswordGenerator password = new PasswordGenerator();
        String createdPassword = password.generate();
        System.out.println(createdPassword);
    }
}