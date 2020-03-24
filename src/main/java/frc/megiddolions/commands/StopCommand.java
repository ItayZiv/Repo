package frc.megiddolions.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.megiddolions.lib.hardware.motors.Stoppable;


public class StopCommand extends CommandBase {
    private final Stoppable[] stoppables;

    public StopCommand(Stoppable... stoppables) {
        this.stoppables = stoppables;

        addRequirements(stoppables);
    }

    @Override
    public void initialize() {
        for (Stoppable stoppable : stoppables) {
            stoppable.stop();
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
