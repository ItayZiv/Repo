package frc.megiddolions.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.megiddolions.Constants.ControlPanelConstants;
import frc.megiddolions.lib.InfiniteRecharge.ControlPanel;
import frc.megiddolions.lib.dashboard.Dashboard;
import frc.megiddolions.lib.dashboard.DashboardItem;

public class ControlPanelSubsystem extends SubsystemBase {
    private final WPI_TalonSRX spinMotor;
    private final Solenoid motorExtender;

    private final ColorSensorV3 colorSensor;

    private ColorMatch colorMatch;
    private ColorMatchResult previousColor;
    private ColorMatchResult foundColor;
    private ControlPanel.ControlPanelColor targetColor;
    private int count = 0;

    private boolean enabled = false;

    public ControlPanelSubsystem() {
        spinMotor = new WPI_TalonSRX(ControlPanelConstants.kSpinTalon);
        spinMotor.configFactoryDefault();

        motorExtender = new Solenoid(ControlPanelConstants.kPistonPort);

        ColorSensorV3 color;
        try {
            color = new ColorSensorV3(I2C.Port.kOnboard);
        }
        catch (Exception e) {
            color = null;
        }
        colorSensor = color;

        colorMatch.addColorMatch(ControlPanel.ControlPanelColor.Blue.colorVal);
        colorMatch.addColorMatch(ControlPanel.ControlPanelColor.Green.colorVal);
        colorMatch.addColorMatch(ControlPanel.ControlPanelColor.Red.colorVal);
        colorMatch.addColorMatch(ControlPanel.ControlPanelColor.Yellow.colorVal);

        Dashboard.getInstance().addData("ColorKey", DriverStation.getInstance()::getGameSpecificMessage);
        Dashboard.getInstance().addData("Color", ControlPanel::getOffsetColorAssignment);
    }

    @Override
    public void periodic() {
        targetColor = ControlPanel.getOffsetColorAssignment();
        if (enabled) {
            if (colorSensor != null)
                foundColor = colorMatch.matchClosestColor(colorSensor.getColor());
            else
                foundColor = new ColorMatchResult(new Color(0, 0, 0), 0);
            if (foundColor.color != previousColor.color)
                count++;
            previousColor = foundColor;
        }
    }

    public void setMotorExtension(boolean state) {
        motorExtender.set(state);
    }

    public boolean getMotorExtension() {
        return motorExtender.get();
    }

    public void spinTurns(int spinCount) {
        enabled = true;
        count = 0;
        if (count <= spinCount * ControlPanelConstants.kColorsChangesPerRevolution)
            spin(0.9);
        else
            stop();
    }

    public void spinToColor() {
        enabled = true;
        if (foundColor.color != targetColor.colorVal)
            spin(0.9);
        else
            stop();
    }

    public void spin(double power) {
        spinMotor.set(power);
    }

    public void stop() {
        spinMotor.stopMotor();
    }
}
