package frc.megiddolions.lib.control.drivetrain;

@FunctionalInterface
public interface DriveTrainConsumer {
    void drive(double left, double right);
}
