package frc.megiddolions.lib.log;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class FileTransfer {
    private static final String kTableName = "logger";

    private FileTransfer m_instance = null;

    public synchronized FileTransfer getInstance() {
        if (m_instance != null)
            m_instance = new FileTransfer();
        return m_instance;
    }

    public boolean running = false;
    private BufferedReader csvReader;
    private NetworkTable table;
    private NetworkTableEntry sentCount;
    private NetworkTableEntry receivedCount;
    private NetworkTableEntry done;

    private FileTransfer() {
        makeReader();
        table = NetworkTableInstance.getDefault().getTable(kTableName);
        sentCount = table.getEntry("sentCount");
        receivedCount = table.getEntry("receivedCount");
        (done = table.getEntry("done")).setBoolean(false);
    }

    private void makeReader() {
        try {
            csvReader = new BufferedReader(
                    new FileReader(Filesystem.getDeployDirectory().toPath().resolve("WrittenFile.csv").toFile()));
        } catch (FileNotFoundException e) {
            DriverStation.reportError("Error opening CSV", e.getStackTrace());
        }
    }

    public void start() {
        try {
            String row;
            if ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                table.getEntry("keys").setStringArray(data);
            }
        }
        catch (IOException e) {
            DriverStation.reportError("Error reading keys from CSV", e.getStackTrace());
        }
        running = true;
    }

    public void update() {
        if (running &&
                receivedCount.getNumber(0).longValue() > sentCount.getNumber(0).longValue()) {
            String row;
            try {
                if ((row = csvReader.readLine()) != null) {
                    String[] stringData = row.split(",");
                    List<Number> data = new java.util.ArrayList<Number>();

                    List.of(stringData).forEach((string) -> data.add(Double.parseDouble(string)));

                    table.getEntry("values").setNumberArray((Number[]) data.toArray());

                    sentCount.setNumber(sentCount.getNumber(0).longValue() + 1);
                }
                else {
                    finish();
                }
            } catch (IOException e) {
                DriverStation.reportError("Error reading data from CSV", e.getStackTrace());
            }
        }
    }

    public void finish() {
        try {
            csvReader.close();
        } catch (IOException e) {
            DriverStation.reportError("Error when closing reader", e.getStackTrace());
        }
        running = false;
        done.setBoolean(true);
    }
}
