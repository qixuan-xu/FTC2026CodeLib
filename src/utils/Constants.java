package utils;

public final class Constants {

    // 防止被实例化
    private Constants() {}

    // ========== PID Control ==========
    public static final double DRIVE_KP = 0.8;
    public static final double DRIVE_KI = 0.0;
    public static final double DRIVE_KD = 0.1;

    // PID output limits
    public static final double PID_MIN_OUTPUT = -1.0;
    public static final double PID_MAX_OUTPUT = 1.0;
    public static final double PID_MIN_INTEGRAL = -1.0;
    public static final double PID_MAX_INTEGRAL = 1.0;

    // ========== Odometry ==========
    public static final double TRACK_WIDTH = 14.2;       // inch (distance between left and right wheels)
    public static final double LATERAL_OFFSET = -6.5;    // inch (distance from center to odometry wheel)
    public static final double ROBOT_RADIUS = 7.1;       // inch (half of track width)

    // ========== Encoder ==========
    public static final double TICKS_PER_REVOLUTION = 537.7;  // REV HD Hex Motor
    public static final double WHEEL_DIAMETER = 3.77953;      // inch
    public static final double TICKS_TO_INCH = (Math.PI * WHEEL_DIAMETER) / TICKS_PER_REVOLUTION;

    // ========== Input Processor ==========
    public static final double DEADZONE = 0.08;
    public static final double CURVE_POWER = 2.0;

    // ========== Motor Control ==========
    public static final double MAX_MOTOR_POWER = 1.0;
    public static final double MIN_MOTOR_POWER = -1.0;

    // ========== Movement Timing ==========
    public static final long UPDATE_INTERVAL_MS = 10;    // 10ms per cycle
    public static final double MAX_ACCELERATION = 0.05;  // motor power change per update (10ms)

    // ========== Robot Dimensions ==========
    // These are example values, adjust based on your robot
    public static final double ROBOT_LENGTH = 18.0;      // inch
    public static final double ROBOT_WIDTH = 16.5;       // inch

    // ========== Potentiometer ==========
    public static final double POTENTIOMETER_MAX_DISTANCE = 24.0;    // inch (maximum extension distance)
    public static final double POTENTIOMETER_DEFAULT_OFFSET = 0.0;   // inch (default zero offset)
}
