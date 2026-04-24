package control;
// A simple PID controller implementation in Java.
// This class allows you to create a PID controller

import utils.MathUtils;

/**
 * A simple PID (Proportional-Integral-Derivative) controller implementation.
 * This controller calculates an output based on the difference between a target and current value,
 * using proportional, integral, and derivative terms to minimize error over time.
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

        this.minOutput = -1.0;
        this.maxOutput = 1.0;

        this.minIntegral = -1.0;
        this.maxIntegral = 1.0;
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

        integral += error * dt;
        integral = MathUtils.clamp(integral, minIntegral, maxIntegral);

        double derivative = (error - lastError) / dt;
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