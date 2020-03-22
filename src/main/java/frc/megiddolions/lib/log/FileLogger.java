package frc.megiddolions.lib.log;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.RobotController;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.LongSupplier;

public class FileLogger {
    private FileLogger m_instance = null;

    public synchronized FileLogger getInstance() {
        if (m_instance != null)
            m_instance = new FileLogger();
        return m_instance;
    }

    private Map<String, FileData> dataEntries;
    private FileWriter csvWriter;
    private LongSupplier timestamp;
    private boolean running = false;

    private FileLogger() {
        makeWriter();
    }

    private void makeWriter() {
        try {
            csvWriter = new FileWriter(Filesystem.getDeployDirectory().toPath().resolve("WrittenFile.csv").toFile());
        }
        catch (IOException e) {
            DriverStation.reportError("Error opening file to log CSV", e.getStackTrace());
        }
    }

    private void writeKeys() throws IOException {
        csvWriter.append("timestamp,");
        csvWriter.append(String.join(",", dataEntries.keySet()));
        csvWriter.append("\n");
    }

    public void start() {
        start(RobotController::getFPGATime);
    }

    public void start(LongSupplier timestamp) {
        try {
            writeKeys();
        }
        catch (IOException e) {
            DriverStation.reportError("Error writing keys to CSV", e.getStackTrace());
        }
        this.timestamp = timestamp;
        running = true;
    }

    public void update() {
        if (running) {
            List<String> data = new java.util.ArrayList<String>(List.of("" + timestamp.getAsLong()));
            dataEntries.forEach((key, dataSupplier) -> data.add(dataSupplier.getData().toString()));
            try {
                csvWriter.append(String.join(",", data));
                csvWriter.append("\n");
                csvWriter.flush();
            } catch (IOException e) {
                DriverStation.reportError("Error writing data to CSV", e.getStackTrace());
            }
        }
    }

    public void finish() {
        try {
            csvWriter.flush();

            csvWriter.close();
        } catch (IOException e) {
            DriverStation.reportError("Error when closing write stream", e.getStackTrace());
        }
        running = false;
    }

    public void addData(String name, FileData data) {
        if (!running)
            dataEntries.put(name, data);
    }

}
