package frc.megiddolions.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.megiddolions.Constants;
import frc.megiddolions.subsystems.DriveTrainSubsystem;
import frc.megiddolions.subsystems.VisionSubsystem;


public class AlignTargetCommand extends CommandBase {
    private final DriveTrainSubsystem driveTrain;
    private final VisionSubsystem vision;
    private final PIDController feedback;

    public AlignTargetCommand(DriveTrainSubsystem driveTrain, VisionSubsystem vision) {
        this.driveTrain = driveTrain;
        this.vision = vision;

        feedback = Constants.DriveConstants.kPositionVisionPID.makeController();
        feedback.setTolerance(Constants.ShooterConstants.kAlignmentTolerance);

        addRequirements(this.driveTrain, this.vision);
    }

    @Override
    public void initialize() {
        feedback.setSetpoint(0);
    }

    @Override
    public void execute() {
        double pow = feedback.calculate(vision.getWantedHorizontalError(driveTrain.getHeading()));

        //TODO: Switch to arcadeDrive based alignment
        driveTrain.tankDrive(pow, -pow);
    }

    @Override
    public boolean isFinished() {
        return feedback.atSetpoint();
    }

    @Override
    public void end(boolean interrupted) {
        driveTrain.stop();
    }
}
