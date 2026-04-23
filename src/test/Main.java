package test;

import control.PID;

public class Main {
    public static void main(String[] args) {
        // Create a PID controller with specific gains
        PID pid = new PID(0.05, 0.001, 0.01);

        double target = 100.0; // The desired target value we want to reach
        double current = 0.0; // The current value that will be updated based on the PID output

        for (int step = 0; step < 50; step++) { // Simulate 50 time steps
            double output = pid.update(target, current); // Update the PID controller with the target and current value to get the output

            // Simulate the system response by updating
            // the current value based on the PID output
            current += output * 10.0;

            // Print the current step, target, current value, and PID output for debugging
            System.out.println(
                    "step=" + step +
                            " current=" + current +
                            " output=" + output
            );
        }
    }
}