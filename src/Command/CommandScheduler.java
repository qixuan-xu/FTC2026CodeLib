package Command;

/**
 * FTC SDK Style Command Scheduler
 *
 * Manages sequential execution of commands in the FTC OpMode loop.
 * The scheduler handles the lifecycle of each command:
 *   - Initialization when scheduled
 *   - Repeated execution in the loop
 *   - Automated cleanup when finished
 *
 * Features:
 * - Only one command runs at a time (sequential execution)
 * - Automatic interrupt handling when new command is scheduled
 * - Non-blocking design (safe for OpMode loop)
 *
 * Usage example:
 *   CommandScheduler scheduler = new CommandScheduler();
 *
 *   while (opModeIsActive()) {
 *       // Schedule new commands as needed
 *       if (gamepad1.a) {
 *           scheduler.schedule(new MoveDistanceCommand(...));
 *       }
 *
 *       // Run scheduler every loop
 *       scheduler.run();
 *
 *       telemetry.addData("Running", scheduler.isCommandRunning());
 *   }
 */
public class CommandScheduler {
    private Command currentCommand = null;
    private boolean commandRunning = false;

    /**
     * Schedule a command to run.
     * If another command is currently running, it will be interrupted.
     * The new command's initialize() is called immediately.
     *
     * @param command the command to schedule (cannot be null)
     */
    public void schedule(Command command) {
        if (commandRunning) {
            // Interrupt current command
            if (currentCommand != null) {
                currentCommand.end(true);
            }
        }

        currentCommand = command;
        currentCommand.initialize();
        commandRunning = true;
    }

    /**
     * Cancel the currently running command immediately.
     * Calls end(true) on the command to notify it of interruption.
     * Safe to call if no command is running.
     */
    public void cancel() {
        if (commandRunning && currentCommand != null) {
            currentCommand.end(true);
            currentCommand = null;
            commandRunning = false;
        }
    }

    /**
     * Process the scheduler state machine in each OpMode loop.
     * Call this method EVERY loop (typically 10-50ms apart).
     *
     * This method:
     * 1. Calls execute() on the running command
     * 2. Checks isFinished()
     * 3. Calls end(false) when command is finished
     * 4. Cleans up state and allows next command to be scheduled
     *
     * Example loop:
     *   while (opModeIsActive()) {
     *       scheduler.run(); // Must be called every iteration!
     *       telemetry.update();
     *   }
     */
    public void run() {
        if (commandRunning && currentCommand != null) {
            currentCommand.execute();

            if (currentCommand.isFinished()) {
                currentCommand.end(false);
                currentCommand = null;
                commandRunning = false;
            }
        }
    }

    /**
     * Check if a command is currently running.
     * Useful for determining when to schedule the next action.
     *
     * @return true if a command is executing, false if idle
     */
    public boolean isCommandRunning() {
        return commandRunning;
    }

    /**
     * Get the currently running command for debugging/telemetry.
     * Useful for logging which command is active.
     *
     * @return reference to current command, or null if idle
     */
    public Command getCurrentCommand() {
        return currentCommand;
    }
}
