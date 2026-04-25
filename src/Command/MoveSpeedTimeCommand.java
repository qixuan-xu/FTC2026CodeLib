package Command;

import control.TeleOpController;
import utils.MathUtils;

/**
 * Command to move robot at a specified speed for a given time.
 * FTC SDK style command implementation.
 */
public class MoveSpeedTimeCommand implements Command {
    private TeleOpController controller;
    private double speed;
    private double time;
    private double direction;
    private double turn;

    private long startTime;
    private long timeMillis;
    private boolean finished = false;

    /**
     * Constructor for MoveSpeedTimeCommand.
     * @param controller TeleOpController for motor output
     * @param speed movement speed (0.0 to 1.0)
     * @param time time to move in seconds
     * @param direction direction to move (0-360 degrees, 0=forward, 90=right, 180=backward, 270=left)
     * @param turn turn speed while moving (-1.0 to 1.0, only affects yaw)
     */
    public MoveSpeedTimeCommand(TeleOpController controller, double speed, double time,
                               double direction, double turn) {
        this.controller = controller;
        this.speed = MathUtils.clamp(speed, 0.0, 1.0);
        this.time = time;
        this.direction = direction;
        this.turn = MathUtils.clamp(turn, -1.0, 1.0);
        this.timeMillis = (long) (time * 1000);
    }

    @Override
    public void initialize() {
        startTime = System.currentTimeMillis();
        finished = false;
    }

    @Override
    public void execute() {
        long currentTime = System.currentTimeMillis();

        // Check if time elapsed
        if (currentTime - startTime >= timeMillis) {
            finished = true;
            return;
        }

        // Calculate x and y components based on direction
        double directionRad = Math.toRadians(direction);
        double x = Math.sin(directionRad) * speed;
        double y = Math.cos(directionRad) * speed;

        controller.update(x, y, turn);
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
