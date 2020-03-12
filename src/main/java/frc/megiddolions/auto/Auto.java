package frc.megiddolions.auto;

import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.megiddolions.Constants;
import frc.megiddolions.commands.AlignTargetCommand;
import frc.megiddolions.commands.StopCommand;
import frc.megiddolions.lib.hardware.motors.Stoppable;
import frc.megiddolions.lib.util;
import frc.megiddolions.subsystems.DriveTrainSubsystem;
import frc.megiddolions.subsystems.IntakeSubsystem;
import frc.megiddolions.subsystems.ShooterSubsystem;
import frc.megiddolions.subsystems.VisionSubsystem;

import java.util.List;

public class Auto {
    private final List<AutoAction> actions;

    public Auto(AutoAction... actions) {
        this.actions = List.of(actions);
    }

    public Command generateAuto() {
        SequentialCommandGroup auto = new SequentialCommandGroup();
        actions.forEach((autoAction -> {
            auto.addCommands(autoAction.command);
        }));
        return auto;
    }

    public Auto generateAutoCommands(DriveTrainSubsystem driveTrain, ShooterSubsystem shooter,
                                    IntakeSubsystem intake, VisionSubsystem vision) {
        actions.forEach(autoAction -> {
            switch (autoAction.type) {
                case SpinShooter:
                    autoAction.command = new InstantCommand(() -> shooter.spin(Constants.ShooterConstants.kShooterRPM), shooter);
                    break;
                case Drive:
                    Trajectory trajectory = ((RamseteAction) autoAction).getPath().makeTrajectory(Constants.AutoConstants.config);
                    autoAction.command = util.generateRamseteCommand(trajectory, driveTrain::getPose,
                            Constants.DriveConstants.kFeedForwardConstants, Constants.DriveConstants.kVelocityPID,
                            Constants.DriveConstants.kDriveKinematics, driveTrain::getWheelSpeeds, driveTrain::tankDriveVolts,
                            driveTrain).withTimeout(trajectory.getTotalTimeSeconds() + 2);
                    break;
                case AlignTarget:
                    autoAction.command = new AlignTargetCommand(driveTrain, vision).withTimeout(3);
                    break;
                case Shoot:
                    autoAction.command = new Auto(new AutoAction(ActionType.AlignTarget),
                            new StopAction(StopAction.AutoStoppables.Drivetrain), new AutoAction(ActionType.Feed),
                            new DelayAction(0.4), new StopAction(StopAction.AutoStoppables.Drivetrain,
                            StopAction.AutoStoppables.ShooterFeed), new DelayAction(0.4))
                            .generateAutoCommands(driveTrain, shooter, intake, vision).generateAuto();
                    break;
                case StartIntake:
                    autoAction.command = new InstantCommand(() -> intake.intake(1), intake);
                    break;
                case Feed:
                    autoAction.command = new InstantCommand(() -> shooter.feedShooter(-0.5));
                    break;
                case Delay:
                    autoAction.command = new WaitCommand(((DelayAction)autoAction).getLength());
                    break;
                case Stop:
                    autoAction.command = new StopCommand(((StopAction)autoAction)
                            .getStoppables(driveTrain, shooter, intake).toArray(Stoppable[]::new));
                    break;
                case End:
                    autoAction.command = new InstantCommand(() -> {
                        shooter.stop();
                        shooter.stopShooter();
                        driveTrain.stop();
                        intake.stop();
                    }, shooter, driveTrain, intake);
                    break;
                default:
                    autoAction.command = new InstantCommand();
                    break;
            }
        });
        return this;
    }
}
