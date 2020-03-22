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
import java.util.UUID;

public class FileTransfer {
    private static final UUID kUUID = UUID.fromString("e378f733-abc5-4df7-8b21-da1cde4ad436");

    private FileTransfer m_instance = null;

    public synchronized FileTransfer getInstance() {
        if (m_instance != null)
            m_instance = new FileTransfer();
        return m_instance;
    }

    public boolean running = false;
    private BufferedReader csvReader;

    private NetworkTable UUIDTable;
    private NetworkTableEntry serverReady;

    private NetworkTable table;
    private NetworkTableEntry sentCount;
    private NetworkTableEntry receivedCount;
    private NetworkTableEntry done;

    private FileTransfer() {
        makeReader();
        UUIDTable = NetworkTableInstance.getDefault().getTable(kUUID.toString());
        serverReady = UUIDTable.getEntry("serverReady");
    }

    private void makeReader() {
        try {
            csvReader = new BufferedReader(
                    new FileReader(Filesystem.getDeployDirectory().toPath().resolve("WrittenFile.csv").toFile()));
        } catch (FileNotFoundException e) {
            DriverStation.reportError("Error opening CSV", e.getStackTrace());
        }
    }

    public boolean initialize() {
        if (!serverReady.getBoolean(false)) {
            if (UUIDTable.getEntry("clientReady").getBoolean(false)) {
                table = NetworkTableInstance.getDefault().getTable(UUIDTable.getEntry("table")
                        .getString(""));
                sentCount = table.getEntry("sentCount");
                receivedCount = table.getEntry("receivedCount");
                (done = table.getEntry("done")).setBoolean(false);
                serverReady.setBoolean(true);
            }
            else
                return false;
        }
        return true;
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
