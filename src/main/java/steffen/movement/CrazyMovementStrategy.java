package steffen.movement;

import robocode.AdvancedRobot;
import robocode.HitRobotEvent;
import robocode.Rules;
import robocode.StatusEvent;
import robocode.util.Utils;
import steffen.utils.Heading;
import steffen.utils.ScannedEnemy;
import steffen.utils.Vector2D;

import java.util.Random;

public class CrazyMovementStrategy extends MovementStrategy {
    Random random = new Random();
    long nextTurnTick = 0;
    long nextChangeDirectionTick = 0;
    int driveDirection = 1;
    public CrazyMovementStrategy(AdvancedRobot robot) {
        super(robot);
        nextTurnTick = 10 + random.nextInt(60);
        nextChangeDirectionTick = 10 + random.nextInt(60);
    }

    @Override
    public void onStatusUpdate(StatusEvent statusEvent, ScannedEnemy enemy) {
        if(robot.getTime() > nextChangeDirectionTick) {
            nextChangeDirectionTick = robot.getTime() + 10 + random.nextInt(60);
            driveDirection = -driveDirection;
        }

        if(robot.getTime() > nextTurnTick) {
            nextTurnTick = robot.getTime() + 10 + random.nextInt(60);
            robot.setTurnRight(random.nextInt(200) - 100);
        }

        robot.setAhead(100);
    }

    @Override
    public void onHitRobot(HitRobotEvent event) {
        driveDirection = -driveDirection;
    }

}
