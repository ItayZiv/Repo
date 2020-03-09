package frc.megiddolions.commands.drive;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.megiddolions.subsystems.DriveTrainSubsystem;

import java.util.function.DoubleSupplier;


public class ArcadeDriveCommand extends CommandBase {
    private final DriveTrainSubsystem driveTrain;
    private final DoubleSupplier speedSupplier;
    private final DoubleSupplier rotationSupplier;

    public ArcadeDriveCommand(DriveTrainSubsystem driveTrain, DoubleSupplier speedSupplier, DoubleSupplier rotationSupplier) {
        this.driveTrain = driveTrain;
        this.speedSupplier = speedSupplier;
        this.rotationSupplier = rotationSupplier;

        addRequirements(this.driveTrain);
    }

    @Override
    public void execute() {
        driveTrain.arcadeDrive(speedSupplier.getAsDouble(), rotationSupplier.getAsDouble());
    }

    @Override
    public void end(boolean interrupted) {
        driveTrain.stop();
    }
}
