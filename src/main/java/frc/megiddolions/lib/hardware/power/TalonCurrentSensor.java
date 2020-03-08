package frc.megiddolions.lib.hardware.power;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class TalonCurrentSensor implements CurrentSensor {
    private WPI_TalonSRX talon;

    public TalonCurrentSensor(WPI_TalonSRX talon) {
        this.talon = talon;
    }

    @Override
    public double getCurrent() {
        return talon.getStatorCurrent();
    }
}
