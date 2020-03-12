package frc.megiddolions.auto;

import frc.megiddolions.lib.control.trajectories.Path;

public class RamseteAction extends AutoAction {
    public final Path path;

    public RamseteAction(Path path) {
        super(ActionType.Drive);
        this.path = path;
    }

    public Path getPath() {
        return path;
    }
}
