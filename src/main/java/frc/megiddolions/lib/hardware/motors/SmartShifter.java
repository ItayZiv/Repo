package frc.megiddolions.lib.hardware.motors;

public class SmartShifter extends Shifter {

    public SmartShifter(int port, double gearRatioPower, double gearRatioSpeed) {
        super(port, gearRatioPower, gearRatioSpeed);
    }

    public SmartShifter(int port, double gearRatioPower, double gearRatioSpeed, double unitsPerOutputRevolution) {
        super(port, gearRatioPower, gearRatioSpeed, unitsPerOutputRevolution);
    }
}
