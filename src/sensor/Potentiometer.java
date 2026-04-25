package sensor;

import utils.Constants;

/**
 * Potentiometer sensor class for FTC robot.
 * Used to measure linear position (e.g., extension distance).
 */
public class Potentiometer {
    private double currentPosition; // 0.0 to 1.0 (normalized position)
    private double maxDistance;     // maximum distance in inches
    private double offset;          // offset in inches

    /**
     * Constructor for potentiometer.
     * @param maxDistance maximum measurable distance in inches
     * @param offset offset in inches (zero position)
     */
    public Potentiometer(double maxDistance, double offset) {
        this.maxDistance = maxDistance;
        this.offset = offset;
        this.currentPosition = 0.0;
    }

    /**
     * Constructor with default offset of 0.
     * @param maxDistance maximum measurable distance in inches
     */
    public Potentiometer(double maxDistance) {
        this(maxDistance, 0.0);
    }

    /**
     * Constructor with default values from Constants.
     */
    public Potentiometer() {
        this(Constants.POTENTIOMETER_MAX_DISTANCE, Constants.POTENTIOMETER_DEFAULT_OFFSET);
    }

    /**
     * Get the current position in inches.
     * @return current position in inches
     */
    public double getDistance() {
        return currentPosition * maxDistance + offset;
    }

    /**
     * Get the normalized position (0.0 to 1.0).
     * @return normalized position
     */
    public double getNormalizedPosition() {
        return currentPosition;
    }

    /**
     * Set the current position (for simulation/testing).
     * @param position normalized position (0.0 to 1.0)
     */
    public void setPosition(double position) {
        this.currentPosition = Math.max(0.0, Math.min(1.0, position));
    }

    /**
     * Calibrate the potentiometer to current position as zero.
     */
    public void calibrateZero() {
        this.offset = -getDistance();
    }

    /**
     * Check if potentiometer is at maximum position.
     * @return true if at max
     */
    public boolean isAtMax() {
        return currentPosition >= 0.99;
    }

    /**
     * Check if potentiometer is at minimum position.
     * @return true if at min
     */
    public boolean isAtMin() {
        return currentPosition <= 0.01;
    }
}
