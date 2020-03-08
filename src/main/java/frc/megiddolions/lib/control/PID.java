package frc.megiddolions.lib.control;

import edu.wpi.first.wpilibj.controller.PIDController;

public class PID {
    public double P;
    public double I;
    public double D;
    public int integralZone;

    public PID(double P, double I, double D) {
        this(P, I, D, 0);
    }

    public PID(double P, double I, double D, int integralZone) {
        this.P = P;
        this.I = I;
        this.D = D;
        this.integralZone = integralZone;
    }

    public PIDController makeController() {
        return new PIDController(P, I, D);
    }

    public PIDController makeController(double period) {
        return new PIDController(P, I, D, period);
    }
}
