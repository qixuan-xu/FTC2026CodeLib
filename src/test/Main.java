package test;
// A simple test class to demonstrate the TeleOpController functionality.
import control.TeleOpController;

public class Main {
    public static void main(String[] args) {
        TeleOpController controller = new TeleOpController(0.05, 3);

        double[] powers = controller.update(1.0, 0.0, 0.0);

        System.out.printf("Front Left:  %.3f%n", powers[0]);
        System.out.printf("Front Right: %.3f%n", powers[1]);
        System.out.printf("Back Left:   %.3f%n", powers[2]);
        System.out.printf("Back Right:  %.3f%n", powers[3]);
    }
}