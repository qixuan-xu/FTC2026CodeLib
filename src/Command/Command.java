package Command;

/**
 * FTC SDK Style Command Interface
 * Based on FTC SDK Command framework pattern.
 */
public interface Command {
    /**
     * Called once when the command is started.
     */
    void initialize();

    /**
     * Called repeatedly while the command is running.
     */
    void execute();

    /**
     * Returns true when the command should end.
     * @return true if command is finished
     */
    boolean isFinished();

    /**
     * Called once when the command ends.
     * @param interrupted true if command was interrupted
     */
    void end(boolean interrupted);
}
