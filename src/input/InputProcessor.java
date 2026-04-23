package input;

import utils.MathUtils;
// This class processes input values by applying a deadzone and a curve transformation.
public class InputProcessor {

    private double deadzone;
    private double curvePower;

    public InputProcessor(double deadzone, double curvePower) {
        this.deadzone = deadzone;
        this.curvePower = curvePower;
    }

    public double process(double input) {
        // 1. deadzone
        double value = MathUtils.deadzone(input, deadzone);

        // 2. 曲线（更细腻）
        value = Math.pow(value, curvePower);

        return value;
    }
}