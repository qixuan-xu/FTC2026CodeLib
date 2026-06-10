package Command;

import control.TeleOpController;
import utils.MathUtils;
import utils.Constants;
import sensor.Potentiometer;
import control.PID;

/**
 * Command to move robot a specified distance using PID control with potentiometer feedback.
 * FTC SDK style command implementation.
 */
public class MoveDistanceCommand implements Command {
    private TeleOpController controller;
    private Potentiometer potentiometer;
    private double targetDistance;
    private double direction;
    private double turn;
    private long timeoutMs;

    private PID pid;
    private double startPosition;
    private double targetPosition;
    private long startTime;
    private boolean finished = false;

    /**
     * Constructor for MoveDistanceCommand.
     * @param controller TeleOpController for motor output
     * @param potentiometer potentiometer sensor for distance measurement
     * @param targetDistance target distance in inches (positive = forward, negative = backward)
     * @param direction direction to move (0-360 degrees, 0=forward, 90=right, 180=backward, 270=left)
     * @param turn turn speed while moving (-1.0 to 1.0, only affects yaw)
     * @param timeoutMs timeout in milliseconds (0 = no timeout)
     */
    public MoveDistanceCommand(TeleOpController controller, Potentiometer potentiometer,
                              double targetDistance, double direction, double turn, long timeoutMs) {
        this.controller = controller;
        this.potentiometer = potentiometer;
        this.targetDistance = targetDistance;
        this.direction = direction;
        this.turn = MathUtils.clamp(turn, -1.0, 1.0);
        this.timeoutMs = timeoutMs;

        this.pid = new PID(); // Uses Constants.DRIVE_KP, KI, KD
    }

    @Override
    public void initialize() {
        pid.reset();
        startPosition = potentiometer.getDistance();
        targetPosition = startPosition + targetDistance;
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

        double currentPosition = potentiometer.getDistance();
        double error = targetPosition - currentPosition;

        // Check if close enough to target
        if (Math.abs(error) < 0.1) { // 0.1 inch tolerance
            finished = true;
            return;
        }

        // PID control for speed
        double dt = 0.02; // Assume 20ms loop time for FTC
        double speed = pid.update(targetPosition, currentPosition, dt);
        speed = MathUtils.clamp(speed, -1.0, 1.0);

        // Calculate x and y components based on direction
        double directionRad = Math.toRadians(direction);
        double forward = Math.cos(directionRad) * speed;
        double strafe = Math.sin(directionRad) * speed;

        controller.update(forward, strafe, turn);
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
