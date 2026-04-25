package sensor;

import utils.Constants;

/**
 * Odometry system using three potentiometers for FTC robot positioning.
 * Two parallel potentiometers (left/right) and one perpendicular (horizontal/back).
 */
public class Odometry {
    private Potentiometer leftWheel;
    private Potentiometer rightWheel;
    private Potentiometer backWheel;

    private double currentX;        // current X position in inches
    private double currentY;        // current Y position in inches
    private double currentHeading;  // current heading in radians

    private double prevLeftPos;
    private double prevRightPos;
    private double prevBackPos;

    /**
     * Constructor with three potentiometers.
     * @param leftWheel left parallel wheel potentiometer
     * @param rightWheel right parallel wheel potentiometer
     * @param backWheel horizontal back wheel potentiometer
     */
    public Odometry(Potentiometer leftWheel, Potentiometer rightWheel, Potentiometer backWheel) {
        this.leftWheel = leftWheel;
        this.rightWheel = rightWheel;
        this.backWheel = backWheel;

        this.currentX = 0.0;
        this.currentY = 0.0;
        this.currentHeading = 0.0;

        // Initialize previous positions
        this.prevLeftPos = leftWheel.getDistance();
        this.prevRightPos = rightWheel.getDistance();
        this.prevBackPos = backWheel.getDistance();
    }

    /**
     * Constructor with default potentiometers from Constants.
     */
    public Odometry() {
        this(new Potentiometer(), new Potentiometer(), new Potentiometer());
    }

    /**
     * Update the robot position based on potentiometer readings.
     * Call this method regularly to track position.
     */
    public void updatePosition() {
        double leftPos = leftWheel.getDistance();
        double rightPos = rightWheel.getDistance();
        double backPos = backWheel.getDistance();

        // Calculate deltas
        double deltaLeft = leftPos - prevLeftPos;
        double deltaRight = rightPos - prevRightPos;
        double deltaBack = backPos - prevBackPos;

        // Calculate heading change (average of parallel wheels)
        double deltaHeading = (deltaRight - deltaLeft) / Constants.TRACK_WIDTH;

        // Calculate local movement
        double deltaXLocal = deltaBack;
        double deltaYLocal = (deltaLeft + deltaRight) / 2.0;

        // If heading changed, rotate the local movement
        if (Math.abs(deltaHeading) > 0.001) {
            double cos = Math.cos(currentHeading + deltaHeading / 2.0);
            double sin = Math.sin(currentHeading + deltaHeading / 2.0);

            double deltaXGlobal = deltaXLocal * cos - deltaYLocal * sin;
            double deltaYGlobal = deltaXLocal * sin + deltaYLocal * cos;

            currentX += deltaXGlobal;
            currentY += deltaYGlobal;
        } else {
            // No heading change, simple addition
            currentX += deltaXLocal * Math.cos(currentHeading) - deltaYLocal * Math.sin(currentHeading);
            currentY += deltaXLocal * Math.sin(currentHeading) + deltaYLocal * Math.cos(currentHeading);
        }

        currentHeading += deltaHeading;

        // Normalize heading to -π to π
        while (currentHeading > Math.PI) currentHeading -= 2 * Math.PI;
        while (currentHeading < -Math.PI) currentHeading += 2 * Math.PI;

        // Update previous positions
        prevLeftPos = leftPos;
        prevRightPos = rightPos;
        prevBackPos = backPos;
    }

    /**
     * Get current X position in inches.
     * @return X position
     */
    public double getX() {
        return currentX;
    }

    /**
     * Get current Y position in inches.
     * @return Y position
     */
    public double getY() {
        return currentY;
    }

    /**
     * Get current heading in radians.
     * @return heading in radians
     */
    public double getHeading() {
        return currentHeading;
    }

    /**
     * Get current heading in degrees.
     * @return heading in degrees
     */
    public double getHeadingDegrees() {
        return Math.toDegrees(currentHeading);
    }

    /**
     * Reset position to (0,0) with heading 0.
     */
    public void resetPosition() {
        currentX = 0.0;
        currentY = 0.0;
        currentHeading = 0.0;

        prevLeftPos = leftWheel.getDistance();
        prevRightPos = rightWheel.getDistance();
        prevBackPos = backWheel.getDistance();
    }

    /**
     * Set current position manually.
     * @param x X position in inches
     * @param y Y position in inches
     * @param heading heading in radians
     */
    public void setPosition(double x, double y, double heading) {
        currentX = x;
        currentY = y;
        currentHeading = heading;
    }
}
