package frc.megiddolions.lib.control.drivetrain;

import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.megiddolions.lib.control.FeedForward;
import frc.megiddolions.lib.control.PID;

public interface RamseteGen_DT extends Subsystem, DriveTrain, Auto_DT{

    DifferentialDriveKinematics getKinematics();
    FeedForward getFeedForwardConstants();
    PID getVelocityPID();
}