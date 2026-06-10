package input;

import utils.MathUtils;
// This class processes input values by applying a deadzone and a curve transformation.
public class InputProcessor {

    private final double deadzone;
    private final double curvePower;

    public InputProcessor(double deadzone, double curvePower) {
        if (deadzone < 0.0 || deadzone >= 1.0) {
            throw new IllegalArgumentException("deadzone must be in [0.0, 1.0)");
        }
        if (curvePower <= 0.0) {
            throw new IllegalArgumentException("curvePower must be positive");
        }
        this.deadzone = deadzone;
        this.curvePower = curvePower;
    }

    public double process(double input) {
        // 1. deadzone
        double value = MathUtils.deadzone(MathUtils.clamp(input, -1.0, 1.0), deadzone);

        // 2. 曲线（更细腻）
        value = Math.signum(value) * Math.pow(Math.abs(value), curvePower);

        return MathUtils.clamp(value, -1.0, 1.0);
    }
}
