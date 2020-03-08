package frc.megiddolions.lib.control;

import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;

public class FeedForward {
    public double S;
    public double V;
    public double A;

    public FeedForward(double S, double V) {
        this(S, V, 0);
    }

    public FeedForward(double S, double V, double A) {
        this.S = S;
        this.V = V;
        this.A = A;
    }

    public SimpleMotorFeedforward toSimpleMotorFeedForward() {
        return new SimpleMotorFeedforward(S, V, A);
    }
}
