package nl.triangle.plant.pipeline.dto;

/**
 * Created by steven on 14-05-16.
 */
public class DropModel {
    private final int x;
    private final int y;
    private final int weight;

    public DropModel(int x, int y, int weight) {
        this.x = x;
        this.y = y;
        this.weight = weight;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWeight() {
        return weight;
    }

}
