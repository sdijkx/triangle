package nl.triangle.plant.classifier.algorithms.coordinatesets;

/**
 * Created by sdi20386 on 11-4-2015.
 */
public class Coordinate {

    public final double x;
    public final double y;

    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
