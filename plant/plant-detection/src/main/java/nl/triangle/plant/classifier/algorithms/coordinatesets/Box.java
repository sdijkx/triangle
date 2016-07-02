package nl.triangle.plant.classifier.algorithms.coordinatesets;

/**
 * Created by steven on 10-04-16.
 */
class Box {
    private final Coordinate leftTop;
    private final Coordinate rightBottom;

    Box(Coordinate leftTop, Coordinate rightBottom) {
        this.leftTop = leftTop;
        this.rightBottom = rightBottom;
    }

    public int getWidth() {
        return (int) (rightBottom.getX() - leftTop.getX());
    }

    public int getHeight() {
        return (int) (rightBottom.getY() - leftTop.getY());
    }

    @Override
    public String toString() {
        return "Box{" +
                "leftTop=" + leftTop +
                ", rightBottom=" + rightBottom +
                '}';
    }
}
