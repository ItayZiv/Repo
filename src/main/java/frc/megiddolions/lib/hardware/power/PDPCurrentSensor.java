package frc.megiddolions.lib.hardware.power;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class PDPCurrentSensor implements CurrentSensor {
    private PowerDistributionPanel pdp;
    private int port;

    public PDPCurrentSensor(int port) {
        pdp = new PowerDistributionPanel();
        this.port = port;
    }

    @Override
    public double getCurrent() {
        return pdp.getCurrent(port);
    }
}
