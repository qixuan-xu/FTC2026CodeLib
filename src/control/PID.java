package control;
// A simple PID controller implementation in Java.
// This class allows you to create a PID controller

import utils.MathUtils;
import utils.Constants;

/**
 * PID (Proportional-Integral-Derivative) Controller Implementation
 *
 * Closed-loop control system for precise robot movement and stabilization.
 * Continuously adjusts motor power to reach and maintain target values.
 *
 * Three Terms:
 * 1. Proportional (P): Responds proportionally to current error
 *    - output += kP * error
 *    - Larger kP = faster response, but may cause oscillation
 *
 * 2. Integral (I): Corrects accumulated error over time
 *    - output += kI * integral(error)
 *    - Eliminates steady-state error, but can cause overshoot
 *
 * 3. Derivative (D): Responds to rate of error change
 *    - output += kD * (error - lastError)
 *    - Dampens oscillation and improves stability
 *
 * Formula (each update):
 *   error = target - current
 *   integral = integral + error * dt
 *   derivative = (error - lastError) / dt
 *   output = (kP * error) + (kI * integral) + (kD * derivative)
 *
 * Tuning Guide:
 * - Start with kP only, increase until oscillation appears
 * - Add kD to reduce oscillation
 * - Add kI if controller doesn't reach target
 * - Typical range: kP=[0.1-1.0], kI=[0.0-0.1], kD=[0.01-0.5]
 *
 * Default values from Constants.java:
 *   DRIVE_KP = 0.8
 *   DRIVE_KI = 0.0
 *   DRIVE_KD = 0.1
 *
 * Usage:
 *   PID pidController = new PID(); // Uses Constants defaults
 *
 *   while (moving) {
 *       double dt = (currentTime - lastTime) / 1000.0;
 *       double output = pidController.update(targetValue, currentValue, dt);
 *       motor.setPower(output);
 *   }
 *
 * Safety Features:
 * - Output clamping: Prevents motor commands from exceeding [-1.0, 1.0]
 * - Integral windup prevention: Limits accumulated integral
 * - Supports reset() for state clearing
 */
public class PID {
    private double kP;
    private double kI;
    private double kD;

    private double integral;
    private double lastError;

    private double minOutput;
    private double maxOutput;

    private double minIntegral;
    private double maxIntegral;

    /**
     * Constructs a PID controller with the given coefficients.
     * @param kP Proportional gain
     * @param kI Integral gain
     * @param kD Derivative gain
     */
    public PID(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;

        this.integral = 0.0;
        this.lastError = 0.0;

        this.minOutput = Constants.PID_MIN_OUTPUT;
        this.maxOutput = Constants.PID_MAX_OUTPUT;

        this.minIntegral = Constants.PID_MIN_INTEGRAL;
        this.maxIntegral = Constants.PID_MAX_INTEGRAL;
    }

    /**
     * Constructs a PID controller with the default coefficients from Constants.
     */
    public PID() {
        this(Constants.DRIVE_KP, Constants.DRIVE_KI, Constants.DRIVE_KD);
    }

    /**
     * Updates the PID controller with the current target and measured value, and the time step.
     * @param target The desired target value
     * @param current The current measured value
     * @param dt The time step since the last update (in seconds)
     * @return The calculated output value, clamped to the output limits
     */
    public double update(double target, double current, double dt) {
        double error = target - current;

        double derivative = 0.0;
        if (dt > 0.0) {
            integral += error * dt;
            integral = MathUtils.clamp(integral, minIntegral, maxIntegral);
            derivative = (error - lastError) / dt;
        }
        lastError = error;

        double output = (kP * error) + (kI * integral) + (kD * derivative);
        return MathUtils.clamp(output, minOutput, maxOutput);
    }

    /**
     * Resets the integral and last error to zero.
     */
    public void reset() {
        integral = 0.0;
        lastError = 0.0;
    }

    /**
     * Sets the output limits for the PID controller.
     * @param minOutput Minimum output value
     * @param maxOutput Maximum output value
     */
    public void setOutputLimits(double minOutput, double maxOutput) {
        this.minOutput = minOutput;
        this.maxOutput = maxOutput;
    }

    /**
     * Sets the integral limits to prevent integral windup.
     * @param minIntegral Minimum integral value
     * @param maxIntegral Maximum integral value
     */
    public void setIntegralLimits(double minIntegral, double maxIntegral) {
        this.minIntegral = minIntegral;
        this.maxIntegral = maxIntegral;
    }

    /**
     * Sets the PID coefficients.
     * @param kP Proportional gain
     * @param kI Integral gain
     * @param kD Derivative gain
     */
    public void setCoefficients(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }
}
