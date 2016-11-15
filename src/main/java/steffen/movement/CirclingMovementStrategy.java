package steffen.movement;

import robocode.AdvancedRobot;
import robocode.StatusEvent;
import robocode.util.Utils;
import steffen.utils.Heading;
import steffen.utils.ScannedEnemy;
import steffen.utils.Vector2D;

public class CirclingMovementStrategy extends MovementStrategy {
    double driveDirection = 1;
    double distanceFromWall = 100;
    double optimalDistance = 300;

    public CirclingMovementStrategy(AdvancedRobot robot) {
        super(robot);
    }

    @Override
    public void handleMovement(StatusEvent statusEvent, ScannedEnemy enemy) {

        if (isCloseToWall() || enemy == null) {
            avoidHittingWalls();
        } else {
            setDrivingDirection(statusEvent, enemy);
        }
        robot.setAhead(100 * driveDirection);
    }

    private boolean isCloseToWall() {
        double x = robot.getX(), y = robot.getY();
        return (x < distanceFromWall || x > robot.getBattleFieldWidth() - distanceFromWall || y < distanceFromWall || y > robot.getBattleFieldHeight() - distanceFromWall);
    }

    private void setDrivingDirection(StatusEvent statusEvent, ScannedEnemy enemy) {
        Vector2D myPosition = new Vector2D(robot.getX(), robot.getY());
        Vector2D distanceVector = enemy.getExpectedPosition(statusEvent.getTime()).sub(myPosition);
        Vector2D myDirection = Vector2D.getVectorFromDegreesAndLength(90 - robot.getHeading(), robot.getVelocity());

        boolean tooClose = optimalDistance > distanceVector.getLength();

        double bearing =  distanceVector.getAngle() - myDirection.getAngle();
        double turnForNormalToRobot = Utils.normalRelativeAngleDegrees(bearing - 90);
        double turnToFixDistance = 5 * driveDirection * (tooClose ? -1 : 1);

        robot.setDebugProperty("bearing", String.format("%f", bearing));
        robot.setDebugProperty("turnRight", String.format("%f", turnForNormalToRobot));

        robot.setTurnLeft((turnForNormalToRobot + turnToFixDistance*3) / 4);
    }

    private void avoidHittingWalls() {
        double x = robot.getX(), y=robot.getY();
        double turnStrength = 45;
        boolean shouldChangeDriveDirection = false;
        Heading heading = Heading.getHeadingForAngle(driveDirection == 1 ? robot.getHeading() : robot.getHeading() + 180);

        if(x < distanceFromWall) {
            if(heading == Heading.NORTH_WEST || heading == Heading.NORTH) {
                robot.setTurnRight(turnStrength);
            }
            if(heading == Heading.SOUTH_WEST || heading == Heading.SOUTH) {
                robot.setTurnLeft(turnStrength);
            }
            if(heading == Heading.WEST) {
                shouldChangeDriveDirection = true;
            }
        }

        if(x > robot.getBattleFieldWidth() - distanceFromWall) {
            if(heading == Heading.NORTH_EAST || heading == Heading.NORTH) {
                robot.setTurnLeft(turnStrength);
            }
            if(heading == Heading.SOUTH_EAST || heading == Heading.SOUTH) {
                robot.setTurnRight(turnStrength);
            }
            if(heading == Heading.EAST) {
                shouldChangeDriveDirection = true;
            }
        }
        if(y < distanceFromWall) {
            if(heading == Heading.SOUTH_WEST || heading == Heading.WEST) {
                robot.setTurnRight(turnStrength);
            }
            if(heading == Heading.SOUTH_EAST || heading == Heading.EAST) {
                robot.setTurnLeft(turnStrength);
            }
            if(heading == Heading.SOUTH) {
                shouldChangeDriveDirection = true;
            }
        }

        if(y > robot.getBattleFieldHeight() - distanceFromWall) {
            if(heading == Heading.NORTH_WEST || heading == Heading.WEST) {
                robot.setTurnLeft(turnStrength);
            }
            if(heading == Heading.NORTH_EAST || heading == Heading.EAST) {
                robot.setTurnRight(turnStrength);
            }
            if(heading == Heading.NORTH) {
                shouldChangeDriveDirection = true;
            }
        }

        if (shouldChangeDriveDirection) {
            driveDirection = -driveDirection;
        }
    }
}
