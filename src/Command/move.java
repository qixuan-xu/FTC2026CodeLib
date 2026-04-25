package Command;

import control.TeleOpController;
import utils.MathUtils;
import utils.Constants;

/**
 * Move command for FTC robot.
 * Provides two movement methods: by distance and by speed/time.
 */
public class move {
    private TeleOpController controller;

    public move(TeleOpController controller) {
        this.controller = controller;
    }

    /**
     * Move the robot a specified distance in a given direction with optional turn.
     * @param distance the distance to move
     * @param speed the movement speed (0.0 to 1.0)
     * @param direction the direction to move (0-360 degrees, 0=forward, 90=right, 180=backward, 270=left)
     * @param turn the turn speed while moving (only affects yaw, doesn't affect x,y movement)
     * @return true if movement completed successfully
     */
    public boolean MoveDistance(double distance, double speed, double direction, double turn) {
        speed = MathUtils.clamp(speed, 0.0, 1.0);
        turn = MathUtils.clamp(turn, -1.0, 1.0);

        // Convert direction from degrees to radians
        double directionRad = Math.toRadians(direction);

        // Calculate x and y components based on direction
        double x = Math.sin(directionRad) * speed;
        double y = Math.cos(directionRad) * speed;

        // Calculate time needed to cover distance at given speed
        double maxSpeed = Math.sqrt(x * x + y * y);
        double timeNeeded = maxSpeed > 0 ? distance / maxSpeed : 0;

        // Execute movement for calculated time
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < (long) (timeNeeded * 1000)) {
            controller.update(x, y, turn);
            try {
                Thread.sleep(Constants.UPDATE_INTERVAL_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }

        return true;
    }

    /**
     * Move the robot at a specified speed for a given time with optional turn.
     * @param speed the movement speed (0.0 to 1.0)
     * @param time the time to move in seconds
     * @param direction the direction to move (0-360 degrees, 0=forward, 90=right, 180=backward, 270=left)
     * @param turn the turn speed while moving (only affects yaw, doesn't affect x,y movement)
     * @return true if movement completed successfully
     */
    public boolean MoveSpeedTime(double speed, double time, double direction, double turn) {
        speed = MathUtils.clamp(speed, 0.0, 1.0);
        turn = MathUtils.clamp(turn, -1.0, 1.0);

        // Convert direction from degrees to radians
        double directionRad = Math.toRadians(direction);

        // Calculate x and y components based on direction
        double x = Math.sin(directionRad) * speed;
        double y = Math.cos(directionRad) * speed;

        // Execute movement for specified time
        long startTime = System.currentTimeMillis();
        long timeMillis = (long) (time * 1000);

        while (System.currentTimeMillis() - startTime < timeMillis) {
            controller.update(x, y, turn);
            try {
                Thread.sleep(Constants.UPDATE_INTERVAL_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }

        // Stop movement
        controller.update(0, 0, 0);

        return true;
    }

    /**
     * Stop the robot immediately.
     */
    public void stop() {
        controller.update(0, 0, 0);
    }
}
