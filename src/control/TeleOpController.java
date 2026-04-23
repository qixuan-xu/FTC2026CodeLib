package control;

import hardware.DriveMath;
import input.InputProcessor;

public class TeleOpController {

    private final InputProcessor processor;

    public TeleOpController(double deadzone, double curvePower) {
        this.processor = new InputProcessor(deadzone, curvePower);
    }

    public double[] update(double forwardInput, double strafeInput, double turnInput) {
        double forward = processor.process(forwardInput);
        double strafe = processor.process(strafeInput);
        double turn = processor.process(turnInput);

        return DriveMath.mecanum(forward, strafe, turn);
    }
}