package steffen.utils;

import static robocode.util.Utils.normalAbsoluteAngleDegrees;

public enum Heading {
    NORTH(0), NORTH_EAST(45), EAST(90), SOUTH_EAST(135), SOUTH(180), SOUTH_WEST(225), WEST(270), NORTH_WEST(315);

    public double angle;

    Heading(double angle) {
        this.angle = angle;
    }

    public boolean containsDirection(double angle) {
        double absoluteAngle = normalAbsoluteAngleDegrees(angle);
        double anglediff = (absoluteAngle - this.angle + 180 + 360) % 360 - 180;
        return anglediff > -22.5 && anglediff <= 22.5;
    }

    public static Heading getHeadingForAngle(double angle) {
        double absoluteAngle = normalAbsoluteAngleDegrees(angle);
        for(Heading heading : Heading.values()) {
            if(heading.containsDirection(absoluteAngle)) {
                return heading;
            }
        }
        return Heading.NORTH;
    }
}
