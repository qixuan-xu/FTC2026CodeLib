package utils;

public class MathUtils {
    // Clamps a value between a minimum and maximum range. If the value is less
    // than the minimum, it returns the minimum. If the value is greater than
    // the maximum, it returns the maximum. Otherwise, it returns the original value.
    public static double clamp(double value, double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("min cannot be greater than max");
        }
        return Math.max(min, Math.min(max, value));
    }

    // Clamps a value to a maximum absolute value. If the absolute value of the input
    // is greater than the specified maximum, it returns the maximum with the same sign
    // as the input. Otherwise, it returns the original value.
    public static double clampAbs(double value, double max) {
        if (max < 0) {
            throw new IllegalArgumentException("max must be positive");
        }
        return Math.max(-max, Math.min(max, value));
    }

    // Applies a deadzone to the input value. If the absolute value of the input
    // is less than or equal to the threshold, it returns 0. Otherwise, it returns
    // the original value.
    public static double deadzone(double value, double threshold) {
        if (threshold < 0.0 || threshold >= 1.0) {
            throw new IllegalArgumentException("threshold must be in [0.0, 1.0)");
        }
        if (Math.abs(value) <= threshold) return 0.0;
        double sign = Math.signum(value);
        return sign * (Math.abs(value) - threshold) / (1 - threshold);
    }

    // Checks if two double values are approximately equal within a specified epsilon.
    // It returns true if the absolute difference between the two values is less than the epsilon,
    // indicating that they are close enough to be considered equal, and false otherwise.
    public static boolean epsilonEquals(double a, double b, double epsilon) {
        return Math.abs(a - b) < epsilon;
    }

    // Normalizes two values by finding the maximum absolute value among them and returning
    // it if it's greater than 1.0. If the maximum absolute value is less than or equal to
    // 1.0, it returns 1.0. This is useful for scaling values to ensure they do not exceed
    // a certain range, such as when controlling motor outputs in robotics, where you want
    // to maintain the ratio between the values while ensuring they do not exceed the maximum
    // allowed value.
    public static double normalize(double a, double b) {
        double max = Math.max(Math.abs(a), Math.abs(b));
        return max > 1.0 ? max : 1.0;
    }

    // Normalizes four values by finding the maximum absolute value among them and returning
    // it if it's greater than 1.0. If the maximum absolute value is less than or equal to 1.0,
    // it returns 1.0. This is useful for scaling values to ensure they do not exceed a certain
    // range, such as when controlling motor outputs in robotics, where you want to maintain the
    // ratio between the values while ensuring they do not exceed the maximum allowed value.
    public static double normalize(double a, double b, double c, double d) {
        double max = Math.max(
                Math.max(Math.abs(a), Math.abs(b)),
                Math.max(Math.abs(c), Math.abs(d))
        );
        return max > 1.0 ? max : 1.0;
    }


    // Normalize the angle to the range [-180, 180). This method takes an angle in degrees and
    // normalizes it to ensure that it falls within the specified range. It does this by first
    // taking the modulus of the angle with 360 to wrap it around, and then adjusting it if it
    // exceeds 180 or is less than or equal to -180. This is useful for ensuring that angles are
    // represented in a consistent way, especially when dealing with rotations in robotics or
    // other applications where angles can wrap around.
    public static double normalizeAngle(double angle) {
          angle = angle % 360;
          if (angle > 180) angle -= 360;
          if (angle <= -180) angle += 360;
          return angle;
    }

    // Calculates the distance between two points (x1, y1) and (x2, y2) using the
    // Euclidean distance formula.
    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }
}
