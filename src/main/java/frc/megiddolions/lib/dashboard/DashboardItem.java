package frc.megiddolions.lib.dashboard;

public class DashboardItem<T> implements DashboardData<T>{
    private final DashboardData<T> data;
    private final String name;
    public final T defaultValue;

    public DashboardItem(DashboardData<T> data, String name, T defaultValue) {
        this.data = data;
        this.name = name;
        this.defaultValue = defaultValue;
    }

    @Override
    public T getData() {
        return data.getData();
    }

    public String getName() {
        return name;
    }
}
