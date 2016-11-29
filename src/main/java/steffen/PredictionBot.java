package steffen;

import robocode.*;
import steffen.movement.CirclingMovementStrategy;
import steffen.movement.CrazyMovementStrategy;
import steffen.movement.MovementStrategy;
import steffen.scanning.FocusTrackingScanningStrategy;
import steffen.scanning.ScanningStrategy;
import steffen.shooting.LinearPredictionShootingStrategy;
import steffen.shooting.ShootingStrategy;
import steffen.utils.ScannedEnemy;
import steffen.utils.Vector2D;

import java.awt.*;

public class PredictionBot extends AdvancedRobot {
    public ScannedEnemy scannedEnemy;
    ShootingStrategy shootingStrategy = new LinearPredictionShootingStrategy(this);
    ScanningStrategy scanningStrategy = new FocusTrackingScanningStrategy(this);

    CirclingMovementStrategy circlingMovementStrategy = new CirclingMovementStrategy(this);
    CrazyMovementStrategy crazyMovementStrategy = new CrazyMovementStrategy(this);
    MovementStrategy movementStrategy = circlingMovementStrategy;

    public void run() {
        setColors(Color.red, Color.yellow, Color.green);
        setBulletColor(Color.yellow);

        setAdjustRadarForGunTurn(true);
        setAdjustGunForRobotTurn(true);
        while(true) {
            execute();
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        double enemyHeading = (Math.PI / 2) - e.getHeadingRadians();
        Vector2D velocity = new Vector2D(Math.cos(enemyHeading) * e.getVelocity(), Math.sin(enemyHeading) * e.getVelocity());
        Vector2D myPosition = new Vector2D(getX(), getY());

        double myHeading = (Math.PI / 2) - getHeadingRadians();
        double bearing = myHeading - e.getBearingRadians();
        Vector2D distanceVector = new Vector2D(Math.cos(bearing) * e.getDistance(), Math.sin(bearing) * e.getDistance());
        Vector2D enemyPosition = myPosition.add(distanceVector);

        scannedEnemy = new ScannedEnemy(getTime(), enemyPosition, velocity);
    }

    public void onStatus(StatusEvent e) {
        scanningStrategy.onStatusUpdate(e, scannedEnemy);
        shootingStrategy.onStatusUpdate(e, scannedEnemy);
        movementStrategy.onStatusUpdate(e, scannedEnemy);

        if(getTime() % 200 < 150) {
            movementStrategy = circlingMovementStrategy;
        } else {
            movementStrategy = crazyMovementStrategy;
        }
    }

    public void onHitRobot(HitRobotEvent e) {
        movementStrategy.onHitRobot(e);
    }

    public void onWin(WinEvent e) {
        turnRight(20);
        turnLeft(40);
        turnRight(20);
    }
}
