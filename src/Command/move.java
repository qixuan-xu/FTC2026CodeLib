package Command;

import control.TeleOpController;
import sensor.Potentiometer;
import sensor.Odometry;

/**
 * FTC Robot Movement Command Factory
 * Creates FTC SDK style commands for robot movement.
 * Provides factory methods for different movement types.
 */
public class Move {
    private TeleOpController controller;

    /**
     * Constructor for Move factory.
     * @param controller TeleOpController for motor output
     */
    public Move(TeleOpController controller) {
        this.controller = controller;
    }

    /**
     * Create a command to move the robot a specified distance using PID control with potentiometer feedback.
     * @param potentiometer the potentiometer sensor for distance measurement
     * @param targetDistance the target distance to move in inches (positive = forward, negative = backward)
     * @param direction the direction to move (0-360 degrees, 0=forward, 90=right, 180=backward, 270=left)
     * @param turn the turn speed while moving (-1.0 to 1.0, only affects yaw)
     * @param timeoutMs timeout in milliseconds (0 = no timeout)
     * @return MoveDistanceCommand instance
     */
    public MoveDistanceCommand moveDistance(Potentiometer potentiometer, double targetDistance,
                                          double direction, double turn, long timeoutMs) {
        return new MoveDistanceCommand(controller, potentiometer, targetDistance, direction, turn, timeoutMs);
    }

    /**
     * Create a command to move the robot at a specified speed for a given time with optional turn.
     * @param speed the movement speed (0.0 to 1.0)
     * @param time the time to move in seconds
     * @param direction the direction to move (0-360 degrees, 0=forward, 90=right, 180=backward, 270=left)
     * @param turn the turn speed while moving (-1.0 to 1.0, only affects yaw)
     * @return MoveSpeedTimeCommand instance
     */
    public MoveSpeedTimeCommand moveSpeedTime(double speed, double time, double direction, double turn) {
        return new MoveSpeedTimeCommand(controller, speed, time, direction, turn);
    }

    /**
     * Create a command to move the robot to a target position using odometry feedback.
     * @param odometry the odometry system for position tracking
     * @param targetX target X position in inches
     * @param targetY target Y position in inches
     * @param targetHeading target heading in degrees (-999 to keep current heading)
     * @param timeoutMs timeout in milliseconds (0 = no timeout)
     * @return MoveToPositionCommand instance
     */
    public MoveToPositionCommand moveToPosition(Odometry odometry, double targetX, double targetY,
                                              double targetHeading, long timeoutMs) {
        return new MoveToPositionCommand(controller, odometry, targetX, targetY, targetHeading, timeoutMs);
    }
}
