package frc.megiddolions.subsystems;


import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.megiddolions.Constants.VisionConstants;
import frc.megiddolions.lib.hardware.vision.ChameleonCamera;
import frc.megiddolions.lib.util;

public class VisionSubsystem extends SubsystemBase {
    private final ChameleonCamera camera;

    public VisionSubsystem() {
        CameraServer.getInstance().startAutomaticCapture().setResolution(640, 480);
        camera = new ChameleonCamera(VisionConstants.kCameraName);
    }

    public double getHorizontalTargetError() {
        return camera.getHorizontalOffset();
    }

    public double getCorrectedHorizontalError() {
        double shooterTargetDistance = Math.hypot(getEstimatedDistance(), VisionConstants.kCameraOffset);
        return Math.asin((getEstimatedDistance() * Math.sin(180 - getHorizontalTargetError())) / shooterTargetDistance) % 360;
    }

    public double getCorrectedInnerPortHorizontalError(Rotation2d robotHeading) {
        Translation2d targetTranslation = estimateTargetLocation(robotHeading).getTranslation();
        double innerPortDistance = Math.hypot(targetTranslation.getX(), targetTranslation.getY() + VisionConstants.kPortDistMeters);
        return getCorrectedHorizontalError() - Math.asin((VisionConstants.kPortDistMeters * Math.sin(
                Math.asin(targetTranslation.getY() / getEstimatedDistance()) % 360 + 90)) / innerPortDistance) % 360;
    }

    public double getWantedHorizontalError(Rotation2d robotHeading) {
        if (util.checkTolerance(getCorrectedHorizontalError(), VisionConstants.kMaxInnerPortAngle))
            return getCorrectedInnerPortHorizontalError(robotHeading);
        else
            return getCorrectedHorizontalError();
    }

    public double getVerticalTargetError() {
        return camera.getVerticalOffset();
    }

    public double getEstimatedDistance() {
        return VisionConstants.kPitchToDistance.convert(getVerticalTargetError());
    }

    public Pose2d estimateTargetLocation(Rotation2d robotHeading) {
        Rotation2d rotation = robotHeading.plus(Rotation2d.fromDegrees(getHorizontalTargetError()));
        Translation2d translation = new Translation2d(getEstimatedDistance() * Math.sin(rotation.getDegrees()), 0);
        translation = new Translation2d(translation.getX(), Math.sqrt(Math.pow(getEstimatedDistance(), 2) - Math.pow(translation.getX(), 2)));

        return new Pose2d(translation, rotation);
    }
}

