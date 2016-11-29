package steffen.shooting;

import robocode.AdvancedRobot;
import robocode.Rules;
import robocode.StatusEvent;
import robocode.util.Utils;
import steffen.utils.ScannedEnemy;
import steffen.utils.Vector2D;

public class LinearPredictionShootingStrategy extends ShootingStrategy {
    public LinearPredictionShootingStrategy(AdvancedRobot robot) {
        super(robot);
    }

    @Override
    public void onStatusUpdate(StatusEvent event, ScannedEnemy scannedEnemy) {
        if(scannedEnemy != null) {
            double gunHeading = 90 - robot.getGunHeading();
            Vector2D distanceVector = getExpectedFirePosition(scannedEnemy).sub(getPosition());
            double bearing = distanceVector.getAngle();
            double gunTurn = Utils.normalRelativeAngleDegrees(bearing - gunHeading);
            robot.setTurnGunLeft(gunTurn);

            if(Math.abs(robot.getGunTurnRemaining()) < 2 && robot.getTime() - scannedEnemy.getScannedTick() < 5) {
                robot.setFire(getFirepower(distanceVector.getLength()));
            }
        }
    }

    private Vector2D getExpectedFirePosition(ScannedEnemy scannedEnemy) {
        Vector2D currentEnemyPosition = scannedEnemy.getExpectedPosition(robot.getTime());
        double distance = currentEnemyPosition.sub(getPosition()).getLength();
        double firepower = getFirepower(distance);
        double radius = robot.getWidth() / 2;

        long turnsToHit = (long)Math.ceil(distance / Rules.getBulletSpeed(firepower));
        Vector2D expectedFirePosition = scannedEnemy.getExpectedPosition(robot.getTime() + turnsToHit);

        for(int i=0; i<10; i++) {
            turnsToHit = (long)Math.ceil(expectedFirePosition.sub(getPosition()).getLength() / Rules.getBulletSpeed(firepower));
            Vector2D nextExpectedFirePosition = scannedEnemy.getExpectedPosition(robot.getTime() + turnsToHit);

            if(nextExpectedFirePosition.getX() < radius) {
                nextExpectedFirePosition.setX(radius);
            }

            if(nextExpectedFirePosition.getY() < radius) {
                nextExpectedFirePosition.setY(radius);
            }

            if(nextExpectedFirePosition.getX() > robot.getBattleFieldWidth() - radius) {
                nextExpectedFirePosition.setX(robot.getBattleFieldWidth() - radius);
            }

            if(nextExpectedFirePosition.getY() > robot.getBattleFieldHeight() - radius) {
                nextExpectedFirePosition.setY(robot.getBattleFieldHeight() - radius);
            }

            if(nextExpectedFirePosition.sub(expectedFirePosition).getLength() < 5) {
                return nextExpectedFirePosition;
            }
            expectedFirePosition = nextExpectedFirePosition;
        }

        return expectedFirePosition;
    }

    private double getFirepower(double distance) {
        return 400 / distance;
    }

    private Vector2D getPosition() {
        return new Vector2D(robot.getX(), robot.getY());
    }
}
