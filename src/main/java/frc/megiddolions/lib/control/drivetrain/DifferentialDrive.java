package frc.megiddolions.lib.control.drivetrain;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpiutil.math.MathUtil;

public class DifferentialDrive implements DriveTrain{

    private final DriveTrainConsumer consumer;

    /**
     * Creates a DifferentialDrive object with the given output consumer
     *
     * @param consumer The consumer to drive the robot with
     */
    public DifferentialDrive(DriveTrainConsumer consumer) {
        this.consumer = consumer;
    }

    /**
     * Outputs a left and right power to each side of the consumer
     *
     * @param leftPower the power to give the left side along the X axis [-1.0..1.0]. Forward is positive.
     * @param rightPower the power to give the right side along the X axis [-1.0..1.0]. Forward is positive.
     */
    public void tankDrive(double leftPower, double rightPower) {
        consumer.drive(leftPower, rightPower);
    }

    /**
     * Outputs a left and right power to each side of the consumer, based on a voltage.
     * To achieve proper behaviour a output of 1 is the battery voltage
     *
     * @param leftVolts the voltage to give the left side along the X. The range is the battery voltage. Forward is positive.
     * @param rightVolts the voltage to give the right side along the X. The range is the battery voltage. Forward is positive.
     */
    public void tankDriveVolts(double leftVolts, double rightVolts) {
        consumer.drive(leftVolts / RobotController.getBatteryVoltage(), rightVolts / RobotController.getBatteryVoltage());
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

        consumer.drive(MathUtil.clamp(leftMotorOutput, -1.0, 1.0),
                MathUtil.clamp(rightMotorOutput, -1.0, 1.0));
    }


    /**
     * Stops the robot
     */
    @Override
    public void stop() {
        consumer.drive(0, 0);
    }
}
