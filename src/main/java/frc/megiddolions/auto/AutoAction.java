package frc.megiddolions.auto;


import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutoAction {
    public final ActionType type;
    public CommandBase command;

    public AutoAction(ActionType type) {
        this.type = type;
    }

    public static AutoAction SpinShooter() {
        return new AutoAction(ActionType.SpinShooter);
    }
    public static AutoAction Feed() {
        return  new AutoAction(ActionType.Feed);
    }
    public static AutoAction Align() {
        return new AutoAction(ActionType.AlignTarget);
    }
}
