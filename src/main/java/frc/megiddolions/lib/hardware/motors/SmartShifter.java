package frc.megiddolions.lib.hardware.motors;

import edu.wpi.first.wpilibj.LinearFilter;

public class SmartShifter extends Shifter {
    private final double kMaxVelocityLowGear;
    private LinearFilter speedFilter = LinearFilter.movingAverage(15);

    public SmartShifter(int port, double gearRatioPower, double gearRatioSpeed, double maxVelocityLowGear) {
        this(port, gearRatioPower, gearRatioSpeed, 0, maxVelocityLowGear);
    }

    public SmartShifter(int port, double gearRatioPower, double gearRatioSpeed, double unitsPerOutputRevolution,
                        double maxVelocityLowGear) {
        super(port, gearRatioPower, gearRatioSpeed, unitsPerOutputRevolution);
        this.kMaxVelocityLowGear = maxVelocityLowGear;
    }

    public void update(double velocity) {
        double filteredVelocity = speedFilter.calculate(Math.abs(velocity));

        if (filteredVelocity >= kMaxVelocityLowGear)
            setState(ShifterState.Speed);
        else
            setState(ShifterState.Power);
    }
}
