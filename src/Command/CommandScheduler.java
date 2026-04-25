package Command;

/**
 * Simple Command Scheduler for FTC SDK style commands.
 * Manages execution of commands in FTC OpMode loop.
 */
public class CommandScheduler {
    private Command currentCommand = null;
    private boolean commandRunning = false;

    /**
     * Schedule a command to run. Only one command can run at a time.
     * @param command the command to schedule
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
     * Cancel the currently running command.
     */
    public void cancel() {
        if (commandRunning && currentCommand != null) {
            currentCommand.end(true);
            currentCommand = null;
            commandRunning = false;
        }
    }

    /**
     * Run the scheduler. Call this in OpMode loop.
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
     * @return true if command is running
     */
    public boolean isCommandRunning() {
        return commandRunning;
    }

    /**
     * Get the currently running command.
     * @return current command or null
     */
    public Command getCurrentCommand() {
        return currentCommand;
    }
}
