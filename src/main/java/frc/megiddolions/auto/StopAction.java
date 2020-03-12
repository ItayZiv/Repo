package frc.megiddolions.auto;

import frc.megiddolions.lib.ObjectFactory;
import frc.megiddolions.lib.hardware.motors.Stoppable;

import java.util.List;

public class StopAction extends AutoAction {
    private final List<AutoStoppables> stoppableTypes;

    public StopAction(AutoStoppables... stoppables) {
        super(ActionType.Stop);
        this.stoppableTypes = List.of(stoppables);
    }

    public List<Stoppable> getStoppables(Stoppable driveTrain, Stoppable shooter, Stoppable intake) {
        List<Stoppable> stoppableList = List.of();
        stoppableTypes.forEach(stoppableType -> {
            switch (stoppableType) {
                case Drivetrain:
                    stoppableList.add(driveTrain);
                    break;
                case ShooterFeed:
                    stoppableList.add(shooter);
                    break;
                case Intake:
                    stoppableList.add(intake);
                    break;
            }
        });
        return stoppableList;
    }

    public static StopAction Stop() {
        return new StopAction(AutoStoppables.Drivetrain, AutoStoppables.ShooterFeed);
    }

    public enum AutoStoppables {
        Drivetrain,
        ShooterFeed,
        Intake
    }
}
