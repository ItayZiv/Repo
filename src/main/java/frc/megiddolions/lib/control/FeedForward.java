package frc.megiddolions.lib.control;

import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;

public class FeedForward {
    public double sVolts;
    public double vVoltsSecondsPerMeter;
    public double aVoltsSecondsSquaredPerMeter;

    public FeedForward(double sVolts, double vVoltsSecondsPerMeter) {
        this(sVolts, vVoltsSecondsPerMeter, 0);
    }

    public FeedForward(double sVolts, double vVoltsSecondsPerMeter, double aVoltsSecondsSquaredPerMeter) {
        this.sVolts = sVolts;
        this.vVoltsSecondsPerMeter = vVoltsSecondsPerMeter;
        this.aVoltsSecondsSquaredPerMeter = aVoltsSecondsSquaredPerMeter;
    }

    public SimpleMotorFeedforward toSimpleMotorFeedForward() {
        return new SimpleMotorFeedforward(sVolts, vVoltsSecondsPerMeter, aVoltsSecondsSquaredPerMeter);
    }
}
