package steffen.shooting;

import robocode.AdvancedRobot;
import robocode.StatusEvent;
import steffen.utils.ScannedEnemy;

public abstract class ShootingStrategy {
    AdvancedRobot robot;

    public ShootingStrategy(AdvancedRobot robot) {
        this.robot = robot;
    }

    public abstract void onStatusUpdate(StatusEvent event, ScannedEnemy scannedEnemy);
}
