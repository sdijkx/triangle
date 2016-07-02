package nl.triangle.plant.classifier.algorithms.imagedescriptor.cell;

/**
 * Created by steven on 11-06-16.
 */
class GradientVector {
    private final double x, y, angle, magnitude;

    protected GradientVector(double x, double y) {
        this.x = x;
        this.y = y;
        angle = Math.toDegrees(Math.atan2(x, y));
        magnitude = Math.sqrt(x * x + y * y);
    }

    public double getAngle() {
        return angle;
    }

    public double getMagnitude() {
        return magnitude;
    }
}
