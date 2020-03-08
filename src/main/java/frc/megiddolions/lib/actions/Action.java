package frc.megiddolions.lib.actions;

@Deprecated
public interface Action {

    Runnable getAction();

    void setAction(Runnable action);

    void doAction();
}
