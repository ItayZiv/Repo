/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.megiddolions;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod;
import com.ctre.phoenix.motorcontrol.can.SlotConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.util.Units;
import frc.megiddolions.lib.util;
import frc.megiddolions.lib.control.FeedForward;
import frc.megiddolions.lib.control.PID;
import frc.megiddolions.lib.lambdas.UnitConverter;

import java.util.function.DoubleFunction;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 * <p>
 * It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants 
{
    public static double kRobotPeriod = 0.02;

    public static final class DriveConstants {
        public static final int kFrontLeftSpark = 2;
        public static final int kRearLeftSpark = 3;
        public static final int kFrontRightSpark = 13;
        public static final int kRearRightSpark = 1;

        public static final int kShifterPCMPort = 0;

        public static final double kDriveDeadband = 0.1;

        public static final double kTrackWidthMeters = 0.837094301;

        private static final double kWheelDiameterInches = 6;
        public static final double kWheelDiameterMeters = Units.inchesToMeters(kWheelDiameterInches);
        public static final double kMotorGearRatio = 5.952380952;

        public static final double kDistancePerMotorRevolution = (kWheelDiameterMeters * Math.PI) / kMotorGearRatio;
        public static final double kSpeedMetersPerSecondFromRevolutionPerMinute = kDistancePerMotorRevolution * (1.0 / 60);

        public static final DifferentialDriveKinematics kDriveKinematics = new DifferentialDriveKinematics(kTrackWidthMeters);

        public static final double ksVolts = 1.07;
        public static final double kvVoltsSecondsPerMeter = 1.44;
        public static final double kaVoltsSecondsSquaredPerMeter = 0.052;
        public static final FeedForward kFeedForwardConstants = new FeedForward(ksVolts, kvVoltsSecondsPerMeter, kaVoltsSecondsSquaredPerMeter);

        public static final double kVelocityP = 1.93;
        public static final PID kVelocityPID = new PID(kVelocityP, 0, 0);

        public static final PID kPositionVisionPID = new PID(0.04, 0.001, 0.002);
    }

    public static final class ShooterConstants {
        public static final int kMasterFlywheelTalon = 4;
        public static final int kSlaveFlywheelTalon = 5;

        public static final int kShooterIndexerVictor = 6;

        public static final boolean kInvertFlywheelMotors = false;
        public static final boolean kInvertIndexMotor = true;

        public static final int kEncoderUnitsPerRevolution = 4096;
        public static final boolean kInvertedEncoderPhase = true;
        public static final double kEncoderGearing = 1;

        public static final int kShooterPeakCurrentLimit = 40;
        public static final int kShooterPeakCurrentDuration = 5000;
        public static final int kShooterConstantCurrentLimit = 20;

        public static final UnitConverter kUnitsToRevolutions =
                (double units) -> units / (kEncoderGearing * kEncoderUnitsPerRevolution);
        public static final UnitConverter kUnitsPer100msToRevolutionsPerMinute =
                (double unitsPer100ms) ->
                        util.RevolutionsPer100msToRevolutionsPerMinute(kUnitsToRevolutions.convert(unitsPer100ms));

        public static final UnitConverter kRevolutionsToUnits =
                (double revolutions) -> revolutions * kEncoderGearing * kEncoderUnitsPerRevolution;
        public static final UnitConverter kRevolutionsPerMinuteToUnitsPer100ms =
                (double revolutionsPerMinute) ->
                        util.RevolutionsPerMinuteToRevolutionsPer100ms(kRevolutionsToUnits.convert(revolutionsPerMinute));

        public static final PID kVelocityPID = new PID(1.2, 0.8, 0.8, 750); // 0.32 0 0.8

        public static final FeedForward kVelocityFeedForward =
                new FeedForward(0.873, 0.147, 0.23);


        public static final double kVelocityToleranceRevolutionsPerMinute = 50;
        public static final double kVelocityToleranceUnitsPer100ms =
                kRevolutionsPerMinuteToUnitsPer100ms.convert(kVelocityToleranceRevolutionsPerMinute);
        public static final double kAlignmentTolerance = 1;

        public static final double defaultSpeed = 0.2;

        public static final int kErrorOffset = 0;
        public static final int kMaxError = 400;

        public static final int kMicroSwitch = 4;

        public static TalonSRXConfiguration getShooterTalonConfiguration(boolean isMaster) {
            TalonSRXConfiguration config = new TalonSRXConfiguration();
            config.continuousCurrentLimit = kShooterConstantCurrentLimit;
            config.peakCurrentDuration = kShooterPeakCurrentDuration;
            config.peakCurrentLimit = kShooterPeakCurrentLimit;

            if (isMaster) {
                config.primaryPID.selectedFeedbackSensor = FeedbackDevice.QuadEncoder;
                config.velocityMeasurementPeriod = VelocityMeasPeriod.Period_10Ms;
                config.velocityMeasurementWindow = 32;
                config.slot0 = ShooterConstants.kVelocityPID.makeTalonSlotConfig();
                config.slot0.allowableClosedloopError = (int)Math.round(kVelocityToleranceUnitsPer100ms);
            }

            return config;
        }
    }

    public static final class IntakeConstants {
        public static final int kIntakeVictor = 8;
    }

    public static final class ClimbConstants {
        public static final int kClimbTalon = 11;
        public static final int kClimbBalanceTalon = 12;
    }

    public static final class ControlPanelConstants {
        public static final int kSpinTalon = 7;

        public static final int kPistonPort = 3;

        public static final int kSpinnerPCMPort = 1;

        public static final int kSpinsPerColor = 4;
        public static final int kColorsPerRevolution = 8;
        public static final double kMaxSpeed = 40;

        public static final double kRotationControlSpins = 4;

        public static final int kColorsChangesPerRevolution = 8;
    }

    public static final class VisionConstants {
        public static final String kCameraName = "Microsoft LifeCam HD-3000";

        public static final double kCameraOffset = 0.0155;

        public static final double kCameraPitch = 26;

        public static final double kPortDistMeters = Units.inchesToMeters(29.25);

        public static final double kMaxInnerPortAngle = 25;

        public static final UnitConverter kPitchToDistance = (double angle) ->
                0.0114 * Math.pow(angle, 2) - 0.0622 * angle + 3.3753;
    }

    public static final class OIConstants {
        public static final int kLeftJoystick = 0;
        public static final int kRightJoystick = 1;
        public static final int kGamepadPort = 2;

        public static final int kShifterJoystickButton = 1;
        public static final int kAlignJoystickButton = 1;
        public static final int kStraightDriveButton = 2;
    }

    public static final class DashboardConstants {
        public static final String kDashboardTable = "Dashboard";
    }
}