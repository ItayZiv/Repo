package frc.megiddolions.subsystems;


import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.megiddolions.Constants.ClimbConstants;
import frc.megiddolions.lib.hardware.motors.Stoppable;

public class ClimbSubsystem extends SubsystemBase implements Stoppable {
    private final WPI_TalonSRX climbMotor;

    private final WPI_TalonSRX balanceMotor;

    public ClimbSubsystem() {
        climbMotor = new WPI_TalonSRX(ClimbConstants.kClimbTalon);
        climbMotor.configFactoryDefault();
        climbMotor.setNeutralMode(NeutralMode.Brake);

        balanceMotor = new WPI_TalonSRX(ClimbConstants.kClimbBalanceTalon);
        balanceMotor.configFactoryDefault();
        balanceMotor.setNeutralMode(NeutralMode.Brake);
    }

    public void climb(double power) {
        climbMotor.set(power);
    }

    public void balance(double power) {
        balanceMotor.set(power);
    }

    public void stop() {
        climbMotor.stopMotor();
        balanceMotor.stopMotor();
    }
}

