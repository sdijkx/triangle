package nl.triangle.plant.classifier.algorithms.coordinategroup;

/**
 * Created by steven on 16-06-16.
 */
public class IntCoordinateGroup implements CoordinateGroup<Integer> {
    public final int x;
    public final int y;

    public IntCoordinateGroup(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Integer x() {
        return x;
    }

    @Override
    public Integer y() {
        return y;
    }

    @Override
    public Integer zero() {
        return 0;
    }
}
