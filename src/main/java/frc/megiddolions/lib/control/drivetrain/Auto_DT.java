package frc.megiddolions.lib.control.drivetrain;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;

public interface Auto_DT {

    Pose2d getPose();
    DifferentialDriveWheelSpeeds getWheelSpeeds();
}
