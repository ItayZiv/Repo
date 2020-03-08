package frc.megiddolions.lib.control.drivetrain;

public interface DriveTrain {

    void tankDrive(double left, double right);
    void arcadeDrive(double xSpeed, double zRotation);
    void tankDriveVolts(double leftVolts, double rightVolts);
    void stop();
}