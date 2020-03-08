package frc.megiddolions.lib.control.trajectories;

public class DriveTrainConstants {
    public final int ksVolts;
    public final int kvVoltsSecondsPerMeter;
    public final int kaVoltsSecondsSquaredPerMeter;

    public final int kPDriveVel;

    public DriveTrainConstants(int sVolts, int vVoltsSecondsPerMeter, int aVoltsSecondsSquaredPerMeter, int PDriveVel) {
        ksVolts = sVolts;
        kvVoltsSecondsPerMeter = vVoltsSecondsPerMeter;
        kaVoltsSecondsSquaredPerMeter = aVoltsSecondsSquaredPerMeter;
        kPDriveVel = PDriveVel;
    }
}
