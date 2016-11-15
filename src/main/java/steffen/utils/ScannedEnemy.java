package steffen.utils;

public class ScannedEnemy {
    private long scannedTick;
    private Vector2D position;
    private Vector2D velocity;

    public ScannedEnemy(long tick, Vector2D position, Vector2D velocity) {
        this.scannedTick = tick;
        this.position = position;
        this.velocity = velocity;
    }

    public Vector2D getExpectedPosition(long currentTick) {
        int timeSinceScan = (int) (currentTick - scannedTick);
        return velocity.multiply(timeSinceScan).add(position);
    }

    public long getScannedTick() {
        return scannedTick;
    }
}
