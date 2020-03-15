package frc.megiddolions.lib.control.drivetrain;

@FunctionalInterface
public interface MecanumConsumer {
    void drive(double frontLeft, double rearLeft, double frontRight, double rearRight);
}
