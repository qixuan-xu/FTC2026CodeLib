package test;

import Command.CommandScheduler;
import Command.MoveDistanceCommand;
import Command.MoveSpeedTimeCommand;
import Command.MoveToPositionCommand;
import control.PID;
import control.TeleOpController;
import input.InputProcessor;
import sensor.Odometry;
import sensor.Potentiometer;
import utils.MathUtils;

public class LogicTests {
    private static final double EPSILON = 1e-9;

    public static void main(String[] args) {
        testInputProcessorPreservesSign();
        testMoveDirectionMapping();
        testMoveToPositionUsesShortestHeadingError();
        testOdometryAxes();
        testPotentiometerCalibration();
        testPidHandlesZeroDt();
        testSchedulerRejectsNullCommands();

        System.out.println("LogicTests passed");
    }

    private static void testInputProcessorPreservesSign() {
        InputProcessor squared = new InputProcessor(0.0, 2.0);
        assertClose(-0.25, squared.process(-0.5), "negative inputs should stay negative");
        assertClose(0.25, squared.process(0.5), "positive inputs should stay positive");

        InputProcessor fractional = new InputProcessor(0.0, 1.5);
        assertTrue(Double.isFinite(fractional.process(-0.25)), "fractional curves should not produce NaN");
        assertTrue(fractional.process(-0.25) < 0.0, "fractional negative input should stay negative");
    }

    private static void testMoveDirectionMapping() {
        CapturingController controller = new CapturingController();
        MoveSpeedTimeCommand forward = new MoveSpeedTimeCommand(controller, 0.5, 1.0, 0.0, 0.0);
        forward.initialize();
        forward.execute();
        assertClose(0.5, controller.forward, "0 degrees should move forward");
        assertClose(0.0, controller.strafe, "0 degrees should not strafe");

        MoveSpeedTimeCommand right = new MoveSpeedTimeCommand(controller, 0.5, 1.0, 90.0, 0.0);
        right.initialize();
        right.execute();
        assertClose(0.0, controller.forward, "90 degrees should not move forward");
        assertClose(0.5, controller.strafe, "90 degrees should strafe right");

        Potentiometer distance = new Potentiometer(10.0);
        MoveDistanceCommand distanceForward = new MoveDistanceCommand(controller, distance, 1.0, 0.0, 0.0, 0);
        distanceForward.initialize();
        distanceForward.execute();
        assertTrue(controller.forward > 0.0, "distance command at 0 degrees should command forward motion");
        assertClose(0.0, controller.strafe, "distance command at 0 degrees should not strafe");
    }

    private static void testMoveToPositionUsesShortestHeadingError() {
        CapturingController controller = new CapturingController();
        Odometry odometry = new Odometry(new Potentiometer(10.0), new Potentiometer(10.0), new Potentiometer(10.0));
        odometry.setPosition(0.0, 0.0, Math.toRadians(170.0));

        MoveToPositionCommand command = new MoveToPositionCommand(controller, odometry, 0.0, 0.0, -170.0, 0);
        command.initialize();
        command.execute();

        assertTrue(controller.turn > 0.0, "heading from 170 to -170 should turn through +20 degrees");
    }

    private static void testOdometryAxes() {
        Potentiometer left = new Potentiometer(10.0);
        Potentiometer right = new Potentiometer(10.0);
        Potentiometer back = new Potentiometer(10.0);
        Odometry forwardOdometry = new Odometry(left, right, back);
        left.setPosition(0.1);
        right.setPosition(0.1);
        forwardOdometry.updatePosition();
        assertClose(1.0, forwardOdometry.getX(), "parallel wheels should update forward X");
        assertClose(0.0, forwardOdometry.getY(), "parallel wheels should not update strafe Y");

        Potentiometer left2 = new Potentiometer(10.0);
        Potentiometer right2 = new Potentiometer(10.0);
        Potentiometer back2 = new Potentiometer(10.0);
        Odometry strafeOdometry = new Odometry(left2, right2, back2);
        back2.setPosition(0.1);
        strafeOdometry.updatePosition();
        assertClose(0.0, strafeOdometry.getX(), "back wheel should not update forward X");
        assertClose(1.0, strafeOdometry.getY(), "back wheel should update strafe Y");
    }

    private static void testPotentiometerCalibration() {
        Potentiometer potentiometer = new Potentiometer(10.0, 2.0);
        potentiometer.setPosition(0.5);

        potentiometer.calibrateZero();
        assertClose(0.0, potentiometer.getDistance(), "calibration should zero current distance");

        potentiometer.calibrateZero();
        assertClose(0.0, potentiometer.getDistance(), "repeated calibration should stay zero");
    }

    private static void testPidHandlesZeroDt() {
        PID pid = new PID();
        double output = pid.update(1.0, 0.0, 0.0);
        assertTrue(Double.isFinite(output), "PID output should remain finite when dt is zero");
    }

    private static void testSchedulerRejectsNullCommands() {
        boolean thrown = false;
        try {
            new CommandScheduler().schedule(null);
        } catch (NullPointerException expected) {
            thrown = true;
        }
        assertTrue(thrown, "scheduler should reject null commands explicitly");
    }

    private static void assertClose(double expected, double actual, String message) {
        if (Math.abs(expected - actual) > EPSILON) {
            throw new AssertionError(message + " expected=" + expected + " actual=" + actual);
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static class CapturingController extends TeleOpController {
        double forward;
        double strafe;
        double turn;

        CapturingController() {
            super(0.0, 1.0);
        }

        @Override
        public double[] update(double forwardInput, double strafeInput, double turnInput) {
            this.forward = MathUtils.clamp(forwardInput, -1.0, 1.0);
            this.strafe = MathUtils.clamp(strafeInput, -1.0, 1.0);
            this.turn = MathUtils.clamp(turnInput, -1.0, 1.0);
            return super.update(forwardInput, strafeInput, turnInput);
        }
    }
}
