package frc.megiddolions.lib.control.trajectories;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.constraint.TrajectoryConstraint;

import java.util.List;

public class Path {
    private List<Pose2d> poseList;
    private boolean reversed;
    private List<TrajectoryConstraint> constraints;

    public Path() {
        this(List.of(new Pose2d()), false);
    }

    public Path(List<Pose2d> poseList, boolean reversed, TrajectoryConstraint... constraints) {
        this.poseList = poseList;
        this.reversed = reversed;
        this.constraints = List.of(constraints);
    }

    public Trajectory makeTrajectory(TrajectoryConfig config) {
        config.addConstraints(constraints);
        config.setReversed(reversed);
        return TrajectoryGenerator.generateTrajectory(poseList, config);
    }

    public Pose2d getFirstPose() {
        return  poseList.get(0);
    }
}
