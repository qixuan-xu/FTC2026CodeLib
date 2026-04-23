package test;
// This is a simple test program to demonstrate the use of the
// DriveMath class for calculating mecanum wheel powers based
// on user input for forward, strafe, and turn movements. The
// program continuously prompts the user to enter values for
// forward, strafe, and turn, and then calculates and displays
// the corresponding power for each of the four mecanum wheels
// (front left, front right, back left, back right) using the
// DriveMath.mecanum method. The output is formatted to show
// the power for each wheel with two decimal places for easy
// reading. This allows users to see how different combinations
// of forward, strafe, and turn inputs affect the wheel powers,
// which is useful for understanding how to control a robot with
// mecanum wheels effectively.
import hardware.DriveMath;
import input.InputProcessor;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        InputProcessor processor = new InputProcessor(0.05, 3);

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("forward strafe turn: ");
            double forward = sc.nextDouble();
            double strafe  = sc.nextDouble();
            double turn    = sc.nextDouble();

            double[] p = DriveMath.mecanum(forward, strafe, turn);

            System.out.printf("FL=%.2f FR=%.2f BL=%.2f BR=%.2f%n",
                    p[0], p[1], p[2], p[3]);
        }
    }
}