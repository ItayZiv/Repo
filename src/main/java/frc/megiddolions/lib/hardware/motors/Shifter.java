package frc.megiddolions.lib.hardware.motors;

import edu.wpi.first.wpilibj.Solenoid;

public class Shifter extends Solenoid {
    private final double kLowGearRatio;
    private final double kHighGearRatio;
    private final double kUnitsPerOutputRevolution;

    public Shifter(int port, double lowGearRatio, double highGearRatio) {
        this(port, lowGearRatio, highGearRatio, 0);
    }

    public Shifter(int port, double lowGearRatio, double highGearRatio, double unitsPerOutputRevolution) {
        super(port);
        this.kLowGearRatio = lowGearRatio;
        this.kHighGearRatio = highGearRatio;
        this.kUnitsPerOutputRevolution = unitsPerOutputRevolution;
    }

    public void setState(ShifterState state) {
        this.set(state.value);
    }

    public ShifterState getState() {
        return ShifterState.getState(this.get());
    }

    public double getGearRatio() {
        return getGearRatio(getState());
    }

    public double getGearRatio(ShifterState state) {
        switch (state) {
            case Low:
                return kLowGearRatio;
            case High:
                return kHighGearRatio;
            default:
                return getGearRatio(ShifterState.Low);
        }
    }

    public double outputUnitsPerInputRevolution() {
        return outputUnitsPerInputRevolution(kUnitsPerOutputRevolution);
    }

    public double outputUnitsPerInputRevolution(double unitsPerOutputRevolution) {
        return unitsPerOutputRevolution * getGearRatio();
    }

    public double outputUnitsPerSecondPerInputRevolutionsPerMinute() {
        return outputUnitsPerSecondPerInputRevolutionsPerMinute(kUnitsPerOutputRevolution);
    }

    public double outputUnitsPerSecondPerInputRevolutionsPerMinute(double unitsPerOutputRevolution) {
        return outputUnitsPerInputRevolution(unitsPerOutputRevolution) * (1.0 / 60);
    }

    public enum ShifterState {
        High(false),
        Low(true),
        Unknown(false);

        public final boolean value;

        ShifterState(boolean value) {
            this.value = value;
        }

        public ShifterState swap() {
            return getState(!this.value);
        }

        public static ShifterState getState(boolean value) {
            return (value) ? Low : High;
        }
    }
}
