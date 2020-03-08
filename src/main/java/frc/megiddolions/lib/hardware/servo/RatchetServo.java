package frc.megiddolions.lib.hardware.servo;

import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.Timer;

public class RatchetServo {
    private final double openTimeSeconds;
    private final PWM servo;
    private Timer timer = new Timer();

    public RatchetServo(PWM servo, double openTimeSeconds) {
        this.servo = servo;
        this.openTimeSeconds = openTimeSeconds;
        timer.start();
    }

    public void setPosition(double pos) {
        servo.setPosition(pos);
        timer.reset();
    }

    public boolean isReady() {
        return timer.hasPeriodPassed(openTimeSeconds);
    }
}
