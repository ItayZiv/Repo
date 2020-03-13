package frc.megiddolions.lib.hardware.oi;

import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpiutil.CircularBuffer;

import java.util.function.DoubleSupplier;

public class AxisActiveTrigger extends Trigger {
    private final int kBufferSize;

    private final DoubleSupplier supplier;
    private CircularBuffer valueBuffer;

    public AxisActiveTrigger(DoubleSupplier supplier) {
        this(supplier, 10);
    }

    public AxisActiveTrigger(DoubleSupplier supplier, int bufferSize) {
        super();
        this.supplier = supplier;
        this.kBufferSize = bufferSize;
        valueBuffer = new CircularBuffer(kBufferSize);
    }

    @Override
    public boolean get() {
        valueBuffer.addFirst(supplier.getAsDouble());
        boolean active = true;
        for (int i = 0; i < kBufferSize; i++) {
            active = valueBuffer.get(i) != 0 && active;
        }
        return active;
    }
}
