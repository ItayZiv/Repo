package frc.megiddolions.auto;


import edu.wpi.first.wpilibj2.command.Command;

public class AutoAction {
    public final ActionType type;
    public Command command;

    public AutoAction(ActionType type) {
        this.type = type;
    }

}
