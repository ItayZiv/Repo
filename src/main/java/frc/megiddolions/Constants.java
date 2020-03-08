/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.megiddolions;

import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.util.Units;
import frc.megiddolions.lib.control.FeedForward;
import frc.megiddolions.lib.control.PID;

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
}