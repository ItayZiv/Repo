package frc.megiddolions.auto;


import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.megiddolions.lib.util;

import java.io.IOException;

public class AutoAction {
    public final ActionType type;
    public CommandBase command;

    public AutoAction(ActionType type) {
        this.type = type;
    }

    public String getPath() {
        return "";
    }
}
