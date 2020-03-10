package frc.megiddolions.auto;

import edu.wpi.first.wpilibj2.command.InstantCommand;

import java.util.List;

public enum Autos {
    Test(List.of(new RamseteAction("Test")));

    public final List<AutoAction> actions;

    Autos(List<AutoAction> actions) {
        this.actions = actions;
    }
}
