package steffen;

import robocode.*;
import steffen.utils.Heading;

import java.awt.*;
import java.awt.geom.Ellipse2D;

import static robocode.util.Utils.normalRelativeAngleDegrees;

public class PredictionBot extends AdvancedRobot {
    public void run() {
        setColors(Color.magenta, Color.cyan, Color.black);
        setBulletColor(Color.yellow);
        setAdjustRadarForGunTurn(true);
        setAdjustGunForRobotTurn(true);

        setTurnRadarLeft(360);

        while (true) {
            if(getRadarTurnRemaining() < 5) {
                setTurnRadarLeft(360);
            }
            execute();
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        e.g
    }

    public void onWin(WinEvent e) {
        turnRight(20);
        turnLeft(40);
        turnRight(20);
    }

    public void onPaint(Graphics2D graphics) {
        double radius = getHeight() / 2;
        Shape antiCollisonCircle = new Ellipse2D.Double(getX() - radius, getY() - radius, 2 * radius, 2 * radius);
        graphics.setColor(new Color(255, 50, 50, 100));
        graphics.fill(antiCollisonCircle);
        graphics.setColor(Color.black);
        graphics.draw(antiCollisonCircle);
    }
}
