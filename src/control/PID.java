package control;
// A simple PID controller implementation in Java.
// This class allows you to create a PID controller

import utils.MathUtils;

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

    public double update(double target, double current) {
        double error = target - current;

        integral += error;
        integral = MathUtils.clamp(integral, minIntegral, maxIntegral);

        double derivative = error - lastError;
        lastError = error;

        double output = (kP * error) + (kI * integral) + (kD * derivative);
        return MathUtils.clamp(output, minOutput, maxOutput);
    }

    public void reset() {
        integral = 0.0;
        lastError = 0.0;
    }

    public void setOutputLimits(double minOutput, double maxOutput) {
        this.minOutput = minOutput;
        this.maxOutput = maxOutput;
    }

    public void setIntegralLimits(double minIntegral, double maxIntegral) {
        this.minIntegral = minIntegral;
        this.maxIntegral = maxIntegral;
    }

    public void setCoefficients(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }
}