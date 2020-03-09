package frc.megiddolions.commands.drive;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.megiddolions.subsystems.DriveTrainSubsystem;

import java.util.function.DoubleSupplier;


public class TankDriveCommand extends CommandBase {
    private final DriveTrainSubsystem driveTrain;
    private final DoubleSupplier leftSupplier;
    private final DoubleSupplier rightSupplier;

    public TankDriveCommand(DriveTrainSubsystem driveTrain, DoubleSupplier leftSupplier, DoubleSupplier rightSupplier) {
        this.driveTrain = driveTrain;
        this.leftSupplier = leftSupplier;
        this.rightSupplier = rightSupplier;

        addRequirements(this.driveTrain);
    }

    @Override
    public void execute() {
        driveTrain.tankDrive(leftSupplier.getAsDouble(), rightSupplier.getAsDouble());
    }

    @Override
    public void end(boolean interrupted) {
        driveTrain.stop();
    }
}
