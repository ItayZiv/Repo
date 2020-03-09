/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.megiddolions;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.megiddolions.Constants.*;
import frc.megiddolions.commands.AlignTargetCommand;
import frc.megiddolions.commands.drive.TankDriveCommand;
import frc.megiddolions.lib.Gamepad;
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

    private final DriveTrainSubsystem driveTrain;
    private final ControlPanelSubsystem controlPanel;
    private final IntakeSubsystem intake;
    private final ShooterSubsystem shooter;
    private final VisionSubsystem vision;

    /**
     * The container for the robot.  Contains subsystems, OI devices, and commands.
     */
    public RobotContainer()
    {
        driveTrain = new DriveTrainSubsystem();
        controlPanel = new ControlPanelSubsystem();
        intake = new IntakeSubsystem();
        shooter = new ShooterSubsystem();
        vision = new VisionSubsystem();

        driveTrain.setDefaultCommand(new TankDriveCommand(driveTrain, () -> leftJoystick.getY() * -1, () -> rightJoystick.getY() * -1));
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
        new JoystickButton(leftJoystick, OIConstants.kShifterJoystickButton).whenPressed(new InstantCommand(() ->
                driveTrain.setShifter(driveTrain.getShifter().swap()), driveTrain));
        new JoystickButton(rightJoystick, OIConstants.kAlignJoystickButton).whileHeld(new AlignTargetCommand(driveTrain, vision));
        new JoystickButton(rightJoystick, OIConstants.kStraightDriveButton).whileHeld(new TankDriveCommand(driveTrain,
                () -> rightJoystick.getY() * -1, () -> rightJoystick.getY() * -1));
    }


    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand()
    {
        return new InstantCommand();
    }
}
