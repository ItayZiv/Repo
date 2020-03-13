package frc.megiddolions.lib.hardware.oi;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;

public class Gamepad extends XboxController {

    public final JoystickButton A;
    public final JoystickButton B;
    public final JoystickButton X;
    public final JoystickButton Y;
    public final JoystickButton leftBumper;
    public final JoystickButton rightBumper;
    public final JoystickButton Back;
    public final JoystickButton Start;
    public final JoystickButton leftStick;
    public final JoystickButton rightStick;
    public final POVButton dpad_up;
    public final POVButton dpad_right;
    public final POVButton dpad_down;
    public final POVButton dpad_left;

    public final AxisActiveTrigger leftStickYActivity;
    public final AxisActiveTrigger leftStickXActivity;
    public final AxisActiveTrigger rightStickYActivity;
    public final AxisActiveTrigger rightStickXActivity;
    public final AxisActiveTrigger triggerActivity;

    public Gamepad(final int port) {
        super(port);

        A = new JoystickButton(this, Button.kA.value);
        B = new JoystickButton(this, Button.kB.value);
        X = new JoystickButton(this, Button.kX.value);
        Y = new JoystickButton(this, Button.kY.value);
        leftBumper = new JoystickButton(this, Button.kBumperLeft.value);
        rightBumper = new JoystickButton(this, Button.kBumperRight.value);
        Back = new JoystickButton(this, Button.kBack.value);
        Start = new JoystickButton(this, Button.kStart.value);
        leftStick = new JoystickButton(this, Button.kStickLeft.value);
        rightStick = new JoystickButton(this, Button.kStickRight.value);
        dpad_up = new POVButton(this, 0);
        dpad_right = new POVButton(this, 90);
        dpad_down = new POVButton(this, 180);
        dpad_left = new POVButton(this, 270);

        leftStickYActivity = new AxisActiveTrigger(() -> getY(Hand.kLeft));
        leftStickXActivity = new AxisActiveTrigger(() -> getX(Hand.kLeft));
        rightStickYActivity = new AxisActiveTrigger(() -> getY(Hand.kRight));
        rightStickXActivity = new AxisActiveTrigger(() -> getX(Hand.kRight));
        triggerActivity = new AxisActiveTrigger(this::getCombinedTriggers);
    }

    public double getCombinedTriggers() {
        double leftTrigger = getTriggerAxis(Hand.kLeft);
        double rightTrigger = getTriggerAxis(Hand.kRight);

        if (leftTrigger >= rightTrigger)
            return leftTrigger;
        else
            return -rightTrigger;
    }
}
