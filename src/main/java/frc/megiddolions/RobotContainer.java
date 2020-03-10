/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.megiddolions;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.megiddolions.Constants.*;
import frc.megiddolions.auto.AutoAction;
import frc.megiddolions.auto.Autos;
import frc.megiddolions.auto.RamseteAction;
import frc.megiddolions.commands.AlignTargetCommand;
import frc.megiddolions.commands.ControlPanelControlCommand;
import frc.megiddolions.commands.StopCommand;
import frc.megiddolions.commands.drive.ArcadeDriveCommand;
import frc.megiddolions.commands.drive.TankDriveCommand;
import frc.megiddolions.lib.Gamepad;
import frc.megiddolions.lib.dashboard.Dashboard;
import frc.megiddolions.lib.util;
import frc.megiddolions.subsystems.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer
{
    private final Joystick leftJoystick = new Joystick(OIConstants.kLeftJoystick);
    private final Joystick rightJoystick = new Joystick(OIConstants.kRightJoystick);
    private final Gamepad gamepad = new Gamepad(OIConstants.kGamepadPort);

    private final ClimbSubsystem climb;
    private final DriveTrainSubsystem driveTrain;
    private final ControlPanelSubsystem controlPanel;
    private final IntakeSubsystem intake;
    private final ShooterSubsystem shooter;
    private final VisionSubsystem vision;

    private final SendableChooser<Autos> autoChooser;
    private final Autos[] autos = new Autos[] {Autos.Test};
    /**
     * The container for the robot.  Contains subsystems, OI devices, and commands.
     */
    public RobotContainer()
    {
        climb = new ClimbSubsystem();
        driveTrain = new DriveTrainSubsystem();
        controlPanel = new ControlPanelSubsystem();
        intake = new IntakeSubsystem();
        shooter = new ShooterSubsystem();
        vision = new VisionSubsystem();

        driveTrain.setDefaultCommand(new TankDriveCommand(driveTrain, () -> leftJoystick.getY() * -1, () -> rightJoystick.getY() * -1));

        autoChooser = Dashboard.getInstance().addChooser("Auto", autos, Autos.Test);
        // Configure the button bindings
        configureButtonBindings();
    }

    /**
     * Use this method to define your button->command mappings.  Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick Joystick} or {@link XboxController}), and then passing it to a
     * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton JoystickButton}.
     */
    private void configureButtonBindings()
    {
        new JoystickButton(leftJoystick, OIConstants.kShifterJoystickButton).whenPressed(
                () -> driveTrain.setShifter(driveTrain.getShifter().swap()), driveTrain);
        new JoystickButton(rightJoystick, OIConstants.kAlignJoystickButton).whileHeld(new AlignTargetCommand(driveTrain, vision));
        new JoystickButton(rightJoystick, OIConstants.kStraightDriveButton).whileHeld(new ArcadeDriveCommand(driveTrain,
                () -> rightJoystick.getY() * -1, () -> 0));

        //Spin and stop shooter
        gamepad.A.whenPressed(() -> shooter.spin(ShooterConstants.kShooterRPM), shooter);
        gamepad.B.whenPressed(shooter::stopShooter, shooter);

        //Feed shooter and backup
        gamepad.X.whileHeld(() -> shooter.feedShooter(-0.5), shooter).whenReleased(shooter::stop, shooter);
        new JoystickButton(leftJoystick, 10).whenPressed(() -> shooter.feedShooter(0.8)).whenReleased(() -> shooter.feedShooter(0));

        //Intake in each direction
        gamepad.rightBumper.whileHeld(() -> intake.intake(1), intake).whenReleased(intake::stop, intake); //Intake
        gamepad.leftBumper.whileHeld(() -> intake.intake(-1), intake).whenReleased(intake::stop, intake);

        //Climb in each direction
        gamepad.dpad_up.whileHeld(() -> climb.climb(0.8)).whenReleased(climb::stop, climb);
        gamepad.dpad_down.whileHeld(() -> climb.climb(-0.8)).whenReleased(climb::stop, climb);

        gamepad.leftStick.whenPressed(
                () -> controlPanel.setMotorExtension(!controlPanel.getMotorExtension()), controlPanel).whileHeld(
                        new ControlPanelControlCommand(controlPanel, gamepad::getCombinedTriggers));
    }


    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand()
    {
        AtomicBoolean useDefaultAuto = new AtomicBoolean(false);
        List<AutoAction> actions = autoChooser.getSelected().actions;
        actions.forEach(autoAction -> {
            switch (autoAction.type) {
                case SpinShooter:
                    autoAction.command = new InstantCommand(() -> shooter.spin(ShooterConstants.kShooterRPM), shooter);
                    break;
                case Drive:
                    Trajectory traj;
                    if (AutoConstants.kUsePathWeaver) {
                        try {
                            traj = util.getTrajectory(autoAction.getPath());
                        } catch (IOException e) {
                            try {
                                DriverStation.reportError("Unable to open trajectory: " + ((RamseteAction) autoAction).path, e.getStackTrace());
                            } catch (Exception ex) {
                                DriverStation.reportError("Unable to open trajectory and unable to cast to Ramsete" +
                                        ex.getMessage(), e.getStackTrace());
                            }
                            traj = util.kEmptyTrajectory;
                            useDefaultAuto.set(true);
                        }
                    }
                    else {
                        traj = util.kEmptyTrajectory;
                        traj = TrajectoryGenerator.generateTrajectory()
                    }
                    if (!useDefaultAuto.get())
                        autoAction.command = new ParallelDeadlineGroup(
                                new WaitCommand(traj.getTotalTimeSeconds() + 2),
                                util.generateRamseteCommand(traj, driveTrain::getPose, DriveConstants.kFeedForwardConstants,
                                        DriveConstants.kVelocityPID, DriveConstants.kDriveKinematics,
                                        driveTrain::getWheelSpeeds, driveTrain::tankDriveVolts, driveTrain));
                    else
                        autoAction.command = new InstantCommand();
                    break;
                case StartIntake:
                    autoAction.command = new InstantCommand(() -> intake.intake(1), intake);
                    break;
                case Feed:
                    autoAction.command = new InstantCommand(() -> shooter.feedShooter(-0.5));
                    break;
                case Delay:
                    autoAction.command = new StopCommand(shooter, driveTrain).andThen(new WaitCommand(0.5));
                    break;
                case Stop:
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
        if (!useDefaultAuto.get()) {
            SequentialCommandGroup autoGroup = new SequentialCommandGroup();
            actions.forEach(autoAction -> autoGroup.addCommands(autoAction.command));
            return autoGroup;
        }
        else
            return getDefaultAuto();
    }

    public SequentialCommandGroup getDefaultAuto() {
        return new SequentialCommandGroup(util.generateRamseteCommand(AutoConstants.kDefaultTrajectory,
                driveTrain::getPose, DriveConstants.kFeedForwardConstants, DriveConstants.kVelocityPID,
                DriveConstants.kDriveKinematics, driveTrain::getWheelSpeeds, driveTrain::tankDriveVolts, driveTrain),
                new InstantCommand(() -> shooter.feedShooter(-0.6)),
                new WaitCommand(2.5), new InstantCommand(() -> shooter.feedShooter(-0.6)),
                new WaitCommand(2.5), new InstantCommand(() -> shooter.feedShooter(-0.6)));
    }
}
