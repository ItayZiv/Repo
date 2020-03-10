package frc.megiddolions.auto;

import edu.wpi.first.wpilibj.trajectory.Trajectory;
import frc.megiddolions.lib.util;

import java.io.IOException;

public class RamseteAction extends AutoAction {
    public final String path;

    public RamseteAction(String path) {
        super(ActionType.Drive);
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
