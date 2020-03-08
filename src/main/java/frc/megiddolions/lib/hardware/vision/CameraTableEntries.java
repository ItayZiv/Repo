package frc.megiddolions.lib.hardware.vision;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;

public class CameraTableEntries {
    public NetworkTableEntry hasTarget;
    public NetworkTableEntry pose;
    public NetworkTableEntry vertical;
    public NetworkTableEntry horizontal;

    public CameraTableEntries(NetworkTable table, String hasTarget, String pose, String vertical, String horizontal) {
        this.hasTarget = table.getEntry(hasTarget);
        this.pose = table.getEntry(pose);
        this.vertical = table.getEntry(vertical);
        this.horizontal = table.getEntry(horizontal);
    }
}
