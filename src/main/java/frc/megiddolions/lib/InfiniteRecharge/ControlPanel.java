package frc.megiddolions.lib.InfiniteRecharge;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.util.Color;
import frc.megiddolions.lib.annotations.Tested;

import java.util.Map;

@Tested
public class ControlPanel {

    public enum ControlPanelColor {
        Blue(new Color(0.143, 0.427, 0.429)),
        Green(new Color(0.197, 0.561, 0.240)),
        Red(new Color(0.561, 0.232, 0.114)),
        Yellow(new Color(0.361, 0.524, 0.113)),
        Unknown(new Color(0, 0, 0));

        public final Color colorVal;

        ControlPanelColor(Color color) {
            colorVal = color;
        }
    }
    
    public static final Map<ControlPanelColor, ControlPanelColor> kColorOffsetMap = Map.of(
            ControlPanelColor.Yellow, ControlPanelColor.Green,
            ControlPanelColor.Red, ControlPanelColor.Blue,
            ControlPanelColor.Green, ControlPanelColor.Yellow,
            ControlPanelColor.Blue, ControlPanelColor.Red);

    public static ControlPanelColor getOffsetColorAssignment() {
        return kColorOffsetMap.get(getColorAssignment());
    }

    public static ControlPanelColor getColorAssignment() {
        return parseMessage(DriverStation.getInstance().getGameSpecificMessage());
    }

    private static ControlPanelColor parseMessage(String message) {
        if (message == null || message.length() == 0) return ControlPanelColor.Unknown;

        char colorCode = message.toUpperCase().charAt(0);
        switch (colorCode) {
            case 'B': return ControlPanelColor.Blue;
            case 'G': return ControlPanelColor.Green;
            case 'R': return ControlPanelColor.Red;
            case 'Y': return ControlPanelColor.Yellow;
            default: return ControlPanelColor.Unknown;
        }
    }
}
