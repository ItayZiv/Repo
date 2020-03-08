package frc.megiddolions.lib.control.drivetrain;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpiutil.math.MathUtil;

public class DifferentialDrive implements DriveTrain{

    private final DriveTrainConsumer consumer;

    public DifferentialDrive(DriveTrainConsumer consumer) {
        this.consumer = consumer;
    }

    public void tankDrive(double leftPower, double rightPower) {
        consumer.drive(leftPower, rightPower);
    }

    public void tankDriveVolts(double leftVolts, double rightVolts) {
        consumer.drive(leftVolts / RobotController.getBatteryVoltage(), rightVolts / RobotController.getBatteryVoltage());
    }

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

    @Override
    public void stop() {
        consumer.drive(0, 0);
    }
}
