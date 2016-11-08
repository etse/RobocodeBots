package steffen;

import robocode.*;
import steffen.utils.Heading;

import java.awt.*;

import static robocode.util.Utils.normalRelativeAngleDegrees;

public class SupermanBot extends AdvancedRobot {
    double firePower = 0.1;
    double driveDirection = 1;
    double lastDetection = 0;
    double optimalDistance = 300;
    double distanceFromWall = 100;

    public void run() {
        setBodyColor(Color.red);
        setGunColor(Color.cyan);
        setRadarColor(Color.blue);

        setAdjustRadarForGunTurn(true);
        setAdjustGunForRobotTurn(true);

        setTurnRadarLeft(360);
        execute();

        while (true) {
            double timeSinceDetection = getTime() - lastDetection;

            if(getGunTurnRemaining() < 5 && timeSinceDetection < 3) {
                fire(firePower);
            }

            if(timeSinceDetection > 5) {
                setTurnRadarLeft(25);
            }

            setAhead(100 * driveDirection);

            avoidHittingWalls();
            execute();
        }
    }

    private boolean isCloseToWall() {
        double x = getX(), y = getY();
        return (x < distanceFromWall || x > getBattleFieldWidth() - distanceFromWall || y < distanceFromWall || y > getBattleFieldHeight() - distanceFromWall);
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

    public void onScannedRobot(ScannedRobotEvent e) {
        firePower = 400 / e.getDistance();
        lastDetection = getTime();

        turnGunTowardsRobot(e);
        turnScannerToTrackRobot(e);

        if(!isCloseToWall()) {
            setDrivingDirection(e);
        }
    }

    private void turnScannerToTrackRobot(ScannedRobotEvent e) {
        double headingOffset = getHeading() - getRadarHeading() + e.getBearing();
        double optimalScanOffset = Rules.RADAR_TURN_RATE / 2;

        if (headingOffset > 0) {
            setTurnRadarRight(normalRelativeAngleDegrees(headingOffset + optimalScanOffset));
        } else {
            setTurnRadarRight(normalRelativeAngleDegrees(headingOffset - optimalScanOffset));
        }
    }

    private void turnGunTowardsRobot(ScannedRobotEvent e) {
        setTurnGunRight(getHeading() - getGunHeading() + e.getBearing());
    }

    private void setDrivingDirection(ScannedRobotEvent e) {
        boolean tooClose = optimalDistance > e.getDistance();

        double turnForNormalToRobot = e.getBearing() - 90;
        double turnToFixDistance = 5 * driveDirection * (tooClose ? -1 : 1);

        setTurnRight((turnForNormalToRobot + turnToFixDistance*3) / 4);
    }

    public void onWin(WinEvent e) {
        turnRight(20);
        fire(3);
        turnLeft(40);
        fire(3);
        turnRight(20);
    }
}
