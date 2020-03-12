package frc.megiddolions.auto;

import frc.megiddolions.Constants;

import java.util.List;

public enum Autos {
    Test(List.of(new RamseteAction(Constants.AutoConstants.kTestAuto))),
    Full(List.of(AutoAction.SpinShooter(), new DelayAction(2), new AutoAction(ActionType.StartIntake),
            AutoAction.Align(), AutoAction.Feed(), DelayAction.FeedDelay(), StopAction.Stop(), DelayAction.FeedDelay(),
            AutoAction.Align(), AutoAction.Feed(), DelayAction.FeedDelay(), StopAction.Stop(), DelayAction.FeedDelay(),
            AutoAction.Align(), AutoAction.Feed(), DelayAction.FeedDelay(), StopAction.Stop(), DelayAction.FeedDelay(),
            new AutoAction(ActionType.End), new AutoAction(ActionType.StartIntake),
            new RamseteAction(Constants.AutoConstants.kBasicPickup), new StopAction(StopAction.AutoStoppables.Intake),
            AutoAction.SpinShooter(), new RamseteAction(Constants.AutoConstants.kBasicShoot),
            AutoAction.Align(), AutoAction.Feed(), DelayAction.FeedDelay(), StopAction.Stop(), DelayAction.FeedDelay(),
            AutoAction.Align(), AutoAction.Feed(), DelayAction.FeedDelay(), StopAction.Stop(), DelayAction.FeedDelay(),
            AutoAction.Align(), AutoAction.Feed(), DelayAction.FeedDelay(), StopAction.Stop(), DelayAction.FeedDelay(),
            AutoAction.Align(), AutoAction.Feed(), DelayAction.FeedDelay(), StopAction.Stop(), DelayAction.FeedDelay(),
            new AutoAction(ActionType.End)));

    public final List<AutoAction> actions;

    Autos(List<AutoAction> actions) {
        this.actions = actions;
    }
}
