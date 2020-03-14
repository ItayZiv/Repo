package frc.megiddolions.lib.hardware.motors;

import edu.wpi.first.wpilibj.LinearFilter;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;

public class SmartShifter extends Shifter {
    private final double kMaxVelocityLowGear;
    private final double kTurningThreshold;
    private LinearFilter velocityFilter = LinearFilter.movingAverage(15);

    public SmartShifter(int port, double lowGearRatio, double highGearRatio, double maxVelocityLowGear) {
        this(port, lowGearRatio, highGearRatio, 0, maxVelocityLowGear, 1);
    }

    public SmartShifter(int port, double lowGearRatio, double highGearRatio, double unitsPerOutputRevolution,
                        double maxVelocityLowGear, double turningThreshold) {
        super(port, lowGearRatio, highGearRatio, unitsPerOutputRevolution);
        this.kMaxVelocityLowGear = maxVelocityLowGear;
        this.kTurningThreshold = turningThreshold;
    }

    public void update(DifferentialDriveWheelSpeeds velocity) {
        double filteredVelocity = velocityFilter.calculate(Math.min(Math.abs(velocity.leftMetersPerSecond),
                Math.abs(velocity.rightMetersPerSecond)));
        double turningDifference = Math.abs(velocity.leftMetersPerSecond - velocity.rightMetersPerSecond);

        if (filteredVelocity >= kMaxVelocityLowGear && kTurningThreshold <= turningDifference)
            setState(ShifterState.High);
        else
            setState(ShifterState.Low);
    }
}
