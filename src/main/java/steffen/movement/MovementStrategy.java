package steffen.movement;

import robocode.AdvancedRobot;
import robocode.HitRobotEvent;
import robocode.StatusEvent;
import steffen.utils.ScannedEnemy;

public abstract class MovementStrategy {
    protected AdvancedRobot robot;

    MovementStrategy(AdvancedRobot robot) {
        this.robot = robot;
    }

    public abstract void onStatusUpdate(StatusEvent statusEvent, ScannedEnemy scannedEnemy);

    public abstract void onHitRobot(HitRobotEvent event);
}
