package frc.megiddolions.lib;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;

public class XboxGamepad extends XboxController {

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

    public XboxGamepad(final int port) {
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
    }
}
