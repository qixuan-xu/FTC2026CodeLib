# FTC2026Code Source Documentation

This folder contains the robot control code for the FTC 2026 project. The code is organized into small packages for commands, control logic, drivetrain math, sensors, input processing, tests, and shared utilities.

## Commands

The command system follows an FTC-style command-based structure. Commands represent robot actions that can be scheduled, executed repeatedly, checked for completion, and safely ended.

### `Command`

Base interface for all commands.

Each command has this lifecycle:

1. `initialize()` — called once when the command starts
2. `execute()` — called repeatedly while the command is running
3. `isFinished()` — checked every loop to determine if the command is done
4. `end(boolean interrupted)` — called when the command finishes or is cancelled

### `CommandScheduler`

Runs one command at a time.

Responsibilities:

- Schedules commands
- Interrupts the current command when a new one is scheduled
- Calls `execute()` every loop
- Calls `end(false)` when the command finishes
- Supports command cancellation with `cancel()`

Typical usage:
