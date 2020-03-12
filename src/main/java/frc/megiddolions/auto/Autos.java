package frc.megiddolions.auto;

import frc.megiddolions.Constants;

public enum Autos {
    Test(new Auto(new RamseteAction(Constants.AutoConstants.kTestAuto))),
    Full(new Auto(new AutoAction(ActionType.SpinShooter), new DelayAction(2), new AutoAction(ActionType.StartIntake),
            new AutoAction(ActionType.Shoot), new AutoAction(ActionType.Shoot), new AutoAction(ActionType.Shoot),
            new AutoAction(ActionType.End), new AutoAction(ActionType.StartIntake),
            new RamseteAction(Constants.AutoConstants.kBasicPickup), new StopAction(StopAction.AutoStoppables.Intake),
            new AutoAction(ActionType.SpinShooter), new RamseteAction(Constants.AutoConstants.kBasicShoot),
            new AutoAction(ActionType.Shoot), new AutoAction(ActionType.Shoot), new AutoAction(ActionType.Shoot),
            new AutoAction(ActionType.Shoot), new AutoAction(ActionType.End)));

    public final Auto auto;

    Autos(Auto auto) {
        this.auto = auto;
    }
}
