package steffen.movement;

import robocode.AdvancedRobot;
import robocode.StatusEvent;
import steffen.utils.ScannedEnemy;

public abstract class MovementStrategy {
    protected AdvancedRobot robot;

    MovementStrategy(AdvancedRobot robot) {
        this.robot = robot;
    }

    public abstract void handleMovement(StatusEvent statusEvent, ScannedEnemy scannedEnemy);
}
