package Command;

/**
 * FTC SDK Style Command Interface
 *
 * Base interface for all robot commands following the FTC command-based programming paradigm.
 * Commands represent discrete actions that the robot can perform (move, rotate, pickup, etc).
 *
 * Execution flow:
 *   1. initialize() - called once when command starts
 *   2. execute()    - called repeatedly in scheduler loop
 *   3. isFinished() - checked each loop to determine if command is done
 *   4. end()        - called once when command finishes or is interrupted
 *
 * Example:
 *   Command moveCmd = new MoveDistanceCommand(controller, potentiometer, 24.0, 0, 0, 5000);
 *   scheduler.schedule(moveCmd);
 *
 *   while (opModeIsActive()) {
 *       scheduler.run(); // calls initialize -> execute -> isFinished -> end
 *   }
 */
public interface Command {

    /**
     * Initialize the command when it is first scheduled.
     * Called ONCE at the beginning of command execution.
     *
     * Use this to:
     * - Set initial state variables
     * - Reset sensors
     * - Calculate target values
     */
    void initialize();

    /**
     * Execute the command logic.
     * Called REPEATEDLY in the OpMode loop (typically every 10-50ms).
     *
     * This method should:
     * - Calculate motor powers based on sensors/state
     * - Send commands to motors
     * - Update internal state
     * - NOT block or use Thread.sleep()
     */
    void execute();

    /**
     * Check if command execution is finished.
     * Called REPEATEDLY in the OpMode loop until returns true.
     *
     * Return true when:
     * - Goal is achieved (e.g., distance traveled, position reached)
     * - Timeout occurs
     * - Error state is detected
     *
     * @return true if command is finished, false to continue executing
     */
    boolean isFinished();

    /**
     * Called when command ends (either naturally or interrupted).
     * Called ONCE when isFinished() returns true or command is cancelled.
     *
     * Use this to:
     * - Stop motors (set power to 0)
     * - Save final state
     * - Log telemetry
     * - Cleanup resources
     *
     * @param interrupted true if command was cancelled (end() called before isFinished())
     *                   false if command finished naturally
     */
    void end(boolean interrupted);
}
