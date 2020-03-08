package frc.megiddolions.lib;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.megiddolions.lib.control.FeedForward;
import frc.megiddolions.lib.control.PID;
import frc.megiddolions.lib.control.drivetrain.RamseteGen_DT;
import frc.megiddolions.lib.lambdas.Suppliers;
import frc.megiddolions.lib.hardware.power.CurrentSensor;

import java.nio.file.Paths;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class util {
    private static final int kChameleonPoseX = 0;
    private static final int kChameleonPoseY = 1;
    private static final int kChameleonPoseRotation = 2;
    public static final Trajectory kEmptyTrajectory = new Trajectory(List.of(new Trajectory.State()));

    public static Pose2d fromChameleonPose(double[] chameleonPose) {
        return new Pose2d(chameleonPose[kChameleonPoseX], chameleonPose[kChameleonPoseY], Rotation2d.fromDegrees(chameleonPose[kChameleonPoseRotation]));
    }

    public static Trajectory getTrajectory(String pathName) {
        try {
            return TrajectoryUtil.fromPathweaverJson(Paths.get(Filesystem.getDeployDirectory() + "/" + pathName + ".wpilib.json"));
        }
        catch (Exception e) {
            return kEmptyTrajectory;
        }
    }

    public static RamseteCommand generateRamseteCommand(String trajectoryName, RamseteGen_DT drivetrain) {
        return generateRamseteCommand(trajectoryName, drivetrain::getPose, drivetrain.getFeedForwardConstants(),
                drivetrain.getVelocityPID(), drivetrain.getKinematics(), drivetrain::getWheelSpeeds, drivetrain::tankDriveVolts, drivetrain);
    }

    public static RamseteCommand generateRamseteCommand(String trajectoryName, Supplier<Pose2d> poseSupplier,
                                                        FeedForward feedForwardConstants, PID velocityPID,
                                                        DifferentialDriveKinematics kinematics,
                                                        Supplier<DifferentialDriveWheelSpeeds> wheelSpeedsSupplier,
                                                        BiConsumer<Double, Double> tankDriveVolts, Subsystem drivetrain) {
        return generateRamseteCommand(getTrajectory(trajectoryName), poseSupplier, feedForwardConstants, velocityPID,
                kinematics, wheelSpeedsSupplier, tankDriveVolts, drivetrain);
    }

    public static RamseteCommand generateRamseteCommand(Trajectory trajectory, Supplier<Pose2d> poseSupplier,
                                                        FeedForward feedForwardConstants, PID velocityPID,
                                                        DifferentialDriveKinematics kinematics,
                                                        Supplier<DifferentialDriveWheelSpeeds> wheelSpeedsSupplier,
                                                        BiConsumer<Double, Double> tankDriveVolts, Subsystem drivetrain) {
        return new RamseteCommand(
                trajectory,
                poseSupplier,
                new RamseteController(),
                feedForwardConstants.toSimpleMotorFeedForward(),
                kinematics,
                wheelSpeedsSupplier,
                velocityPID.makeController(),
                velocityPID.makeController(),
                tankDriveVolts,
                drivetrain
        );
    }

    public static Trigger fromCurrentSensor(CurrentSensor currentSensor, double currentThresh) {
        return new Trigger(Suppliers.fromCurrentSensor(currentSensor, currentThresh));
    }

    public static double clamp(double num, double max) {
        return clamp(num, max, -max);
    }

    public static double clamp(double num, double min, double max) {
        return Math.max(Math.min(num, max), min);
    }

    public static int clamp(int num, int max) {
        return clamp(num, max, -max);
    }

    public static int clamp(int num, int min, int max) {
        return Math.max(Math.min(num, max), min);
    }

    public static void configSlave(BaseMotorController slave, BaseMotorController master) {
        configSlave(slave, master, false);
    }

    public static void configSlave(BaseMotorController slave, BaseMotorController master, boolean inverted) {
        slave.configFactoryDefault();
        slave.follow(master);
        slave.setInverted((inverted) ? InvertType.OpposeMaster : InvertType.FollowMaster);
    }

    public static double RPMtoRevolutionsPer100ms(double RPM) {
        return RPM / 600;
    }

    public static boolean checkRange(double value, double min, double max) {
        return value >= min && value <= max;
    }

    public static boolean checkTolerance(double value, double tolerance) {
        return checkTolerance(value, 0, tolerance);
    }

    public static boolean checkTolerance(double value, double target,  double tolerance) {
        return value >= target - tolerance && value <= target + tolerance;
    }

    public static double deadband(double value, double deadband) {
        if (Math.abs(value) <= deadband) {
            return 0;
        }
        return value;
    }
}
