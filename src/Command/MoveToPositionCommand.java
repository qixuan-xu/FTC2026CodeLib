package Command;

import control.TeleOpController;
import utils.MathUtils;
import utils.Constants;
import sensor.Odometry;
import control.PID;

/**
 * Command to move robot to a target position using odometry feedback.
 * FTC SDK style command implementation.
 */
public class MoveToPositionCommand implements Command {
    private TeleOpController controller;
    private Odometry odometry;
    private double targetX;
    private double targetY;
    private double targetHeading;
    private long timeoutMs;

    private PID pidX;
    private PID pidY;
    private PID pidHeading;
    private long startTime;
    private boolean finished = false;

    /**
     * Constructor for MoveToPositionCommand.
     * @param controller TeleOpController for motor output
     * @param odometry odometry system for position tracking
     * @param targetX target X position in inches
     * @param targetY target Y position in inches
     * @param targetHeading target heading in degrees (-999 to keep current heading)
     * @param timeoutMs timeout in milliseconds (0 = no timeout)
     */
    public MoveToPositionCommand(TeleOpController controller, Odometry odometry,
                                double targetX, double targetY, double targetHeading, long timeoutMs) {
        this.controller = controller;
        this.odometry = odometry;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetHeading = targetHeading;
        this.timeoutMs = timeoutMs;

        this.pidX = new PID();
        this.pidY = new PID();
        this.pidHeading = new PID();
    }

    @Override
    public void initialize() {
        pidX.reset();
        pidY.reset();
        pidHeading.reset();
        startTime = System.currentTimeMillis();
        finished = false;
    }

    @Override
    public void execute() {
        long currentTime = System.currentTimeMillis();

        // Check timeout
        if (timeoutMs > 0 && currentTime - startTime >= timeoutMs) {
            finished = true;
            return;
        }

        // Update odometry position
        odometry.updatePosition();

        double currentX = odometry.getX();
        double currentY = odometry.getY();
        double currentHeading = odometry.getHeadingDegrees();

        // Calculate errors
        double errorX = targetX - currentX;
        double errorY = targetY - currentY;
        double errorHeading = (targetHeading != -999) ? targetHeading - currentHeading : 0;

        // Normalize heading error to -180 to 180
        while (errorHeading > 180) errorHeading -= 360;
        while (errorHeading < -180) errorHeading += 360;

        // Check if close enough to target
        if (Math.abs(errorX) < 0.5 && Math.abs(errorY) < 0.5 && Math.abs(errorHeading) < 2.0) {
            finished = true;
            return;
        }

        // PID control
        double dt = 0.02; // Assume 20ms loop time for FTC
        double speedX = pidX.update(targetX, currentX, dt);
        double speedY = pidY.update(targetY, currentY, dt);
        double turn = pidHeading.update(targetHeading != -999 ? targetHeading : currentHeading, currentHeading, dt);

        // Clamp values
        speedX = MathUtils.clamp(speedX, -1.0, 1.0);
        speedY = MathUtils.clamp(speedY, -1.0, 1.0);
        turn = MathUtils.clamp(turn, -1.0, 1.0);

        controller.update(speedX, speedY, turn);
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public void end(boolean interrupted) {
        controller.update(0, 0, 0); // Always stop motors
    }
}
