package frc.megiddolions.auto;

public class DelayAction extends AutoAction {
    private final double length;

    public DelayAction(double length) {
        super(ActionType.Delay);
        this.length = length;
    }

    public double getLength() {
        return length;
    }

}
