package frc.megiddolions.lib.actions;

import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.megiddolions.lib.hardware.power.CurrentSensor;

@Deprecated
public class CurrentAction extends Trigger implements Action {
    private Runnable m_action;
    private CurrentSensor m_sensor;
    private double m_currentThresh;

    public CurrentAction(CurrentSensor currentSensor, double thresh) {
        this(currentSensor, thresh, () -> {});
    }

    public CurrentAction(CurrentSensor currentSensor, double thresh, Runnable action) {
        m_sensor = currentSensor;
        m_currentThresh = thresh;
        m_action = action;
    }

    @Override
    public boolean get() {
        return m_sensor.getCurrent() >= m_currentThresh;
    }

    @Override
    public Runnable getAction() {
        return m_action;
    }

    @Override
    public void setAction(Runnable action) {
        m_action = action;
    }

    public CurrentSensor getCurrentSensor() {
        return m_sensor;
    }

    public void setCurrentSensor(CurrentSensor sensor) {
        m_sensor = sensor;
    }

    @Override
    public void doAction() {

    }
}
