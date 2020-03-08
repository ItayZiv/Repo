package frc.megiddolions.lib.hardware.power;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class TalonMotorCurrentSensor implements CurrentSensor {
    private PDPCurrentSensor PDPSensor;
    private TalonCurrentSensor TalonSensor;

    public TalonMotorCurrentSensor(WPI_TalonSRX talon, int PDPport) {
        PDPSensor = new PDPCurrentSensor(PDPport);
        TalonSensor = new TalonCurrentSensor(talon);
    }

    @Override
    public double getCurrent() {
        return (PDPSensor.getCurrent() + TalonSensor.getCurrent()) / 2;
    }
}
