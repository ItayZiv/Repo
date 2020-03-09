package frc.megiddolions.lib.dashboard;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.megiddolions.Constants.DashboardConstants;
import frc.megiddolions.lib.InfiniteRecharge.ControlPanel;

import java.util.List;

public class Dashboard {

    private static Dashboard m_instance = null;

    public static Dashboard getInstance() {
        if (m_instance == null)
            m_instance = new Dashboard();
        return m_instance;
    }

    public final NetworkTableInstance ntInstance;
    private final NetworkTable dashboardTable;
    private List<DashboardItem> dataList;

    private Dashboard() {
        ntInstance = NetworkTableInstance.getDefault();
        dashboardTable = ntInstance.getTable(DashboardConstants.kDashboardTable);
    }

    public void update() {
        for (DashboardItem dashboardItem : dataList) {
            dashboardTable.getEntry(dashboardItem.getName()).setValue(dashboardItem.getData());
        }
    }

    public void addData(DashboardItem data) {
        if (data.defaultValue instanceof ControlPanel.ControlPanelColor) {
            ControlPanel.ControlPanelColor defaultColor = (ControlPanel.ControlPanelColor)data.defaultValue;
            dataList.add(new DashboardItem<>(() -> {
                ControlPanel.ControlPanelColor color = (ControlPanel.ControlPanelColor) data.getData();
                return new double[] {color.colorVal.red, color.colorVal.green, color.colorVal.blue};
            }, data.getName(), new double[] {defaultColor.colorVal.red, defaultColor.colorVal.green, defaultColor.colorVal.blue}));
        }
        else
            dataList.add(data);
    }
}
