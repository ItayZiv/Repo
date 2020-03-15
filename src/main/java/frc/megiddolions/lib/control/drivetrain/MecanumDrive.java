package frc.megiddolions.lib.control.drivetrain;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.drive.RobotDriveBase;
import edu.wpi.first.wpilibj.drive.Vector2d;
import edu.wpi.first.wpiutil.math.MathUtil;

public class MecanumDrive implements DriveTrain {
    private final MecanumConsumer consumer;

    public MecanumDrive(MecanumConsumer consumer) {
        this.consumer = consumer;
    }

    /**
     * Drive method for Mecanum platform.
     *
     * <p>Angles are measured clockwise from the positive X axis. The robot's speed is independent
     * from its angle or rotation rate.
     *
     * @param ySpeed    The robot's speed along the Y axis [-1.0..1.0]. Right is positive.
     * @param xSpeed    The robot's speed along the X axis [-1.0..1.0]. Forward is positive.
     * @param zRotation The robot's rotation rate around the Z axis [-1.0..1.0]. Clockwise is
     *                  positive.
     */
    @SuppressWarnings("ParameterName")
    public void driveCartesian(double ySpeed, double xSpeed, double zRotation) {
        driveCartesian(ySpeed, xSpeed, zRotation, 0.0);
    }

    /**
     * Drive method for Mecanum platform.
     *
     * <p>Angles are measured clockwise from the positive X axis. The robot's speed is independent
     * from its angle or rotation rate.
     *
     * @param ySpeed    The robot's speed along the Y axis [-1.0..1.0]. Right is positive.
     * @param xSpeed    The robot's speed along the X axis [-1.0..1.0]. Forward is positive.
     * @param zRotation The robot's rotation rate around the Z axis [-1.0..1.0]. Clockwise is
     *                  positive.
     * @param gyroAngle The current angle reading from the gyro in degrees around the Z axis. Use
     *                  this to implement field-oriented controls.
     */
    @SuppressWarnings("ParameterName")
    public void driveCartesian(double ySpeed, double xSpeed, double zRotation, double gyroAngle) {
        ySpeed = MathUtil.clamp(ySpeed, -1.0, 1.0);
        xSpeed = MathUtil.clamp(xSpeed, -1.0, 1.0);

        // Compensate for gyro angle.
        Vector2d input = new Vector2d(ySpeed, xSpeed);
        input.rotate(-gyroAngle);

        double[] wheelSpeeds = new double[4];
        wheelSpeeds[RobotDriveBase.MotorType.kFrontLeft.value] = input.x + input.y + zRotation;
        wheelSpeeds[RobotDriveBase.MotorType.kFrontRight.value] = -input.x + input.y - zRotation;
        wheelSpeeds[RobotDriveBase.MotorType.kRearLeft.value] = -input.x + input.y + zRotation;
        wheelSpeeds[RobotDriveBase.MotorType.kRearRight.value] = input.x + input.y - zRotation;

        consumer.drive(wheelSpeeds[RobotDriveBase.MotorType.kFrontLeft.value],
                wheelSpeeds[RobotDriveBase.MotorType.kRearLeft.value],
                wheelSpeeds[RobotDriveBase.MotorType.kFrontRight.value],
                wheelSpeeds[RobotDriveBase.MotorType.kRearRight.value]);
    }


    /**
     * Drive method for Mecanum platform.
     *
     * <p>Angles are measured counter-clockwise from straight ahead. The speed at which the robot
     * drives (translation) is independent from its angle or rotation rate.
     *
     * @param magnitude The robot's speed at a given angle [-1.0..1.0]. Forward is positive.
     * @param angle     The angle around the Z axis at which the robot drives in degrees [-180..180].
     * @param zRotation The robot's rotation rate around the Z axis [-1.0..1.0]. Clockwise is
     *                  positive.
     */
    @SuppressWarnings("ParameterName")
    public void drivePolar(double magnitude, double angle, double zRotation) {
        driveCartesian(magnitude * Math.sin(angle * (Math.PI / 180.0)),
                magnitude * Math.cos(angle * (Math.PI / 180.0)), zRotation, 0.0);
    }

    /**
     * Outputs a left and right power to each side of the consumer
     *
     * @param leftPower the power to give the left side along the X axis [-1.0..1.0]. Forward is positive.
     * @param rightPower the power to give the right side along the X axis [-1.0..1.0]. Forward is positive.
     */
    public void tankDrive(double leftPower, double rightPower) {
        consumer.drive(leftPower,leftPower, rightPower, rightPower);
    }

    /**
     * Outputs a left and right power to each side of the consumer, based on a voltage.
     * To achieve proper behaviour a output of 1 is the battery voltage
     *
     * @param leftVolts the voltage to give the left side along the X. The range is the battery voltage. Forward is positive.
     * @param rightVolts the voltage to give the right side along the X. The range is the battery voltage. Forward is positive.
     */
    public void tankDriveVolts(double leftVolts, double rightVolts) {
        double leftPower = leftVolts / RobotController.getBatteryVoltage();
        double rightPower = rightVolts / RobotController.getBatteryVoltage();
        consumer.drive(leftPower, leftPower, rightPower, rightPower);
    }

    /**
     * Outputs a power to each side of the robot based on a given speed and rotation in a arcade drive fashion.
     *
     * @param xSpeed the speed to move on the X axis [-1.0..1.0]. Forward positive
     * @param zRotation the rotation to give around the Z axis [-1.0..1.0]. Clockwise positive
     */
    public void arcadeDrive(double xSpeed, double zRotation) {
        double leftMotorOutput;
        double rightMotorOutput;

        double maxInput = Math.copySign(Math.max(Math.abs(xSpeed), Math.abs(zRotation)), xSpeed);

        if (xSpeed >= 0.0) {
            // First quadrant, else second quadrant
            if (zRotation >= 0.0) {
                leftMotorOutput = maxInput;
                rightMotorOutput = xSpeed - zRotation;
            } else {
                leftMotorOutput = xSpeed + zRotation;
                rightMotorOutput = maxInput;
            }
        } else {
            // Third quadrant, else fourth quadrant
            if (zRotation >= 0.0) {
                leftMotorOutput = xSpeed + zRotation;
                rightMotorOutput = maxInput;
            } else {
                leftMotorOutput = maxInput;
                rightMotorOutput = xSpeed - zRotation;
            }
        }

        leftMotorOutput = MathUtil.clamp(leftMotorOutput, -1.0, 1.0);
        rightMotorOutput = MathUtil.clamp(rightMotorOutput, -1.0, 1.0);
        consumer.drive(leftMotorOutput, leftMotorOutput, rightMotorOutput, rightMotorOutput);
    }


    /**
     * Stops the robot
     */
    @Override
    public void stop() {
        consumer.drive(0, 0, 0, 0);
    }
}
