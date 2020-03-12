package frc.megiddolions.lib.hardware.motors;

import edu.wpi.first.wpilibj.Solenoid;

public class Shifter extends Solenoid {
    private final double kGearRatioPower;
    private final double kGearRatioSpeed;
    private final double kUnitsPerOutputRevolution;

    public Shifter(int port, double gearRatioPower, double gearRatioSpeed) {
        this(port, gearRatioPower, gearRatioSpeed, 0);
    }

    public Shifter(int port, double gearRatioPower, double gearRatioSpeed, double unitsPerOutputRevolution) {
        super(port);
        this.kGearRatioPower = gearRatioPower;
        this.kGearRatioSpeed = gearRatioSpeed;
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
            case Power:
                return kGearRatioPower;
            case Speed:
                return kGearRatioSpeed;
            default:
                return getGearRatio(ShifterState.Power);
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
        Speed(false),
        Power(true),
        Unknown(false);

        public final boolean value;

        ShifterState(boolean value) {
            this.value = value;
        }

        public ShifterState swap() {
            return getState(!this.value);
        }

        public static ShifterState getState(boolean value) {
            return (value) ? Power : Speed;
        }
    }
}
