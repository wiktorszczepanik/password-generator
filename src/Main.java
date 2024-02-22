import PasswordGenerator.*;

public class Main {
    public static void main(String[] args) {
        PasswordGenerator pg = new PasswordGenerator();
        pg.setPasswordLength(10);
        pg.setPasswordRange(10, 15);
//        pg.setGeneralShare(0.5f, 0.20f, 0.20d, 0.1);
        // pg.setRandomShare(true); // also false
        //
        System.out.println(pg);
    }
}