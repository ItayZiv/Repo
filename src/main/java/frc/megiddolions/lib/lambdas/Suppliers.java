package frc.megiddolions.lib.lambdas;

import frc.megiddolions.lib.annotations.NeedsTesting;
import frc.megiddolions.lib.hardware.power.CurrentSensor;

import java.util.function.BooleanSupplier;

public class Suppliers {

    @NeedsTesting
    public static BooleanSupplier fromCurrentSensor(CurrentSensor currentSensor, double currentThresh)
    {
        return () -> currentSensor.getCurrent() >= currentThresh;
    }
}
