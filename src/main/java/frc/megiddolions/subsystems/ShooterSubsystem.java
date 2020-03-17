package frc.megiddolions.subsystems;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.megiddolions.Constants.ShooterConstants;
import frc.megiddolions.lib.hardware.motors.Stoppable;
import frc.megiddolions.lib.util;


public class ShooterSubsystem extends SubsystemBase implements Stoppable {
    private final WPI_TalonSRX shooterMaster;
    private final WPI_TalonSRX shooterSlave;

    private final SimpleMotorFeedforward shooterFeedForward = ShooterConstants.kVelocityFeedForward.toSimpleMotorFeedForward();

    private final WPI_VictorSPX indexMotor;

    public ShooterSubsystem() {
        shooterMaster = new WPI_TalonSRX(ShooterConstants.kMasterFlywheelTalon);
        shooterSlave = new WPI_TalonSRX(ShooterConstants.kSlaveFlywheelTalon);
        shooterMaster.configFactoryDefault();
        shooterSlave.configFactoryDefault();

        indexMotor = new WPI_VictorSPX(ShooterConstants.kShooterIndexerVictor);
        indexMotor.configFactoryDefault();

        shooterMaster.set(ControlMode.Velocity, 0);
        shooterMaster.configAllSettings(ShooterConstants.getShooterTalonConfiguration(true));
        shooterMaster.setSensorPhase(ShooterConstants.kInvertedEncoderPhase);
        shooterMaster.selectProfileSlot(0, 0);
        shooterMaster.setInverted(ShooterConstants.kInvertFlywheelMotors);

        shooterSlave.configAllSettings(ShooterConstants.getShooterTalonConfiguration(false));
        shooterSlave.setInverted(InvertType.OpposeMaster);

        indexMotor.setInverted(ShooterConstants.kInvertIndexMotor);
    }

    public void spin(int revolutionPerMinute) {
        shooterMaster.set(ControlMode.Velocity, revolutionPerMinute, DemandType.ArbitraryFeedForward,
                shooterFeedForward.calculate(util.UnitsPerMinuteToUnitsPerSecond(revolutionPerMinute) /
                        RobotController.getBatteryVoltage()));
    }

    public void spin(double power) {
        shooterMaster.set(power);
    }

    public void feedShooter(double speed) {
        indexMotor.set(speed);
    }

    public boolean atSetpoint() {
        return util.checkTolerance(indexMotor.getClosedLoopError(), ShooterConstants.kVelocityToleranceRevolutionsPerMinute);
    }

    public void stopShooter() {
        shooterMaster.set(ControlMode.Velocity, 0);
        shooterMaster.set(0);
        shooterMaster.stopMotor();
    }

    public void stop() {
        indexMotor.stopMotor();
    }
}

