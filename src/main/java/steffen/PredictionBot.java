package steffen;

import robocode.*;
import robocode.util.Utils;
import steffen.utils.Heading;
import steffen.utils.ScannedEnemy;
import steffen.utils.Vector2D;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class PredictionBot extends AdvancedRobot {
    ScannedEnemy scannedEnemy;
    double driveDirection = 1;
    double distanceFromWall = 100;
    double optimalDistance = 300;

    public void run() {
        setColors(Color.magenta, Color.cyan, Color.black);
        setBulletColor(Color.yellow);
        setAdjustRadarForGunTurn(true);
        setAdjustGunForRobotTurn(true);
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

        if(!isCloseToWall()) {
            setDrivingDirection(e);
        }
    }

    public void onStatus(StatusEvent e) {
        if (scannedEnemy != null && getTime() - scannedEnemy.getScannedTick() < 15) {
            double gunHeading = 90 - getGunHeading();
            Vector2D distanceVector = getExpectedFirePosition().sub(getPosition());
            double bearing = distanceVector.getAngle();
            double gunTurn = Utils.normalRelativeAngleDegrees(bearing - gunHeading);
            setTurnGunLeft(gunTurn);

            if(Math.abs(getGunTurnRemaining()) < 5) {
                setFire(getFirepower(distanceVector.getLength()));
            }

            double radarHeading = 90 - getRadarHeading();
            double expectedEnemyBearing = scannedEnemy.getExpectedPosition(getTime()).sub(getPosition()).getAngle();
            double radarTurn = Utils.normalRelativeAngleDegrees(expectedEnemyBearing - radarHeading);
            radarTurn = radarTurn > 0 ? 40 : -40;
            setTurnRadarLeft(radarTurn);

        } else {
            setTurnRadarRight(45);
        }
        avoidHittingWalls();
        setAhead(100 * driveDirection);
        execute();
    }

    public void onWin(WinEvent e) {
        turnRight(20);
        turnLeft(40);
        turnRight(20);
    }

    private double getFirepower(double distance) {
        return 400 / distance;
    }

    private Vector2D getPosition() {
        return new Vector2D(getX(), getY());
    }

    private Vector2D getExpectedFirePosition() {
        Vector2D currentEnemyPosition = scannedEnemy.getExpectedPosition(getTime());
        double distance = currentEnemyPosition.sub(getPosition()).getLength();
        double firepower = getFirepower(distance);
        double radius = getWidth() / 2;

        long turnsToHit = (long)Math.ceil(distance / Rules.getBulletSpeed(firepower));
        Vector2D expectedFirePosition = scannedEnemy.getExpectedPosition(getTime() + turnsToHit);

        for(int i=0; i<10; i++) {
            turnsToHit = (long)Math.ceil(expectedFirePosition.sub(getPosition()).getLength() / Rules.getBulletSpeed(firepower));
            Vector2D nextExpectedFirePosition = scannedEnemy.getExpectedPosition(getTime() + turnsToHit);

            if(nextExpectedFirePosition.getX() < radius) {
                nextExpectedFirePosition.setX(radius);
            }

            if(nextExpectedFirePosition.getY() < radius) {
                nextExpectedFirePosition.setY(radius);
            }

            if(nextExpectedFirePosition.getX() > getBattleFieldWidth() - radius) {
                nextExpectedFirePosition.setX(getBattleFieldWidth() - radius);
            }

            if(nextExpectedFirePosition.getY() > getBattleFieldHeight() - radius) {
                nextExpectedFirePosition.setX(getBattleFieldHeight() - radius);
            }

            if(nextExpectedFirePosition.sub(expectedFirePosition).getLength() < 5) {
                return nextExpectedFirePosition;
            }
            expectedFirePosition = nextExpectedFirePosition;
        }

        return expectedFirePosition;
    }

    private boolean isCloseToWall() {
        double x = getX(), y = getY();
        return (x < distanceFromWall || x > getBattleFieldWidth() - distanceFromWall || y < distanceFromWall || y > getBattleFieldHeight() - distanceFromWall);
    }

    private void setDrivingDirection(ScannedRobotEvent e) {
        boolean tooClose = optimalDistance > e.getDistance();

        double turnForNormalToRobot = e.getBearing() - 90;
        double turnToFixDistance = 5 * driveDirection * (tooClose ? -1 : 1);

        setTurnRight((turnForNormalToRobot + turnToFixDistance*3) / 4);
    }

    private void avoidHittingWalls() {
        double x = getX(), y=getY();
        double turnStrength = 45;
        boolean shouldChangeDriveDirection = false;
        Heading heading = Heading.getHeadingForAngle(driveDirection == 1 ? getHeading() : getHeading() + 180);

        if(x < distanceFromWall) {
            if(heading == Heading.NORTH_WEST || heading == Heading.NORTH) {
                setTurnRight(turnStrength);
            }
            if(heading == Heading.SOUTH_WEST || heading == Heading.SOUTH) {
                setTurnLeft(turnStrength);
            }
            if(heading == Heading.WEST) {
                shouldChangeDriveDirection = true;
            }
        }

        if(x > getBattleFieldWidth() - distanceFromWall) {
            if(heading == Heading.NORTH_EAST || heading == Heading.NORTH) {
                setTurnLeft(turnStrength);
            }
            if(heading == Heading.SOUTH_EAST || heading == Heading.SOUTH) {
                setTurnRight(turnStrength);
            }
            if(heading == Heading.EAST) {
                shouldChangeDriveDirection = true;
            }
        }
        if(y < distanceFromWall) {
            if(heading == Heading.SOUTH_WEST || heading == Heading.WEST) {
                setTurnRight(turnStrength);
            }
            if(heading == Heading.SOUTH_EAST || heading == Heading.EAST) {
                setTurnLeft(turnStrength);
            }
            if(heading == Heading.SOUTH) {
                shouldChangeDriveDirection = true;
            }
        }

        if(y > getBattleFieldHeight() - distanceFromWall) {
            if(heading == Heading.NORTH_WEST || heading == Heading.WEST) {
                setTurnLeft(turnStrength);
            }
            if(heading == Heading.NORTH_EAST || heading == Heading.EAST) {
                setTurnRight(turnStrength);
            }
            if(heading == Heading.NORTH) {
                shouldChangeDriveDirection = true;
            }
        }

        if (shouldChangeDriveDirection) {
            driveDirection = -driveDirection;
        }
    }

    public void onHitRobot(HitRobotEvent e) {
        driveDirection = -driveDirection;
    }

    public void onPaint(Graphics2D graphics) {
        double radius = getHeight() / 2;
        Shape antiCollisonCircle = new Ellipse2D.Double(getX() - radius, getY() - radius, 2 * radius, 2 * radius);
        graphics.setColor(new Color(255, 50, 50, 100));
        graphics.fill(antiCollisonCircle);
        graphics.setColor(Color.black);
        graphics.draw(antiCollisonCircle);

        if (scannedEnemy != null) {
            Vector2D enemyPosition = getExpectedFirePosition();
            Shape expectedPositionCircle = new Ellipse2D.Double(enemyPosition.getX() - radius, enemyPosition.getY() - radius, 2 * radius, 2 * radius);
            graphics.setColor(new Color(255, 50, 50, 100));
            graphics.fill(expectedPositionCircle);
            graphics.setColor(Color.black);
            graphics.draw(expectedPositionCircle);
        }
    }
}
