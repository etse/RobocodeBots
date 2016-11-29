package steffen.scanning;

import robocode.AdvancedRobot;
import robocode.StatusEvent;
import robocode.util.Utils;
import steffen.utils.ScannedEnemy;
import steffen.utils.Vector2D;

public class FocusTrackingScanningStrategy extends ScanningStrategy {
    public FocusTrackingScanningStrategy(AdvancedRobot robot) {
        super(robot);
    }

    @Override
    public void onStatusUpdate(StatusEvent e, ScannedEnemy scannedEnemy) {
        if (scannedEnemy != null && robot.getTime() - scannedEnemy.getScannedTick() < 15) {
            double radarHeading = 90 - robot.getRadarHeading();
            double expectedEnemyBearing = scannedEnemy.getExpectedPosition(robot.getTime()).sub(getPosition()).getAngle();
            double radarTurn = Utils.normalRelativeAngleDegrees(expectedEnemyBearing - radarHeading);
            radarTurn = radarTurn > 0 ? 40 : -40;
            robot.setTurnRadarLeft(radarTurn);

        } else {
            robot.setTurnRadarRight(45);
        }
    }

    private Vector2D getPosition() {
        return new Vector2D(robot.getX(), robot.getY());
    }
}
