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
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.megiddolions.Constants.AutoConstants;
import frc.megiddolions.Constants.DriveConstants;
import frc.megiddolions.Constants.OIConstants;
import frc.megiddolions.Constants.ShooterConstants;
import frc.megiddolions.auto.Auto;
import frc.megiddolions.auto.Autos;
import frc.megiddolions.commands.AlignTargetCommand;
import frc.megiddolions.commands.ControlPanelControlCommand;
import frc.megiddolions.commands.drive.ArcadeDriveCommand;
import frc.megiddolions.commands.drive.TankDriveCommand;
import frc.megiddolions.lib.dashboard.Dashboard;
import frc.megiddolions.lib.hardware.motors.Shifter;
import frc.megiddolions.lib.hardware.oi.Gamepad;
import frc.megiddolions.lib.util;
import frc.megiddolions.subsystems.*;

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
    private final Autos[] autos = new Autos[] {Autos.Test, Autos.Full};
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
        new JoystickButton(rightJoystick, OIConstants.kAlignJoystickButton).whileHeld(new AlignTargetCommand(driveTrain, vision));
        new JoystickButton(rightJoystick, OIConstants.kStraightDriveButton).whileHeld(new ArcadeDriveCommand(driveTrain,
                () -> rightJoystick.getY() * -1, () -> 0));

        //Spin and stop shooter
        gamepad.A.whenPressed(() -> shooter.spin(ShooterConstants.kShooterRPM), shooter);
        gamepad.B.whenPressed(shooter::stopShooter, shooter);

        //Feed shooter and backup
        gamepad.X.whileHeld(() -> shooter.feedShooter(-0.5), shooter).whenReleased(shooter::stop, shooter);
        new JoystickButton(leftJoystick, 10).whenPressed(() -> shooter.feedShooter(0.8))
                .whenReleased(() -> shooter.feedShooter(0));

        //Intake in each direction
        gamepad.rightBumper.whileHeld(() -> intake.intake(1), intake).whenReleased(intake::stop, intake); //Intake
        gamepad.leftBumper.whileHeld(() -> intake.intake(-1), intake).whenReleased(intake::stop, intake);

        //Climb in each direction
        gamepad.dpad_up.whileHeld(() -> climb.climb(0.8)).whenReleased(climb::stop, climb);
        gamepad.dpad_down.whileHeld(() -> climb.climb(-0.8)).whenReleased(climb::stop, climb);

        gamepad.triggerActivity
                .whenActive(() -> controlPanel.setMotorExtension(true), controlPanel)
                .whileActiveContinuous(new ControlPanelControlCommand(controlPanel, gamepad::getCombinedTriggers))
                .whenInactive(() -> controlPanel.setMotorExtension(false), controlPanel);
    }


    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand()
    {
        boolean useDefaultAuto = false;
        Auto auto = autoChooser.getSelected().auto;
        try {
            auto.generateAutoCommands(driveTrain, shooter, intake, vision);
        }
        catch (Exception e) {
            DriverStation.reportError("Unable to import auto.", e.getStackTrace());
            useDefaultAuto = true;
        }
        if (!useDefaultAuto) {
            return auto.generateAuto();
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
                new WaitCommand(2.5), new InstantCommand(() -> shooter.feedShooter(-0.6)),
                new InstantCommand(() -> {
                    shooter.stop();
                    shooter.stopShooter();
                    driveTrain.stop();
                    intake.stop();
                }, shooter, driveTrain, intake));
    }

    public Command getInitCommand() {
        return new InstantCommand(() -> {}, climb, controlPanel, driveTrain, intake, shooter, vision) {
            @Override
            public void initialize() {
                driveTrain.setShifter(Shifter.ShifterState.Low);
                driveTrain.reset();
            }
        };
    }
}
