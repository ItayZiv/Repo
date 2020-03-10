package frc.megiddolions.lib.dashboard;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.megiddolions.Constants.DashboardConstants;
import frc.megiddolions.lib.InfiniteRecharge.ControlPanel;
import frc.megiddolions.lib.dashboard.test.DashboardUpdatable;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class Dashboard {

    private static Dashboard m_instance = null;

    public static Dashboard getInstance() {
        if (m_instance == null)
            m_instance = new Dashboard();
        return m_instance;
    }

    public final NetworkTableInstance ntInstance;
    private final NetworkTable dashboardTable;
    private HashMap<String, Supplier> data = new HashMap<>();

    private Dashboard() {
        ntInstance = NetworkTableInstance.getDefault();
        dashboardTable = ntInstance.getTable(DashboardConstants.kDashboardTable);
        Shuffleboard.getTab(DashboardConstants.kDashboardTable);
    }

    public void update() {
        data.forEach((name, supplier) -> dashboardTable.getEntry(name).setValue(supplier.get()));
    }

    public void addData(String name, Supplier supplier) {
        if (supplier.get().getClass() == ControlPanel.ControlPanelColor.class) {
            data.put(name, () -> {
                var color = (ControlPanel.ControlPanelColor)supplier.get();
                return new double[] {color.colorVal.red, color.colorVal.green, color.colorVal.blue};
            });
        }
        else
            data.put(name, supplier);
    }

    public <E extends Enum<E>> SendableChooser<E> addChooser(String name, E[] choosable, E defaultChosen) {
        SendableChooser<E> chooser = new SendableChooser<>();

        for (E _enum : choosable) {
            if (_enum == defaultChosen) {
                chooser.setDefaultOption(_enum.name(), _enum);
            } else {
                chooser.addOption(_enum.name(), _enum);
            }
        }

        Shuffleboard.getTab(DashboardConstants.kDashboardTable).add(name, chooser);

        return chooser;
    }
}
