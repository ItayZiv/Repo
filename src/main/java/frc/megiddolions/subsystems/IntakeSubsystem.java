package frc.megiddolions.subsystems;


import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.megiddolions.Constants.IntakeConstants;

public class IntakeSubsystem extends SubsystemBase {
    private final WPI_VictorSPX intakeMotor;

    public IntakeSubsystem() {
        intakeMotor = new WPI_VictorSPX(IntakeConstants.kIntakeVictor);
        intakeMotor.configFactoryDefault();
    }

    public void intake(double power) {
        intakeMotor.set(power);
    }

    public void stop() {
        intakeMotor.stopMotor();
    }
}

