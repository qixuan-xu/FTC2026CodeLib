package sensor;

import utils.Constants;
import utils.MathUtils;

/**
 * Three-Wheel Odometry System (三轮里程计)
 *
 * Tracks robot position and heading using three potentiometers:
 * - Two parallel wheels (left and right) for X position and rotation
 * - One perpendicular wheel (back) for Y position
 *
 * How it works:
 * 1. Each potentiometer measures distance traveled by its wheel
 * 2. Differences between wheel positions calculate heading changes
 * 3. Robot position calculated from heading + distance
 * 4. All calculations use Constants.TRACK_WIDTH for accuracy
 *
 * Coordinate system:
 * - X-axis: Forward/Backward (0° = forward)
 * - Y-axis: Left/Right (90° = right)
 * - Heading: Robot rotation (radians, 0° = forward)
 *
 * Position units: inches
 * Heading units: radians (convertible to degrees via getHeadingDegrees())
 *
 * Usage:
 *   Potentiometer left = new Potentiometer();
 *   Potentiometer right = new Potentiometer();
 *   Potentiometer back = new Potentiometer();
 *   Odometry odometry = new Odometry(left, right, back);
 *
 *   // In OpMode loop:
 *   odometry.updatePosition(); // Call every loop
 *   double x = odometry.getX();
 *   double y = odometry.getY();
 *   double heading = odometry.getHeadingDegrees();
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
     * MUST call this every control loop for accurate position tracking (typically every 10-50ms).
     *
     * Algorithm:
     * 1. Read current potentiometer distances
     * 2. Calculate delta (change) for each wheel
     * 3. Calculate heading change from parallel wheel difference
     * 4. Apply rotation to movement vector
     * 5. Update global position and heading
     *
     * Math (三轮里程计数学):
     * - deltaHeading = (deltaRight - deltaLeft) / TRACK_WIDTH
     * - Movement rotated by current heading for accurate position
     *
     * Precision note:
     * - Position accuracy depends on potentiometer calibration
     * - Call frequently for best results
     * - Consider resetting position after major recalibration
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

        // Calculate local movement. X is forward/backward, Y is strafe.
        double deltaForwardLocal = (deltaLeft + deltaRight) / 2.0;
        double deltaStrafeLocal = deltaBack - Constants.LATERAL_OFFSET * deltaHeading;

        // If heading changed, rotate the local movement
        if (Math.abs(deltaHeading) > 0.001) {
            double cos = Math.cos(currentHeading + deltaHeading / 2.0);
            double sin = Math.sin(currentHeading + deltaHeading / 2.0);

            double deltaXGlobal = deltaForwardLocal * cos - deltaStrafeLocal * sin;
            double deltaYGlobal = deltaForwardLocal * sin + deltaStrafeLocal * cos;

            currentX += deltaXGlobal;
            currentY += deltaYGlobal;
        } else {
            // No heading change, simple addition
            currentX += deltaForwardLocal * Math.cos(currentHeading) - deltaStrafeLocal * Math.sin(currentHeading);
            currentY += deltaForwardLocal * Math.sin(currentHeading) + deltaStrafeLocal * Math.cos(currentHeading);
        }

        currentHeading += deltaHeading;

        // Normalize heading to -π to π
        currentHeading = Math.toRadians(MathUtils.normalizeAngle(Math.toDegrees(currentHeading)));

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
        currentHeading = Math.toRadians(MathUtils.normalizeAngle(Math.toDegrees(heading)));

        prevLeftPos = leftWheel.getDistance();
        prevRightPos = rightWheel.getDistance();
        prevBackPos = backWheel.getDistance();
    }
}
