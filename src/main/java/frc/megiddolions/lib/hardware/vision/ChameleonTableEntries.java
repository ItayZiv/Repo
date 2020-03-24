package frc.megiddolions.lib.hardware.vision;

import edu.wpi.first.networktables.NetworkTable;
import frc.megiddolions.lib.annotations.NeedsTesting;

public class ChameleonTableEntries extends CameraTableEntries {
    private static final String kNTPoseKey = "targetPose";
    private static final String kNTHorizontalKey = "targetYaw";
    private static final String kNTVerticalKey = "targetPitch";

    @NeedsTesting
    public ChameleonTableEntries(NetworkTable table) {
        super(table, "", kNTPoseKey, kNTVerticalKey, kNTHorizontalKey);
    }
}
