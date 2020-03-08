package frc.megiddolions.lib.hardware.vision;

import edu.wpi.first.wpilibj.geometry.Pose2d;

public interface VisionCamera {

    boolean hasTarget();

    Pose2d getPose();

    double getVerticalOffset();

    double getHorizontalOffset();
}
