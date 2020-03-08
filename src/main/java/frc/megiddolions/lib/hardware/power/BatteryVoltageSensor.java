package frc.megiddolions.lib.hardware.power;

import edu.wpi.first.wpilibj.RobotController;

public class BatteryVoltageSensor implements VoltageSensor {

    @Override
    public double getVoltage() {
        return RobotController.getBatteryVoltage();
    }
}
