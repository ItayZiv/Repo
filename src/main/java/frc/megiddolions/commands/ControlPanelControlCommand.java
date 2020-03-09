package frc.megiddolions.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.megiddolions.subsystems.ControlPanelSubsystem;

import java.util.function.DoubleSupplier;


public class ControlPanelControlCommand extends CommandBase {
    private final ControlPanelSubsystem controlPanel;
    private final DoubleSupplier speedSupplier;

    public ControlPanelControlCommand(ControlPanelSubsystem controlPanel, DoubleSupplier speedSupplier) {
        this.controlPanel = controlPanel;
        this.speedSupplier = speedSupplier;

        addRequirements(this.controlPanel);
    }

    @Override
    public void execute() {
        controlPanel.spin(speedSupplier.getAsDouble());
    }

    @Override
    public void end(boolean interrupted) {
        controlPanel.stop();
    }
}
