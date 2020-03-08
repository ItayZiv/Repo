package frc.megiddolions.lib.hardware.servo;

import edu.wpi.first.wpilibj.PWM;

public class SmartRobotServo extends PWM {
    public static double kMaxSRSPulseMS = 2.5;
    public static double kMinSRSPulseMS = 0.5;

    public SmartRobotServo(int port) {
        super(port);
        setBounds(kMaxSRSPulseMS, 0, 0, 0, kMinSRSPulseMS);
    }
}
