package steffen.scanning;

import robocode.AdvancedRobot;
import robocode.StatusEvent;
import steffen.utils.ScannedEnemy;

public abstract class ScanningStrategy {
    AdvancedRobot robot;

    public ScanningStrategy(AdvancedRobot robot) {
        this.robot = robot;
    }

    public abstract void onStatusUpdate(StatusEvent e, ScannedEnemy scannedEnemy);
}
