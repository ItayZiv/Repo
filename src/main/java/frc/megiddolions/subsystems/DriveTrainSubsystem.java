package frc.megiddolions.subsystems;


import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.megiddolions.Constants.DriveConstants;
import frc.megiddolions.lib.control.drivetrain.Auto_DT;
import frc.megiddolions.lib.control.drivetrain.DifferentialDrive;
import frc.megiddolions.lib.control.drivetrain.DriveTrain;
import frc.megiddolions.lib.hardware.motors.Stoppable;

import java.util.function.DoubleSupplier;

public class DriveTrainSubsystem extends SubsystemBase implements DriveTrain, Auto_DT, Stoppable {
    private final CANSparkMax leftMaster;
    private final CANSparkMax leftSlave;
    private final CANSparkMax rightMaster;
    private final CANSparkMax rightSlave;

    private final DifferentialDrive drive;

    private final Solenoid shifter;

    private final CANEncoder leftEncoder;
    private final CANEncoder rightEncoder;

    private final AHRS navX;
    private final DoubleSupplier headingSupplier;

    private final DifferentialDriveOdometry odometry;

    public DriveTrainSubsystem() {
        leftMaster = new CANSparkMax(DriveConstants.kFrontLeftSpark, MotorType.kBrushless);
        leftSlave = new CANSparkMax(DriveConstants.kRearLeftSpark, MotorType.kBrushless);
        rightMaster = new CANSparkMax(DriveConstants.kFrontRightSpark, MotorType.kBrushless);
        rightSlave = new CANSparkMax(DriveConstants.kRearRightSpark, MotorType.kBrushless);

        shifter = new Solenoid(DriveConstants.kShifterPCMPort);

        leftMaster.restoreFactoryDefaults();
        leftSlave.restoreFactoryDefaults();
        rightMaster.restoreFactoryDefaults();
        rightSlave.restoreFactoryDefaults();

        leftSlave.follow(leftMaster);
        rightSlave.follow(rightMaster);

        leftEncoder = leftMaster.getEncoder();
        rightEncoder = rightMaster.getEncoder();
        leftEncoder.setPositionConversionFactor(DriveConstants.kDistancePerMotorRevolution);
        rightEncoder.setPositionConversionFactor(DriveConstants.kDistancePerMotorRevolution);
        leftEncoder.setVelocityConversionFactor(DriveConstants.kSpeedMetersPerSecondFromRevolutionPerMinute);
        rightEncoder.setVelocityConversionFactor(DriveConstants.kSpeedMetersPerSecondFromRevolutionPerMinute);

        navX = new AHRS(SPI.Port.kMXP);
        headingSupplier = () -> navX.getAngle() * -1;

        odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(headingSupplier.getAsDouble()));

        drive = new DifferentialDrive((double left, double right) -> {
            leftMaster.set(left);
            rightMaster.set(right);
        });

        setShifter(ShifterState.Power);
        reset();
    }

    @Override
    public void periodic() {
        odometry.update(getHeading(), leftEncoder.getPosition(), rightEncoder.getPosition());
    }

    public void tankDrive(double leftPower, double rightPower) {
        drive.tankDrive(leftPower, rightPower);
    }

    public void arcadeDrive(double xSpeed, double yRotation) {
        drive.arcadeDrive(xSpeed, yRotation);
    }

    public void tankDriveVolts(double leftVolts, double rightVolts) {
        drive.tankDriveVolts(leftVolts, rightVolts);
    }

    public void setShifter(ShifterState state) {
        //TODO: Reset encoders and modify ConversionFactors when switching gear, and keep the previous pose
        shifter.set(state.value);
    }

    public ShifterState getShifter() {
        return ShifterState.getState(shifter.get());
    }

    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
        return new DifferentialDriveWheelSpeeds(leftEncoder.getVelocity(), rightEncoder.getVelocity());
    }

    public Rotation2d getHeading() {
        return Rotation2d.fromDegrees(headingSupplier.getAsDouble());
    }

    public Pose2d getPose() {
        return odometry.getPoseMeters();
    }

    public void reset() {
        resetGyro();
        resetOdometry();
    }

    public void resetOdometry() {
        resetOdometry(new Pose2d());
    }

    public void resetOdometry(Pose2d pose) {
        resetEncoders();
        odometry.resetPosition(pose, Rotation2d.fromDegrees(headingSupplier.getAsDouble()));
    }

    public void resetEncoders() {
        leftEncoder.setPosition(0);
        rightEncoder.setPosition(0);
    }

    public void resetGyro() {
        navX.reset();
    }

    public void stop() {
        drive.stop();
        leftMaster.stopMotor();
        rightMaster.stopMotor();
    }

    public enum ShifterState {
        Speed(false),
        Power(true);

        boolean value;

        ShifterState(boolean value) {
            this.value = value;
        }

        public ShifterState swap() {
            return getState(!this.value);
        }

        public static ShifterState getState(boolean value) {
            return (value) ? Power : Speed;
        }
    }
}

