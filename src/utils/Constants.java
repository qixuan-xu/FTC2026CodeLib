package utils;

/**
 * FTC2026 Robot Constants
 *
 * Centralized configuration for all robot parameters.
 * All hardware measurements, PID tuning values, and timing constants are defined here.
 *
 * Benefits of using a Constants class:
 * - Easy tuning: Change values in one place, affects all code
 * - Clear organization: All config grouped by subsystem
 * - Competition safe: Can be compiled and frozen before match
 * - Reusability: Same values used across different OpModes
 *
 * To adjust for your robot:
 * 1. Measure physical dimensions and update ROBOT_*, TRACK_WIDTH, etc.
 * 2. Tune PID values (DRIVE_KP, KI, KD) through testing
 * 3. Calibrate sensors (POTENTIOMETER_*, TICKS_TO_INCH)
 * 4. Adjust DEADZONE and CURVE_POWER for driver preference
 *
 * IMPORTANT: Always test changes on test robot before competition!
 */
public final class Constants {

    // 防止被实例化 (Prevent instantiation)
    private Constants() {}

    // ========== PID Control ==========
    // 驱动电机PID系数（用于移动控制）
    // Tune these values for your robot's movement response
    public static final double DRIVE_KP = 0.8;        // Proportional gain (larger = faster response)
    public static final double DRIVE_KI = 0.0;        // Integral gain (corrects steady-state error)
    public static final double DRIVE_KD = 0.1;        // Derivative gain (dampens oscillation)

    // PID计算的输出限制（防止积分饱和）
    // Output limits prevent motor commands from exceeding [-1.0, 1.0]
    public static final double PID_MIN_OUTPUT = -1.0;
    public static final double PID_MAX_OUTPUT = 1.0;
    public static final double PID_MIN_INTEGRAL = -1.0;   // 防止积分风速（windup）
    public static final double PID_MAX_INTEGRAL = 1.0;

    // ========== Odometry (里程计) ==========
    // 三轮里程计参数，用于机器人位置追踪
    public static final double TRACK_WIDTH = 14.2;       // 平行轮间距 (inch)
    public static final double LATERAL_OFFSET = -6.5;    // 侧向轮相对中心的偏移 (inch)
    public static final double ROBOT_RADIUS = 7.1;       // 机器人半径 (inch) = TRACK_WIDTH/2

    // ========== Encoder (编码器) ==========
    // 轮子编码器参数，计算实际距离
    public static final double TICKS_PER_REVOLUTION = 537.7;  // REV HD Hex电机规格
    public static final double WHEEL_DIAMETER = 3.77953;      // 轮子直径 (inch)
    public static final double TICKS_TO_INCH = (Math.PI * WHEEL_DIAMETER) / TICKS_PER_REVOLUTION;

    // ========== Input Processor (输入处理) ==========
    // 手柄死区和输入曲线参数
    public static final double DEADZONE = 0.08;        // 摇杆死区（低于此值认为0）
    public static final double CURVE_POWER = 2.0;      // 输入曲线幂次（提高灵敏度）

    // ========== Motor Control (电机控制) ==========
    // 电机功率范围
    public static final double MAX_MOTOR_POWER = 1.0;
    public static final double MIN_MOTOR_POWER = -1.0;

    // ========== Movement Timing (移动时序) ==========
    // OpMode循环时间和加速度参数
    public static final long UPDATE_INTERVAL_MS = 10;    // 每个控制周期10毫秒
    public static final double MAX_ACCELERATION = 0.05;  // 每个周期最多改变5%功率

    // ========== Robot Dimensions (机器人尺寸) ==========
    // 根据实际机器人更新这些值
    public static final double ROBOT_LENGTH = 18.0;      // 机器人长度 (inch)
    public static final double ROBOT_WIDTH = 16.5;       // 机器人宽度 (inch)

    // ========== Potentiometer (电位器) ==========
    // 线性电位器参数，用于测量物理伸展距离
    public static final double POTENTIOMETER_MAX_DISTANCE = 24.0;    // 最大伸展距离 (inch)
    public static final double POTENTIOMETER_DEFAULT_OFFSET = 0.0;   // 默认零点偏移 (inch)
}
