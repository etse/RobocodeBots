package steffen.utils;

public class Vector2D {
     private double x=0, y=0;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getLength() {
        return Math.sqrt(x*x + y*y);
    }

    public Vector2D add(Vector2D vector) {
        return new Vector2D(this.x + vector.x, this.y + vector.y);
    }

    public Vector2D add(double x, double y) {
        return add(new Vector2D(x, y));
    }

    public Vector2D sub(Vector2D vector) {
        return new Vector2D(this.x - vector.x, this.y - vector.y);
    }

    public Vector2D sub(double x, double y) {
        return sub(new Vector2D(x, y));
    }

    public Vector2D multiply(double m) {
        return new Vector2D(this.x * m, this.y * m);
    }

    public double getAngle() {
        return getAngleRadians() * 180 / Math.PI;
    }

    public double getAngleRadians() {
        return Math.atan2(y, x);
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public static Vector2D getVectorFromDegreesAndLength(double angle, double length) {
        return getVectorFromRadiansAndLength(angle * Math.PI / 180, length);
    }

    public static Vector2D getVectorFromRadiansAndLength(double angle, double length) {
        return new Vector2D(length * Math.cos(angle), length  * Math.sin(angle));
    }
}
