// Drives the robot using mecanum wheels. The math is based on the standard
// mecanum wheel equations, which are derived from the geometry of the wheels
// and their placement on the robot. The equations take into account the desired
// forward, strafe, and turn movements to calculate the power for each wheel.
// The resulting wheel powers are then normalized to ensure that they do not
// exceed the maximum allowed power, which is typically 1.0 for motor controllers.
// This allows for smooth and efficient control of the robot's movement in any direction.
package hardware;

import utils.MathUtils;

public class DriveMath {

    public static double[] mecanum(double forward, double strafe, double turn) {
        double frontLeft = forward + strafe + turn;
        double frontRight = forward - strafe - turn;
        double backLeft = forward - strafe + turn;
        double backRight = forward + strafe - turn;

        double max = MathUtils.normalize(frontLeft, frontRight, backLeft, backRight);

        frontLeft /= max;
        frontRight /= max;
        backLeft /= max;
        backRight /= max;

        return new double[]{frontLeft, frontRight, backLeft, backRight};
    }
}