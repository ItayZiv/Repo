package frc.megiddolions.lib.hardware.vision;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Transform2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import frc.megiddolions.lib.annotations.NeedsTesting;
import frc.megiddolions.lib.annotations.Tested;
import frc.megiddolions.lib.annotations.UsedInCompetition;
import frc.megiddolions.lib.util;

@Tested
@UsedInCompetition(competition = "District #1", year = "2020")
public class ChameleonCamera implements VisionCamera{
    private static final String kNTVisionTable = "chameleon-vision";
    private NetworkTable cameraTable;
    private ChameleonTableEntries cameraEntries;

    public ChameleonCamera(String cameraName) {
        cameraTable = NetworkTableInstance.getDefault().getTable(kNTVisionTable).getSubTable(cameraName);
        cameraEntries = new ChameleonTableEntries(cameraTable);
    }

    @Override
    public boolean hasTarget() {
        return cameraEntries.hasTarget.getBoolean(false);
    }

    @Override
    public Pose2d getPose() {
        double[] chameleonPose = cameraEntries.pose.getDoubleArray((double[]) null);
        if (chameleonPose == null || chameleonPose.length == 0) return new Pose2d();

        return util.fromChameleonPose(chameleonPose);
    }

    public double getRawDistance() {
        Pose2d pose = getPose();
        return Math.sqrt( Math.pow(pose.getTranslation().getX(), 2) + Math.pow(pose.getTranslation().getY(), 2));
    }

    @Override
    public double getHorizontalOffset() {
        return cameraEntries.horizontal.getDouble(0);
    }

    @Override
    public double getVerticalOffset() {
        return cameraEntries.vertical.getDouble(0);
    }
}
